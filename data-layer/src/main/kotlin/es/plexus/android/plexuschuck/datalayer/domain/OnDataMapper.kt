package es.plexus.android.plexuschuck.datalayer.domain

import es.plexus.android.plexuschuck.domainlayer.domain.FailureBo
import es.plexus.android.plexuschuck.domainlayer.domain.JokeBo
import es.plexus.android.plexuschuck.domainlayer.domain.JokeBoWrapper
import es.plexus.android.plexuschuck.domainlayer.domain.UserLoginBo

private const val DEFAULT_INTEGER_VALUE = 0
private const val DEFAULT_STRING_VALUE = ""

/**
 * Maps a [UserLoginBo] into a [UserLoginDto]
 */
fun UserLoginBo.boToDto() = UserLoginDto(
    email = email,
    password = password
)

/**
 * Maps a [JokeDtoWrapper] into a [JokeBoWrapper]
 */
fun JokeDtoWrapper.dtoToBo() = JokeBoWrapper(
    type = type,
    value = value.jokeListDtoToBo()
)

private fun List<JokeDto>.jokeListDtoToBo() = map { it.dtoToBo() }

private fun JokeDto.dtoToBo() = JokeBo(
    id = id ?: DEFAULT_INTEGER_VALUE,
    joke = joke ?: DEFAULT_STRING_VALUE,
    categories = categories ?: emptyList()
)

/**
 * Maps a [FailureDto] into a [FailureBo]
 */
fun FailureDto.dtoToBoFailure(): FailureBo = when (this) {
    FailureDto.NoConnection -> FailureBo.NoConnection
    is FailureDto.RequestError -> FailureBo.RequestError(msg = msg ?: DEFAULT_STRING_VALUE)
    FailureDto.FirebaseLoginError -> FailureBo.ServerError(msg = msg ?: DEFAULT_STRING_VALUE)
    is FailureDto.FirebaseRegisterError -> FailureBo.ServerError(msg = msg ?: DEFAULT_STRING_VALUE)
    is FailureDto.Error -> FailureBo.ServerError(msg = msg ?: DEFAULT_STRING_VALUE)
    FailureDto.NoData -> FailureBo.NoData
    FailureDto.Unknown -> FailureBo.Unknown
}
