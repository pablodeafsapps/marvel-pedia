package es.plexus.android.plexuschuck.presentationlayer.domain

import es.plexus.android.plexuschuck.domainlayer.domain.FailureBo
import es.plexus.android.plexuschuck.domainlayer.domain.JokeBo
import es.plexus.android.plexuschuck.domainlayer.domain.UserLoginBo

private const val DEFAULT_STRING_VALUE = "none"

/**
 * Maps a [UserLoginVo] into a [UserLoginBo]
 */
fun UserLoginVo.voToBo() = UserLoginBo(
    email = email ?: DEFAULT_STRING_VALUE,
    password = password ?: DEFAULT_STRING_VALUE
)

/**
 * Extension function which maps a list of joke Business Objects to a list of joke Visual Objects
 *
 * @return the list of [JokeVo] type equivalent data
 */
fun List<JokeBo>.boToVo(): List<JokeVo> = map { it.boToVo() }

private fun JokeBo.boToVo(): JokeVo =
    JokeVo(id = id, joke = joke, categories = categories)

/**
 * Extension function which maps a failure Business Object to a failure Visual Object
 *
 * @return the [FailureVo] type equivalent datum
 */
fun FailureBo.boToVoFailure(): FailureVo =
    when (this) {
        is FailureBo.InputParamsError -> FailureVo.Error(msg = msg)
        is FailureBo.RequestError -> FailureVo.Error(msg = msg)
        is FailureBo.ServerError -> FailureVo.Error(msg = msg)
        is FailureBo.NoData -> FailureVo.NoData
        is FailureBo.NoConnection -> FailureVo.NoConnection
        is FailureBo.Unknown -> FailureVo.Unknown
        is FailureBo.Error -> FailureVo.Error(msg = msg)
    }
