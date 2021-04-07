package es.plexus.android.plexuschuck.datalayer.utils

import es.plexus.android.plexuschuck.datalayer.datasource.ConnectivityDataSource
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * This interceptor is used for check network and throws a custom exception is needed
 */
class ConnectivityInterceptor(private val connectivityDataSource: ConnectivityDataSource) :
    Interceptor {

    companion object {
        const val CONNECTIVITY_INTERCEPTOR_TAG = "connectivityInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!connectivityDataSource.checkNetworkConnectionAvailability()) {
            throw NoConnectivityException()
        }
        return chain.proceed(chain.request())
    }
}

/**
 * Custom [IOException] for no connectivity
 */
class NoConnectivityException : IOException()
