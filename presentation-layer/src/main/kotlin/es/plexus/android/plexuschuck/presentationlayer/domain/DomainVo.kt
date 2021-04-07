package es.plexus.android.plexuschuck.presentationlayer.domain

import android.os.Parcelable
import es.plexus.android.plexuschuck.domainlayer.domain.ErrorMessage
import es.plexus.android.plexuschuck.presentationlayer.feature.main.view.adapter.CnJokeView
import kotlinx.android.parcel.Parcelize

/**
 * This data class represents the Visual Object related to a user login datum
 */
data class UserLoginVo(val email: String?, val password: String?)

/**
 * This data class represents the Visual Object related to a joke datum
 */
@Parcelize
data class JokeVo(val id: Int?, val joke: String?, val categories: List<String>?) : CnJokeView.JokeTypeOne(), Parcelable

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
