import com.example.decompiledapk.R
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kz.sapasoft.emark.app.core.App
import kz.sapasoft.emark.app.data.cloud.ResultWrapper
import kz.sapasoft.emark.app.domain.model.response.ErrorResponse
import kz.sapasoft.emark.app.domain.model.response.ErrorStatus
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            ResultWrapper.Success(apiCall())
        } catch (throwable: Throwable) {
            when (throwable) {
                is UnknownHostException -> {
                    ResultWrapper.Error(
                        ErrorStatus.NO_CONNECTION,
                        null,
                        App.instance.getString(R.string.no_internet_connection)
                    )
                }
                is HttpException -> {
                    val code = throwable.code()
                    ResultWrapper.Error(
                        ErrorStatus.BAD_RESPONSE,
                        code,
                        convertErrorBody(throwable)
                    )
                }
                is SocketTimeoutException -> {
                    ResultWrapper.Error(
                        ErrorStatus.TIMEOUT,
                        null,
                        App.instance.getString(R.string.socket_timeout_exception)
                    )
                }
                else -> {
                    ResultWrapper.Error(
                        ErrorStatus.NOT_DEFINED,
                        null,
                        throwable.message
                    )
                }
            }
        }
    }
}

fun convertErrorBody(throwable: HttpException): String {
    return try {
        val errorBody = throwable.response()?.errorBody()?.string()
        val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
        val code = errorResponse?.code ?: ""
        val details = errorResponse?.details ?: ""

        if (code.isNotBlank() && details.isNotBlank()) "$code $details"
        else code.ifBlank { details }
    } catch (e: Exception) {
        ""
    }
}
