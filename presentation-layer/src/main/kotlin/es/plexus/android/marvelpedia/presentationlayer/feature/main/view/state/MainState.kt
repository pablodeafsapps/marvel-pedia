package es.plexus.android.marvelpedia.presentationlayer.feature.main.view.state

import es.plexus.android.marvelpedia.presentationlayer.base.BaseState
import es.plexus.android.marvelpedia.presentationlayer.domain.CharacterVo
import es.plexus.android.marvelpedia.presentationlayer.domain.FailureVo

/**
 * Models all available states for the 'Main' feature view
 */
sealed class MainState : BaseState() {
    class ShowCharacterList(val characterList: List<CharacterVo>) : MainState()
    class ShowCharacterDetail(val character: CharacterVo) : MainState()
    class ShowError(val failure: FailureVo?) : MainState()
}
