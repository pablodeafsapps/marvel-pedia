package es.plexus.android.marvelpedia.presentationlayer.feature.detail.viewmodel

import androidx.lifecycle.viewModelScope
import es.plexus.android.marvelpedia.domainlayer.domain.CharacterBo
import es.plexus.android.marvelpedia.domainlayer.domain.FailureBo
import es.plexus.android.marvelpedia.domainlayer.feature.detail.DetailDomainLayerBridge
import es.plexus.android.marvelpedia.presentationlayer.base.BaseMvvmViewModel
import es.plexus.android.marvelpedia.presentationlayer.base.ScreenState
import es.plexus.android.marvelpedia.presentationlayer.domain.CharacterVo
import es.plexus.android.marvelpedia.presentationlayer.domain.boToVoFailure
import es.plexus.android.marvelpedia.presentationlayer.domain.toBo
import es.plexus.android.marvelpedia.presentationlayer.domain.toVo
import es.plexus.android.marvelpedia.presentationlayer.feature.detail.view.state.DetailState
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * This [BaseMvvmViewModel] handles the 'detail' feature view-model. Therefore, it is in charge of
 * publishing joke detail information to all subscribers.
 *
 * All results update an observable variable, [_screenState], with [DetailState] values.
 */
@ExperimentalCoroutinesApi
class DetailViewModel(
    bridge: DetailDomainLayerBridge<CharacterBo>
) : BaseMvvmViewModel<DetailDomainLayerBridge<CharacterBo>, DetailState>(bridge = bridge) {

    /**
     * Indicates that the associated view has been created
     */
    fun onViewCreated(character: CharacterVo?) {
        _screenState.value = ScreenState.Loading
        bridge.fetchCharacterDetailsById(
            scope = viewModelScope,
            params = character.toBo(),
            onResult = {
                it.fold(::handleError, ::handleSuccess)
            })
    }

    private fun handleSuccess(character: CharacterBo) {
        _screenState.value =
            ScreenState.Render(DetailState.ShowCharacterInfo(character = character.toVo()))
    }

    private fun handleError(failure: FailureBo) {
        _screenState.value =
            ScreenState.Render(DetailState.ShowError(failure = failure.boToVoFailure()))
    }

}
