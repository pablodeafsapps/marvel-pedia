package es.plexus.android.plexuschuck.datalayer.datasource

import android.content.Context
import es.plexus.android.plexuschuck.datalayer.utils.isNetworkAvailable

/**
 * This interface represents the contract to be complied by an entity to fit in as the connectivity
 * state provider
 */
interface ConnectivityDataSource {

    companion object {
        const val CONNECTIVITY_DATA_SOURCE_TAG = "connectivityDataSource"
    }

    /**
     * Returns the current state of the connection availability
     */
    fun checkNetworkConnectionAvailability(): Boolean

}

/**
 * This class complies with [ConnectivityDataSource] so that it is in charge of providing any
 * required information regarding connectivity
 */
class AndroidDataSource(private val context: Context) : ConnectivityDataSource {

    override fun checkNetworkConnectionAvailability(): Boolean =
        context.isNetworkAvailable()

}
