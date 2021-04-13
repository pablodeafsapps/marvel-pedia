package es.plexus.android.marvelpedia.presentationlayer.utils

import es.plexus.android.marvelpedia.domainlayer.domain.CharacterBo
import es.plexus.android.marvelpedia.domainlayer.domain.CharacterDataBo
import es.plexus.android.marvelpedia.domainlayer.domain.CharacterDataBoWrapper
import es.plexus.android.marvelpedia.domainlayer.domain.ThumbnailBo

private const val DEFAULT_STRING_VALUE = ""
private const val DEFAULT_INTEGER_VALUE = 0

fun getDummyCharacterBoWrapper() = CharacterDataBoWrapper(
    etag = DEFAULT_STRING_VALUE,
    data = getDummyCharacterDataBo()
)

private fun getDummyCharacterDataBo() = CharacterDataBo(
    count = DEFAULT_INTEGER_VALUE,
    limit = DEFAULT_INTEGER_VALUE,
    offset = DEFAULT_INTEGER_VALUE,
    results = listOf(getDummyCharacterBo()),
    total = DEFAULT_INTEGER_VALUE
)

fun getDummyCharacterBo() = CharacterBo(
    id = DEFAULT_INTEGER_VALUE,
    name = DEFAULT_STRING_VALUE,
    description = DEFAULT_STRING_VALUE,
    resourceUri = DEFAULT_STRING_VALUE,
    thumbnail = getDummyThumbnailBo()
)

private fun getDummyThumbnailBo() = ThumbnailBo(
    extension = DEFAULT_STRING_VALUE,
    path = DEFAULT_STRING_VALUE
)
