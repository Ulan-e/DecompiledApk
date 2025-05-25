package kz.sapasoft.emark.app.data.cloud.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.IO
import kz.sapasoft.emark.app.data.cloud.ResultWrapper
import kz.sapasoft.emark.app.data.cloud.rest.ApiService
import kz.sapasoft.emark.app.domain.model.ImageDataModel
import kz.sapasoft.emark.app.domain.model.MarkerModel
import kz.sapasoft.emark.app.domain.model.MarkerModelNullable
import kz.sapasoft.emark.app.domain.model.ProjectModel
import kz.sapasoft.emark.app.domain.model.TagModel
import kz.sapasoft.emark.app.domain.model.TemplateModel
import kz.sapasoft.emark.app.domain.model.request.GetMarkersRequest
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import safeApiCall
import java.util.UUID

class CloudRepository(
    apiService: ApiService,
    coroutineDispatcher: CoroutineDispatcher
) : BaseCloudRepository {

    val apIs: ApiService
    private val dispatcher: CoroutineDispatcher

    init {
        apIs = apiService
        dispatcher = coroutineDispatcher
    }

    constructor(
        apiService: ApiService,
        coroutineDispatcher: CoroutineDispatcher?,
        i: Int,
    ) : this(apiService, (if (i and 2 != 0) IO else coroutineDispatcher)!!)

    override suspend fun getImage(queryKey: String): ResultWrapper<MarkerModel?> {
        return safeApiCall(dispatcher) {
            apIs.getImage(true, 600, 600, queryKey)
        }
    }

    override suspend fun getImageData(parentId: String?): ResultWrapper<List<ImageDataModel>?> {
        return safeApiCall(dispatcher) {
            val response = apIs.getImageData("VIEW_DataEntity", "MarkerEntity", parentId)
            response
        }
    }

    override suspend fun getMarker(str: String?): ResultWrapper<MarkerModel?> {
        return safeApiCall(dispatcher) {
            val response = apIs.getMarker(str)
            response
        }
    }

    @SuppressLint("RestrictedApi")
    override suspend fun getMarkerList(
        i: Int,
        list: List<String?>
    ): ResultWrapper<List<MarkerModel>> {
        return safeApiCall(dispatcher) {
            apIs.getMarkerList(
                GetMarkersRequest(
                    offset = (i - 1) * RoomDatabase.MAX_BIND_PARAMETER_CNT,
                    limit = RoomDatabase.MAX_BIND_PARAMETER_CNT,
                    privilege = "VIEW_MarkerEntity",
                    projectIds = list,
                    lineIds = emptyList()
                )
            )
        }
    }

    override suspend fun getProjectList(i: Int): ResultWrapper<List<ProjectModel>?> {
        return safeApiCall(dispatcher) {
            apIs.getProjectList(
                namePart = "*",
                offset = (i - 1) * 0,
                limit = 999,
                light = false,
                calculatePermissions = true
            )
        }
    }

    @SuppressLint("RestrictedApi")
    override suspend fun getTagList(i: Int): ResultWrapper<List<TagModel>?> {
        return safeApiCall(dispatcher) {
            val offset = (i - 1) * RoomDatabase.MAX_BIND_PARAMETER_CNT
            apIs.getTagList(
                namePart = "*",
                offset = offset,
                limit = RoomDatabase.MAX_BIND_PARAMETER_CNT
            )
        }
    }

    override suspend fun getTemplateList(list: List<String?>?): ResultWrapper<List<TemplateModel?>?> {
        return safeApiCall(dispatcher) {
            apIs.getTemplateList(list)
        }
    }

    override suspend fun login(
        url: String,
        username: String,
        password: String
    ): ResultWrapper<ResponseBody> {
        return safeApiCall(dispatcher) {
            apIs.login(
                url = url,
                username = username,
                password = password
            )
        }
    }

    override suspend fun saveImage(
        str: String?,
        part: MultipartBody.Part?
    ): ResultWrapper<MarkerModel?> {
        return safeApiCall(dispatcher) {
            val uuid = UUID.randomUUID().toString()
            apIs.saveImage(
                uuid,
                "-",
                "image/jpeg",
                "MarkerEntity",
                str,
                part
            )
        }
    }

    override suspend fun saveMarker(markerModelNullable: MarkerModelNullable?): ResultWrapper<MarkerModel?> {
        return safeApiCall(dispatcher) {
            apIs.saveMarker(markerModelNullable)
        }
    }

}

