package es.plexus.android.marvelpedia.datalayer.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import es.plexus.android.marvelpedia.domainlayer.domain.ErrorMessage
import okhttp3.ResponseBody

/**
 * This data class models a wrapper over a [CharacterDataDto] datum
 */
@JsonClass(generateAdapter = true)
data class CharacterDataDtoWrapper(
    @Json(name = "etag") val etag: String?,
    @Json(name = "data") val data: CharacterDataDto?
)

/**
 *
 */
@JsonClass(generateAdapter = true)
data class CharacterDataDto(
    @Json(name = "count") val count: Int?,
    @Json(name = "limit") val limit: Int?,
    @Json(name = "offset") val offset: Int?,
    @Json(name = "results") val results: List<CharacterDto>?,
    @Json(name = "total") val total: Int?
)

/**
 *
 */
@JsonClass(generateAdapter = true)
data class CharacterDto(
    @Json(name = "comics") val comics: ComicsDto?,
    @Json(name = "description") val description: String?,
    @Json(name = "events") val eventsDto: EventsDto?,
    @Json(name = "id") val id: Int?,
    @Json(name = "modified") val modified: String?,
    @Json(name = "name") val name: String?,
    @Json(name = "resourceURI") val resourceUri: String?,
    @Json(name = "series") val series: Series?,
    @Json(name = "stories") val stories: StoriesDto?,
    @Json(name = "thumbnail") val thumbnail: ThumbnailDto?,
    @Json(name = "urls") val urls: List<UrlDto>?
)

/**
 *
 */
@JsonClass(generateAdapter = true)
data class ComicsDto(
    @Json(name = "available") val available: Int?,
    @Json(name = "collectionURI") val collectionUri: String?,
    @Json(name = "items") val items: List<ItemDto>?,
    @Json(name = "returned") val returned: Int?
)

/**
 *
 */
@JsonClass(generateAdapter = true)
data class EventsDto(
    @Json(name = "available") val available: Int?,
    @Json(name = "collectionURI") val collectionUri: String?,
    @Json(name = "items") val items: List<ItemXDto>?,
    @Json(name = "returned") val returned: Int?
)

/**
 *
 */
@JsonClass(generateAdapter = true)
data class Series(
    @Json(name = "available") val available: Int?,
    @Json(name = "collectionURI") val collectionUri: String?,
    @Json(name = "items") val items: List<ItemXXDto>?,
    @Json(name = "returned") val returned: Int?
)

/**
 *
 */
@JsonClass(generateAdapter = true)
data class StoriesDto(
    @Json(name = "available") val available: Int?,
    @Json(name = "collectionURI") val collectionUri: String?,
    @Json(name = "items") val items: List<ItemXXXDto>?,
    @Json(name = "returned") val returned: Int?
)

/**
 *
 */
@JsonClass(generateAdapter = true)
data class ThumbnailDto(
    @Json(name = "extension") val extension: String?,
    @Json(name = "path") val path: String?
)

/**
 *
 */
@JsonClass(generateAdapter = true)
data class UrlDto(
    @Json(name = "type") val type: String?,
    @Json(name = "url") val url: String?
)

/**
 *
 */
@JsonClass(generateAdapter = true)
data class ItemDto(
    @Json(name = "name") val name: String?,
    @Json(name = "resourceURI") val resourceUri: String?
)

/**
 *
 */
@JsonClass(generateAdapter = true)
data class ItemXDto(
    @Json(name = "name") val name: String?,
    @Json(name = "resourceURI") val resourceUri: String?
)

/**
 *
 */
@JsonClass(generateAdapter = true)
data class ItemXXDto(
    @Json(name = "name") val name: String?,
    @Json(name = "resourceURI") val resourceUri: String?
)

/**
 *
 */
@JsonClass(generateAdapter = true)
data class ItemXXXDto(
    @Json(name = "name") val name: String?,
    @Json(name = "resourceURI") val resourceUri: String?,
    @Json(name = "type") val type: String?
)

/**
 * A class which models any failure coming from the 'data-layer'
 */
sealed class FailureDto(val msg: String?) {

    companion object {
        private const val DEFAULT_ERROR_CODE = -1
    }

    object NoConnection : FailureDto(msg = ErrorMessage.ERROR_NO_CONNECTION)
    class RequestError(
        val code: Int = DEFAULT_ERROR_CODE,
        msg: String?,
        val errorBody: ResponseBody? = null
    ) : FailureDto(msg = msg)

    object NoData : FailureDto(msg = ErrorMessage.ERROR_NO_DATA)
    object Unknown : FailureDto(msg = ErrorMessage.ERROR_UNKNOWN)
    class Error(msg: String?) : FailureDto(msg = msg)
}
