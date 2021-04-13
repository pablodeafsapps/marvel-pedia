package es.plexus.android.marvelpedia.presentationlayer.domain

import android.os.Parcelable
import es.plexus.android.marvelpedia.domainlayer.domain.ErrorMessage
import kotlinx.android.parcel.Parcelize

/**
 * This data class represents the Visual Object related to a character datum
 */
data class CharacterDataVoWrapper(
    val etag: String,
    val data: CharacterDataVo
)

/**
 *
 */
data class CharacterDataVo(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<CharacterVo>,
    val total: Int
)

/**
 *
 */
@Parcelize
data class CharacterVo(
    val id: Int,
    val name: String,
    val description: String,
    val resourceUri: String,
    val thumbnail: ThumbnailVo,
) : Parcelable

/**
 *
 */
@Parcelize
data class ThumbnailVo(
    val extension: String,
    val path: String
) : Parcelable

/**
 * A class which models any failure coming from the 'domain-layer' module
 */
sealed class FailureVo(var msg: String?) {

    companion object {
        private const val DEFAULT_STRING_RESOURCE = "none"
    }

    /**
     * Allows to get the message associated to an error
     */
    fun getErrorMessage(): String = msg ?: DEFAULT_STRING_RESOURCE

    object NoConnection : FailureVo(msg = ErrorMessage.ERROR_NO_CONNECTION)
    object NoData : FailureVo(msg = ErrorMessage.ERROR_NO_DATA)
    object Unknown : FailureVo(msg = ErrorMessage.ERROR_UNKNOWN)
    class Error(msg: String) : FailureVo(msg = msg)

}
