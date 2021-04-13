package es.plexus.android.marvelpedia.datalayer.domain

import es.plexus.android.marvelpedia.domainlayer.domain.CharacterBo
import es.plexus.android.marvelpedia.domainlayer.domain.CharacterDataBo
import es.plexus.android.marvelpedia.domainlayer.domain.CharacterDataBoWrapper
import es.plexus.android.marvelpedia.domainlayer.domain.FailureBo
import es.plexus.android.marvelpedia.domainlayer.domain.ThumbnailBo

private const val DEFAULT_INTEGER_VALUE = 0
private const val DEFAULT_STRING_VALUE = ""

/**
 * Maps a [CharacterDataDtoWrapper] into a [CharacterDataBoWrapper]
 */
fun CharacterDataDtoWrapper.toBo() = CharacterDataBoWrapper(
    etag = etag ?: DEFAULT_STRING_VALUE,
    data = data?.toBo() ?: getDefaultCharacterDataBo()
)

/**
 *
 */
private fun CharacterDataDto.toBo() = CharacterDataBo(
    count = count ?: DEFAULT_INTEGER_VALUE,
    limit = limit ?: DEFAULT_INTEGER_VALUE,
    offset = offset ?: DEFAULT_INTEGER_VALUE,
    results = results?.toBoList() ?: getDefaultCharacterBoList(),
    total = total ?: DEFAULT_INTEGER_VALUE
)

/**
 *
 */
private fun List<CharacterDto>.toBoList() = map { it.toBo() }

/**
 *
 */
fun CharacterDto.toBo() = CharacterBo(
    description = description ?: DEFAULT_STRING_VALUE,
    id = id ?: DEFAULT_INTEGER_VALUE,
    name = name ?: DEFAULT_STRING_VALUE,
    resourceUri = resourceUri ?: DEFAULT_STRING_VALUE,
    thumbnail = thumbnail?.toBo() ?: getDefaultThumbnailBo(),
)

/**
 *
 */
private fun ThumbnailDto.toBo() = ThumbnailBo(
    extension = extension ?: DEFAULT_STRING_VALUE,
    path = path ?: DEFAULT_STRING_VALUE
)

private fun getDefaultCharacterDataBo() = CharacterDataBo(
    count = DEFAULT_INTEGER_VALUE,
    limit = DEFAULT_INTEGER_VALUE,
    offset = DEFAULT_INTEGER_VALUE,
    results = emptyList(),
    total = DEFAULT_INTEGER_VALUE
)

private fun getDefaultCharacterBoList() = listOf(CharacterBo(
    id = DEFAULT_INTEGER_VALUE,
    name = DEFAULT_STRING_VALUE,
    description = DEFAULT_STRING_VALUE,
    resourceUri = DEFAULT_STRING_VALUE,
    thumbnail = getDefaultThumbnailBo()
))

private fun getDefaultThumbnailBo() = ThumbnailBo(
    extension = DEFAULT_STRING_VALUE,
    path = DEFAULT_STRING_VALUE
)

/**
 * Maps a [FailureDto] into a [FailureBo]
 */
fun FailureDto.dtoToBoFailure(): FailureBo = when (this) {
    FailureDto.NoConnection -> FailureBo.NoConnection
    is FailureDto.RequestError -> FailureBo.RequestError(msg = msg ?: DEFAULT_STRING_VALUE)
    is FailureDto.Error -> FailureBo.ServerError(msg = msg ?: DEFAULT_STRING_VALUE)
    FailureDto.NoData -> FailureBo.NoData
    FailureDto.Unknown -> FailureBo.Unknown
}
