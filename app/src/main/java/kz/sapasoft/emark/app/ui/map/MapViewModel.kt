package kz.sapasoft.emark.app.ui.map

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch
import kz.sapasoft.emark.app.core.App
import kz.sapasoft.emark.app.core.BaseViewModel
import kz.sapasoft.emark.app.data.cloud.ResultWrapper
import kz.sapasoft.emark.app.data.cloud.repository.BaseCloudRepository
import kz.sapasoft.emark.app.data.local.prefs.PrefsImpl
import kz.sapasoft.emark.app.data.local.room.image.ImageRepository
import kz.sapasoft.emark.app.data.local.room.marker.MarkerRepository
import kz.sapasoft.emark.app.data.local.room.marker_sync.MarkerSyncRepository
import kz.sapasoft.emark.app.data.local.room.project.ProjectRepository
import kz.sapasoft.emark.app.data.local.room.template.TemplateRepository
import kz.sapasoft.emark.app.domain.model.MarkerModel
import kz.sapasoft.emark.app.domain.model.ProjectModel
import kz.sapasoft.emark.app.domain.model.TemplateModel
import kz.sapasoft.emark.app.domain.model.response.ErrorStatus
import kz.sapasoft.emark.app.utils.Constants
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.UUID
import javax.inject.Inject

class MapViewModel @Inject constructor(
    baseCloudRepository2: BaseCloudRepository,
    templateRepository2: TemplateRepository,
    imageRepository2: ImageRepository,
    markerRepository2: MarkerRepository,
    markerSyncRepository2: MarkerSyncRepository,
    projectsRepository2: ProjectRepository,
    prefsImpl2: PrefsImpl
) : BaseViewModel() {
    private val TAG = javaClass.simpleName

    /* access modifiers changed from: private */
    @JvmField
    val baseCloudRepository: BaseCloudRepository
    private val `error$delegate`: MutableLiveData<ResultWrapper.Error> by lazy {
        MutableLiveData<ResultWrapper.Error>()
    }
    private val imageRepository: ImageRepository

    /* access modifiers changed from: private */
    @JvmField
    val markerListAll: MutableList<MarkerModel> = mutableListOf<MarkerModel>()

    /* access modifiers changed from: private */
    @JvmField
    val markerRepository: MarkerRepository

    @JvmField
    val projectsRepository: ProjectRepository

    /* access modifiers changed from: private */
    @JvmField
    val markerSyncRepository: MarkerSyncRepository

    /* access modifiers changed from: private */
    @JvmField
    var page = 1

    /* access modifiers changed from: private */
    @JvmField
    val prefsImpl: PrefsImpl
    private val templateRepository: TemplateRepository
    val error: MutableLiveData<ResultWrapper.Error>
        get() = `error$delegate`
    val markerModelListData: MutableLiveData<List<MarkerModel>> = MutableLiveData()

    init {
        baseCloudRepository = baseCloudRepository2
        templateRepository = templateRepository2
        imageRepository = imageRepository2
        markerRepository = markerRepository2
        markerSyncRepository = markerSyncRepository2
        projectsRepository = projectsRepository2
        prefsImpl = prefsImpl2
    }

    fun saveProject(projectModel: ProjectModel){
        val newLocalProjectId = projectsRepository.addProject(projectModel)
        Log.d("MapViewModel", "MapViewModel newLocalProjectId $newLocalProjectId")
    }

    fun getMarkerList(projectIds: List<String?>) {
        launchIO {
            if (prefsImpl.offline) {
                getMarkerEntityList(projectIds.first())
                return@launchIO
            }

            val pageIndex = page++
            when (val result = baseCloudRepository.getMarkerList(pageIndex, projectIds)) {
                is ResultWrapper.Error -> {
                    if(result.status == ErrorStatus.NO_CONNECTION){
                        getMarkerEntityList(projectIds.first())
                    }else {
                        error.postValue(result)
                    }
                }
                is ResultWrapper.Success -> {
                    val markers = result.value
                    if (markers.isNotEmpty()) {
                        markerListAll.addAll(markers)
                        val syncedList = markerListAll.map { it.toSync(it.isNotSynced) }
                        println("terraw MAP syncedList ${syncedList.size} ")
                        markerSyncRepository.addWithReplace(syncedList)

                        val localMarkers = markerSyncRepository.findByProjectId(projectIds.first())
                        val finishedList = localMarkers.map { it.toModel() }

                        markerModelListData.postValue(finishedList)
                    } else {
                        insertMarkerEntityList(projectIds.first(), markerListAll)
                        getMarkerEntityList(projectIds.first())
                    }
                }
            }
        }
    }

    fun getTemplateList(ids: List<String?>?) {
        launchIO {
            if (!prefsImpl.offline) {
                val result = baseCloudRepository.getTemplateList(ids)
                when (result) {
                    is ResultWrapper.Error ->
                        if (result.status == ErrorStatus.NO_CONNECTION) {
                            val localTemplates = templateRepository.findById(ids?.first())
                        } else {
                            error.postValue(result)
                        }
                    is ResultWrapper.Success -> {
                        val templates = result.value?.filterNotNull() // Remove any nulls if present
                        if (templates?.isNotEmpty() == true) {
                            insertTemplateEntityList(templates)
                        }
                    }
                }
            }
        }
    }

    fun synchronizeMarkers(projectId: String?) {
        requireNotNull(projectId) { "projectId cannot be null" }

        launchIO {
            // First, save the marker list for the given project
            saveMarkerList(projectId)
            // Then retrieve the synchronized marker entity list
            getMarkerEntityList(projectId)
        }
    }

    suspend fun saveMarkerList(projectId: String) {
        val markerSyncList = markerSyncRepository.findByProjectId(projectId)
        val unsyncedMarkers = markerSyncList.filter { it.isNotSynced }
        println("terraw MAP saveMarkerList only unsyced markers ${unsyncedMarkers.size} ")

        for (markerSync in unsyncedMarkers) {
            val markerModel = markerSync.toModel()
            saveMarker(markerModel)
        }
    }

    suspend fun saveMarker(markerModel: MarkerModel) {
        if (!prefsImpl.offline) {
            val markerNullable = markerModel.toNullable()

            // Set ID to null if status is NEW
            if (markerNullable.status == Constants.MarkerStatus.NEW) {
                markerNullable.id = null
            }

            // Generate a random marker ID if none is present
            if (markerNullable.markerId.isNullOrEmpty()) {
                markerNullable.markerId = UUID.randomUUID().toString()
            }

            when (val result = baseCloudRepository.saveMarker(markerNullable)) {

                is ResultWrapper.Error -> {
                    Log.e(TAG, "saveMarker $result")
                    //error.postValue(result)
                }

                is ResultWrapper.Success -> {
                    deleteMarkerSyncById(markerModel.id)

                    // Save the updated marker data
                    val savedMarker = result.value
                    if (savedMarker != null) {
                        insertMarkerEntity(savedMarker)
                    }

                    // Handle associated images
                    imageRepository.getByLocalIdParent(
                        markerModel.idLocal ?: error("ID Local is null")
                    ).forEach { imageData ->
                        imageData.file?.let { saveImage(it, savedMarker?.id.toString()) }
                    }

                    markerSyncRepository.addWithReplace(markerModel.toSync(false))
                }
            }
        }
    }

    fun getMarkerEntityList(str: String?) {
        viewModelScope.launch {
            val markerModels = mutableListOf<MarkerModel>()

            // Get synchronized markers
            val markerSyncList = markerSyncRepository.findByProjectId(str)
            markerSyncList.forEach { item ->
                println("terra markerSyncList item ${item.id}")
            }
            // Get markers from main repository and filter out those already synced
            val unsyncedMarkers = markerRepository.findByProjectId(str).filter { marker ->
                println("terra network marker item ${marker.id}")
                markerSyncList.none { syncMarker -> syncMarker.id == marker.id }
            }

            // Add unique unsynced markers
            markerModels.addAll(unsyncedMarkers)

            println("terra markerModels ${markerModels.size}")

            // Convert synced markers to MarkerModel and add them
            markerModels.addAll(markerSyncList.map { it.toModel() })

            // Post the result to LiveData
            markerModelListData.postValue(markerModels)
        }
    }

    suspend fun saveImage(file: File, parentId: String) {
        // Compress the image file
        val compressedImageFile = Compressor.compress(App.instance, file)

        // Prepare the multipart body for the request
        val requestFile =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), compressedImageFile)
        val body = MultipartBody.Part.createFormData("data", compressedImageFile.name, requestFile)

        // Save the image using the BaseCloudRepository
        when (val result = baseCloudRepository.saveImage(parentId, body)) {
            is ResultWrapper.Error -> {
                Log.e(TAG, ">>>saveImage $result")
                error.postValue(result)
            }
            is ResultWrapper.Success -> Unit // Handle success if needed
        }
    }

    private fun deleteMarkerSyncById(str: String) {
        markerSyncRepository.deleteById(str)
    }

    /* access modifiers changed from: private */
    fun insertTemplateEntityList(list: List<TemplateModel?>?) {
        templateRepository.addWithReplace(list)
    }

    /* access modifiers changed from: private */
    private suspend fun insertMarkerEntityList(str: String?, list: List<MarkerModel?>?) {
        markerRepository.deleteByProjectId(str)
        Log.d("MMarker", "markers insertMarkerEntityList ${list?.size}")
        markerRepository.addWithReplace(list)
    }

    private fun insertMarkerEntity(markerModel: MarkerModel) {
        markerRepository.addWithReplace(markerModel)
    }

    fun checkId(str: String?): Boolean {
        val idRegex = Regex("(?<=#:)(.*)")
        if (str != null) {
            val id = idRegex.find(str)?.value?.takeIf {
                Regex("[a-zA-Z0-9]+").containsMatchIn(it)
            }
            if (id != null) {
                return true
            }
        }

        return false
    }

    val markerSize: Float
        get() = prefsImpl.markerSize

    fun getMarkerModelFromByteStr(str: String?, projectId: String, location: Location?): MarkerModel? {
        if (str.isNullOrEmpty() || location == null) return null

        val idRegex = Regex("(?<=#:)(.*)")
        val modelRegex = Regex("(?<=T:)(.*)")

        val id = idRegex.find(str)?.value?.takeIf {
            Regex("[a-zA-Z0-9]+").containsMatchIn(it)
        } ?: UUID.randomUUID().toString()

        val model = modelRegex.find(str)?.value ?: "1425-XR/iD Gas RFiD Ball"

        val uuid = UUID.randomUUID().toString()

        return MarkerModel(
            projectIds = arrayListOf(projectId),
            idLocal = id,
            id = id,
            markerModel = model,
            location = listOf(location.latitude, location.longitude),
            markerId = id,
            generalId = id,
            status = Constants.MarkerStatus.NEW,
        )
    }
}
