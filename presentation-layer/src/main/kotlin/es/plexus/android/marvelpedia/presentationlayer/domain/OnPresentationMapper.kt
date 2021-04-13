package es.plexus.android.marvelpedia.presentationlayer.domain

import es.plexus.android.marvelpedia.domainlayer.domain.CharacterBo
import es.plexus.android.marvelpedia.domainlayer.domain.CharacterDataBo
import es.plexus.android.marvelpedia.domainlayer.domain.CharacterDataBoWrapper
import es.plexus.android.marvelpedia.domainlayer.domain.FailureBo
import es.plexus.android.marvelpedia.domainlayer.domain.ThumbnailBo

/**
 * Extension function which maps a list of joke Business Objects to a list of joke Visual Objects
 *
 * @return the list of [JokeVo] type equivalent data
 */
fun CharacterDataBoWrapper.toVo() = CharacterDataVoWrapper(
    etag = etag,
    data = data.toVo(),
)

/**
 *
 */
fun CharacterDataBo.toVo() = CharacterDataVo(
    count = count,
    limit = limit,
    offset = offset,
    results = results.toVoList(),
    total = total
)

/**
 *
 */
fun List<CharacterBo>.toVoList(): List<CharacterVo> = map { it.toVo() }

/**
 *
 */
fun CharacterBo.toVo() = CharacterVo(
    id = id,
    name = name,
    description = description,
    resourceUri = resourceUri,
    thumbnail = thumbnail.toVo()
)

private fun ThumbnailBo.toVo() = ThumbnailVo(
    extension = extension,
    path = path
)

/**
 *
 */
 fun CharacterVo?.toBo() = this?.let {
    CharacterBo(
        id = id,
        name = name,
        description = description,
        resourceUri = resourceUri,
        thumbnail = thumbnail.toBo()
    )
}

private fun ThumbnailVo.toBo() = ThumbnailBo(
    extension = extension,
    path = path
)

/**
 * Extension function which maps a failure Business Object to a failure Visual Object
 *
 * @return the [FailureVo] type equivalent datum
 */
fun FailureBo.boToVoFailure(): FailureVo =
    when (this) {
        FailureBo.NoConnection -> FailureVo.NoConnection
        is FailureBo.InputParamsError -> FailureVo.Error(msg = msg)
        is FailureBo.RequestError -> FailureVo.Error(msg = msg)
        is FailureBo.ServerError -> FailureVo.Error(msg = msg)
        FailureBo.NoData -> FailureVo.NoData
        FailureBo.Unknown -> FailureVo.Unknown
        is FailureBo.Error -> FailureVo.Error(msg = msg)
        else -> FailureVo.Unknown
    }
