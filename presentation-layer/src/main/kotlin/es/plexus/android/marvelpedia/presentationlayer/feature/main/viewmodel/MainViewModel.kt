package es.plexus.android.marvelpedia.presentationlayer.feature.main.viewmodel

import androidx.lifecycle.viewModelScope
import es.plexus.android.marvelpedia.domainlayer.domain.CharacterDataBoWrapper
import es.plexus.android.marvelpedia.domainlayer.domain.FailureBo
import es.plexus.android.marvelpedia.domainlayer.feature.main.MainDomainLayerBridge
import es.plexus.android.marvelpedia.presentationlayer.base.BaseMvvmViewModel
import es.plexus.android.marvelpedia.presentationlayer.base.ScreenState
import es.plexus.android.marvelpedia.presentationlayer.domain.CharacterVo
import es.plexus.android.marvelpedia.presentationlayer.domain.boToVoFailure
import es.plexus.android.marvelpedia.presentationlayer.domain.toVoList
import es.plexus.android.marvelpedia.presentationlayer.feature.main.view.state.MainState
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * This [BaseMvvmViewModel] handles the 'main' feature view-model. Therefore, it is in charge of
 * the logic which allows a user to fetch joke data and access a joke detail information. It uses a
 * [MainDomainLayerBridge] which gathers all the operations available for this entity.
 *
 * All results update an observable variable, [_screenState], with [MainState] values.
 */
@ExperimentalCoroutinesApi
class MainViewModel(bridge: MainDomainLayerBridge<CharacterDataBoWrapper>) :
    BaseMvvmViewModel<MainDomainLayerBridge<CharacterDataBoWrapper>, MainState>(bridge = bridge) {

    /**
     * Indicates that the associated view has been created
     */
    fun onViewCreated() {
        _screenState.value = ScreenState.Loading
        bridge.fetchJokes(
            scope = viewModelScope,
            onResult = {
                it.fold(::handleError, ::handleSuccess)
            }
        )
    }

    /**
     * Indicates that a joke item of the associated view has been selected
     */
    fun onCharacterItemSelected(item: CharacterVo) {
        _screenState.value = ScreenState.Render(MainState.ShowCharacterDetail(character = item))
    }

    private fun handleSuccess(dataWrapper: CharacterDataBoWrapper) {
        _screenState.value =
            ScreenState.Render(MainState.ShowCharacterList(characterList = dataWrapper.data.results.toVoList()))
    }

    private fun handleError(failureBo: FailureBo) {
        _screenState.value =
            ScreenState.Render(MainState.ShowError(failure = failureBo.boToVoFailure()))
    }

}
