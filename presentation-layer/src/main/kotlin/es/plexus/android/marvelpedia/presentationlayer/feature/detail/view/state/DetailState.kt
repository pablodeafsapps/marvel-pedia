package es.plexus.android.marvelpedia.presentationlayer.feature.detail.view.state

import es.plexus.android.marvelpedia.presentationlayer.base.BaseState
import es.plexus.android.marvelpedia.presentationlayer.domain.CharacterVo
import es.plexus.android.marvelpedia.presentationlayer.domain.FailureVo

/**
 * Models all available states for the 'Detail' feature view
 */
sealed class DetailState : BaseState() {
    class ShowCharacterInfo(val character: CharacterVo) : DetailState()
    class ShowError(val failure: FailureVo?) : DetailState()
}
