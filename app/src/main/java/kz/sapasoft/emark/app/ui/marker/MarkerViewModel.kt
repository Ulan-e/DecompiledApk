package kz.sapasoft.emark.app.ui.marker

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kz.sapasoft.emark.app.core.BaseViewModel
import kz.sapasoft.emark.app.data.cloud.ResultWrapper
import kz.sapasoft.emark.app.data.cloud.repository.BaseCloudRepository
import kz.sapasoft.emark.app.data.local.prefs.PrefsImpl
import kz.sapasoft.emark.app.data.local.room.image.ImageRepository
import kz.sapasoft.emark.app.data.local.room.marker_sync.MarkerSyncRepository
import kz.sapasoft.emark.app.data.local.room.tag.TagRepository
import kz.sapasoft.emark.app.data.local.room.template.TemplateRepository
import kz.sapasoft.emark.app.domain.model.ImageDataModel
import kz.sapasoft.emark.app.domain.model.MarkerModel
import kz.sapasoft.emark.app.domain.model.TagModel
import kz.sapasoft.emark.app.domain.model.TemplateModel
import kz.sapasoft.emark.app.utils.Constants
import javax.inject.Inject
import kotlin.coroutines.Continuation
import kotlin.jvm.internal.Intrinsics

class MarkerViewModel @Inject constructor(
    baseCloudRepository2: BaseCloudRepository,
    prefsImpl2: PrefsImpl,
    templateRepository2: TemplateRepository,
    imageRepository2: ImageRepository,
    markerSyncRepository2: MarkerSyncRepository,
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
}
