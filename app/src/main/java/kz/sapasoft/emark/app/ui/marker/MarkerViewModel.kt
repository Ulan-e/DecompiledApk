package kz.sapasoft.emark.app.ui.marker

import android.util.Log
import androidx.lifecycle.MutableLiveData
import id.zelory.compressor.Compressor
import kz.sapasoft.emark.app.core.App
import kz.sapasoft.emark.app.core.BaseViewModel
import kz.sapasoft.emark.app.data.cloud.ResultWrapper
import kz.sapasoft.emark.app.data.cloud.repository.BaseCloudRepository
import kz.sapasoft.emark.app.data.local.prefs.PrefsImpl
import kz.sapasoft.emark.app.data.local.room.image.ImageRepository
import kz.sapasoft.emark.app.data.local.room.marker.MarkerRepository
import kz.sapasoft.emark.app.data.local.room.marker_sync.MarkerSyncRepository
import kz.sapasoft.emark.app.data.local.room.tag.TagRepository
import kz.sapasoft.emark.app.data.local.room.template.TemplateRepository
import kz.sapasoft.emark.app.domain.model.ImageDataModel
import kz.sapasoft.emark.app.domain.model.MarkerModel
import kz.sapasoft.emark.app.domain.model.TagModel
import kz.sapasoft.emark.app.domain.model.TemplateModel
import kz.sapasoft.emark.app.utils.Constants
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.Continuation
import kotlin.jvm.internal.Intrinsics

class MarkerViewModel @Inject constructor(
    baseCloudRepository2: BaseCloudRepository,
    prefsImpl2: PrefsImpl,
    templateRepository2: TemplateRepository,
    imageRepository2: ImageRepository,
    markerSyncRepository2: MarkerSyncRepository,
    markerRepository2: MarkerRepository,
    tagRepository2: TagRepository
) : BaseViewModel() {
    private val TAG = javaClass.simpleName

    /* access modifiers changed from: private */
    @JvmField
    val baseCloudRepository: BaseCloudRepository
    private val `error$delegate`: MutableLiveData<ResultWrapper.Error> by lazy {
        MutableLiveData<ResultWrapper.Error>()
    }

    /* access modifiers changed from: private */
    @JvmField
    val imageRepository: ImageRepository
    private val `imagesData$delegate`: MutableLiveData<List<ImageDataModel>> by lazy {
        MutableLiveData<List<ImageDataModel>>()
    }
    private val `loading$delegate`: MutableLiveData<Boolean?> by lazy {
        MutableLiveData<Boolean?>()
    }
    private val `localImagesData$delegate`: MutableLiveData<List<ImageDataModel>> by lazy {
        MutableLiveData<List<ImageDataModel>>()
    }
    private val `markerChangeTask$delegate`: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    private val `markerModelData$delegate`: MutableLiveData<MarkerModel> by lazy {
        MutableLiveData<MarkerModel>()
    }

    /* access modifiers changed from: private */
    @JvmField
    val markerSyncRepository: MarkerSyncRepository
    private val prefsImpl: PrefsImpl
    private val `saveImageData$delegate`: MutableLiveData<List<ImageDataModel>> by lazy {
        MutableLiveData<List<ImageDataModel>>()
    }
    private val `tagModelData$delegate`: MutableLiveData<List<TagModel>> by lazy {
        MutableLiveData<List<TagModel>>()
    }
    private val tagRepository: TagRepository
    private val `templateModelData$delegate`: MutableLiveData<List<TemplateModel>> by lazy {
        MutableLiveData<List<TemplateModel>>()
    }
    private val templateRepository: TemplateRepository
    val error: MutableLiveData<ResultWrapper.Error> by lazy {
        MutableLiveData<ResultWrapper.Error>()
    }

    val imagesData: MutableLiveData<List<ImageDataModel>> by lazy {
        MutableLiveData<List<ImageDataModel>>()
    }

    val loading: MutableLiveData<Boolean?> by lazy {
        MutableLiveData<Boolean?>()
    }

    val localImagesData: MutableLiveData<List<ImageDataModel>> by lazy {
        MutableLiveData<List<ImageDataModel>>()
    }
    val markerChangeTask: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val markerModelData: MutableLiveData<MarkerModel> by lazy {
        MutableLiveData<MarkerModel>()
    }
    val saveImageData: MutableLiveData<List<Any>> by lazy {
        MutableLiveData<List<Any>>()
    }
    val tagModelData: MutableLiveData<List<TagModel>> by lazy {
        MutableLiveData<List<TagModel>>()
    }
    val templateModelData: MutableLiveData<List<TemplateModel>> by lazy {
        MutableLiveData<List<TemplateModel>>()
    }

    @JvmField
    val markerRepository: MarkerRepository

    init {
        Intrinsics.checkParameterIsNotNull(baseCloudRepository2, "baseCloudRepository")
        Intrinsics.checkParameterIsNotNull(prefsImpl2, "prefsImpl")
        Intrinsics.checkParameterIsNotNull(templateRepository2, "templateRepository")
        Intrinsics.checkParameterIsNotNull(imageRepository2, "imageRepository")
        Intrinsics.checkParameterIsNotNull(markerSyncRepository2, "markerSyncRepository")
        Intrinsics.checkParameterIsNotNull(tagRepository2, "tagRepository")
        baseCloudRepository = baseCloudRepository2
        prefsImpl = prefsImpl2
        templateRepository = templateRepository2
        imageRepository = imageRepository2
        markerSyncRepository = markerSyncRepository2
        tagRepository = tagRepository2
        markerRepository = markerRepository2
    }

    fun getAllData(markerModel: MarkerModel, markerTemplateIds: List<String>) {
        requireNotNull(markerModel) { "markerModel is required" }
        requireNotNull(markerTemplateIds) { "markerTemplateIds is required" }

        loading.postValue(true)

        launchIO {
            tagList
            getTemplateList(markerTemplateIds)

            // Await the result of `getMarker` if it is a suspend function
            getMarker(markerModel)
        }

        loading.postValue(false)
    }

    suspend fun getMarker(markerModel: MarkerModel): Unit {
        if (prefsImpl.offline) {
            markerModelData.postValue(markerModel)
            return
        }

        when (markerModel.status) {
            null -> {
                getImages(markerModel)
                val result = baseCloudRepository.getMarker(markerModel.id)
                when (result) {
                    is ResultWrapper.Error -> error.postValue(result)
                    is ResultWrapper.Success -> markerModelData.postValue(result.value)
                }
            }
            Constants.MarkerStatus.EDITED -> {
                getImages(markerModel)
                markerModelData.postValue(markerModel)
            }
            else -> {
                markerModelData.postValue(markerModel)
            }
        }

        markerModel.idLocal?.let { idLocal ->
            val localImages = imageRepository.getByLocalIdParent(idLocal)
            localImagesData.postValue(localImages)
        }
    }

    /* access modifiers changed from: private */
    fun getTemplateList(list: List<String?>) {
        val arrayList: ArrayList<TemplateModel> = ArrayList<TemplateModel>()
        for (findById in list) {
            arrayList.addAll(templateRepository.findById(findById))
        }
        templateModelData.postValue(arrayList)
    }

    val tagList: Unit
        /* access modifiers changed from: private */
        get() {
            tagModelData.postValue(tagRepository.findAll())
        }

    fun saveMarkerAndImage(markerModel: MarkerModel, imageDataModelList: List<ImageDataModel>) {
        launchIO {
            markerSyncRepository.addWithReplace(markerModel.toSync())

            val idLocal = markerModel.idLocal ?: throw NullPointerException("idLocal is null")

            imageRepository.deleteByLocalIdParent(idLocal)
            imageRepository.addAll(imageDataModelList)

            val syncedMarkers = markerSyncRepository.findByProjectId(markerModel.id)
            val newMarker = syncedMarkers.find { it.idLocal == markerModel.idLocal }

            saveMarker(newMarker?.toModel() ?: throw NullPointerException("newMarker is null"))

            markerChangeTask.postValue(true)
        }
    }

    fun deleteLocal(markerModel: MarkerModel) {
        launchIO {
            markerSyncRepository.deleteById(markerModel.id)

            val idLocal = markerModel.idLocal ?: throw NullPointerException("idLocal is null")
            imageRepository.deleteByLocalIdParent(idLocal)

            markerChangeTask.postValue(true)
        }
    }

    private fun getImages(markerModel: MarkerModel) {
        launchIO {
            val result = baseCloudRepository.getImageData(markerModel.id)

            when (result) {
                is ResultWrapper.Error -> error.postValue(result)
                is ResultWrapper.Success -> imagesData.postValue(result.value)
            }
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
                    error.postValue(result)
                }

                is ResultWrapper.Success -> {
                   // deleteMarkerSyncById(markerModel.id)

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
                }
            }
        }
    }

    private fun insertMarkerEntity(markerModel: MarkerModel) {
        markerRepository.addWithReplace(markerModel)
    }

    private fun deleteMarkerSyncById(str: String) {
        markerSyncRepository.deleteById(str)
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
            is ResultWrapper.Error -> error.postValue(result)
            is ResultWrapper.Success -> Unit // Handle success if needed
        }
    }
}
