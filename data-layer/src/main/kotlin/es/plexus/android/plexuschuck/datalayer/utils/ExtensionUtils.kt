package es.plexus.android.plexuschuck.datalayer.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import es.plexus.android.plexuschuck.datalayer.domain.FailureDto
import es.plexus.android.plexuschuck.datalayer.domain.dtoToBoFailure
import es.plexus.android.plexuschuck.domainlayer.domain.ErrorMessage
import es.plexus.android.plexuschuck.domainlayer.domain.FailureBo
import retrofit2.Response
import java.io.IOException

/**
 * This extension function allows any entity hosting a [Context] to easily check whether there is a
 * valid data connection. It also takes into account the Android version available.
 *
 * @author Pablo L. Sordo Martínez
 * @since 1.0
 */
fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.getNetworkCapabilities(
            connectivityManager.activeNetwork
        )
        when {
            networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> true
            networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> true
            // for other devices which are able to connect with Ethernet
            networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true -> true
            // for VPN connections
            networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) == true -> true
            else -> false
        }
    } else {
        connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo?.isConnected == true
    }
}

/**
 * This function provides a proceeding to handle with 'Retrofit' request and [Response] objects, so
 * that a parametrized 'Either' type is returned.
 *
 * @param requestParameter a parameter to be used by the request
 * @param retrofitRequest the retrofit request from a service
 * @param transform function to map response object into a domain object
 * @param errorHandler function to handle errors
 * @param exceptionHandler function to handle exceptions
 *
 * @author Pablo L. Sordo Martínez and Jose Félix Castillo Moya
 * @since 2.0
 */
internal suspend fun <U, S : Response<T>, T, R> retrofitSafeCall(
    requestParameter: U?,
    retrofitRequest: suspend (U?) -> S,
    transform: (T) -> R,
    errorHandler: (Response<T>).() -> Either<FailureBo, Nothing> = { handleDataSourceError() },
    exceptionHandler: (Exception).() -> Either<FailureBo, Nothing> = { handleDataSourceException() }
): Either<FailureBo, R> =
    try {
        val response = retrofitRequest(requestParameter)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                transform(body).right()
            } else {
                errorHandler(response)
            }
        } else {
            errorHandler(response)
        }
    } catch (exception: Exception) {
        exceptionHandler(exception)
    }

/**
 * This function provides a proceeding to handle with 'Retrofit' request and [Response] objects, so
 * that a parametrized 'Either' type is returned. This is a non-parametric version of the above.
 *
 * @param retrofitRequest the retrofit request from a service
 * @param transform function to map response object into a domain object
 * @param errorHandler function to handle errors
 * @param exceptionHandler function to handle exceptions
 *
 * @author Pablo L. Sordo Martínez and Jose Félix Castillo Moya
 * @since 2.0
 */
internal suspend fun <S : Response<T>, T, R> retrofitSafeCall(
    retrofitRequest: suspend () -> S,
    transform: (T) -> R,
    errorHandler: (Response<T>).() -> Either<FailureBo, Nothing> = { handleDataSourceError() },
    exceptionHandler: (Exception).() -> Either<FailureBo, Nothing> = { handleDataSourceException() }
): Either<FailureBo, R> =
    try {
        val response = retrofitRequest()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                transform(body).right()
            } else {
                errorHandler(response)
            }
        } else {
            errorHandler(response)
        }
    } catch (exception: Exception) {
        exceptionHandler(exception)
    }

/**
 * This extension function provides a mechanism to handle HTTP errors coming from a Retrofit
 * [Response]. It returns an [Either] which models the [FailureBo] to be transmitted beyond the
 * domain layer.
 *
 * @author Pablo L. Sordo Martínez
 * @since 1.0
 */
fun <T> Response<T>?.handleDataSourceError(): Either<FailureBo, Nothing> =
    when (this?.code()) {
        in 400..499 -> FailureDto.RequestError(code = 400, msg = ErrorMessage.ERROR_BAD_REQUEST)
        in 500..599 -> FailureDto.RequestError(code = 500, msg = ErrorMessage.ERROR_SERVER)
        else -> FailureDto.Unknown
    }.dtoToBoFailure().left()

/**
 * This extension function provides a mechanism to handle Exceptions coming from a Retrofit
 * request or [Response]. It returns an [Either] which models the [FailureBo] to be transmitted beyond the
 * domain layer.
 *
 * @author Jose Félix Castillo Moya
 * @since 2.0
 */
fun Exception.handleDataSourceException(): Either<FailureBo, Nothing> =
    when (this) {
        is NoConnectivityException -> FailureBo.NoConnection.left()
        is IOException -> FailureBo.ServerError(message ?: "").left()
        else -> {
            Log.e("t", "Error: $message")
            FailureBo.Unknown.left()
        }
    }
