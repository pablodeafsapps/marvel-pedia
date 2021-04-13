package es.plexus.android.marvelpedia.domainlayer.domain

private const val DEFAULT_STRING_RESOURCE = "none"

/**
 * This data class models a wrapper over a [CharacterDataBoWrapper] datum
 */
data class CharacterDataBoWrapper(
    val etag: String,
    val data: CharacterDataBo
)

/**
 *
 */
data class CharacterDataBo(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<CharacterBo>,
    val total: Int
)

/**
 *
 */
data class CharacterBo(
    val id: Int,
    val name: String,
    val description: String,
    val resourceUri: String,
    val thumbnail: ThumbnailBo,
)

/**
 *
 */
data class ThumbnailBo(
    val extension: String,
    val path: String
)

/**
 * A class which models any failure coming from the 'domain-layer'
 */
sealed class FailureBo(var msg: String = DEFAULT_STRING_RESOURCE) {
    object NoConnection : FailureBo(msg = ErrorMessage.ERROR_NO_CONNECTION)
    class InputParamsError(msg: String) : FailureBo(msg = msg)
    class RequestError(msg: String) : FailureBo(msg = msg)
    class ServerError(msg: String) : FailureBo(msg = msg)
    object NoData : FailureBo(msg = ErrorMessage.ERROR_NO_DATA)
    object Unknown : FailureBo(msg = ErrorMessage.ERROR_UNKNOWN)
    class Error(msg: String) : FailureBo(msg = msg)
}

/**
 * This object gathers all error messages available throughout the app
 */
object ErrorMessage {
    const val ERROR_NO_CONNECTION = "No Connection"
    const val ERROR_PARAMS_CANNOT_BE_EMPTY = "Params cannot be empty"
    const val ERROR_BAD_REQUEST = "Bad Request"
    const val ERROR_SOCKET_TIMEOUT_EXCEPTION = "Socket Timeout"
    const val ERROR_NO_DATA = "No Data"
    const val ERROR_SERVER = "Server Error"
    const val ERROR_UNKNOWN = "Unknown Error"
}
