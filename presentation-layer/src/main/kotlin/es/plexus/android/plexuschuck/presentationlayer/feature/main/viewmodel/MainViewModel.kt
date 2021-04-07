package es.plexus.android.plexuschuck.presentationlayer.feature.main.viewmodel

import androidx.lifecycle.viewModelScope
import es.plexus.android.plexuschuck.domainlayer.domain.FailureBo
import es.plexus.android.plexuschuck.domainlayer.domain.JokeBoWrapper
import es.plexus.android.plexuschuck.domainlayer.feature.main.MainDomainLayerBridge
import es.plexus.android.plexuschuck.presentationlayer.base.BaseMvvmViewModel
import es.plexus.android.plexuschuck.presentationlayer.base.ScreenState
import es.plexus.android.plexuschuck.presentationlayer.domain.JokeVo
import es.plexus.android.plexuschuck.presentationlayer.domain.boToVo
import es.plexus.android.plexuschuck.presentationlayer.domain.boToVoFailure
import es.plexus.android.plexuschuck.presentationlayer.feature.main.view.state.MainState
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * This [BaseMvvmViewModel] handles the 'main' feature view-model. Therefore, it is in charge of
 * the logic which allows a user to fetch joke data and access a joke detail information. It uses a
 * [MainDomainLayerBridge] which gathers all the operations available for this entity.
 *
 * All results update an observable variable, [_screenState], with [MainState] values.
 */
@ExperimentalCoroutinesApi
class MainViewModel(bridge: MainDomainLayerBridge<JokeBoWrapper>) :
    BaseMvvmViewModel<MainDomainLayerBridge<JokeBoWrapper>, MainState>(bridge = bridge) {

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
    fun onJokeItemSelected(item: JokeVo) {
        _screenState.value = ScreenState.Render(MainState.ShowJokeDetail(joke = item))
    }

    private fun handleSuccess(wrapper: JokeBoWrapper) {
        _screenState.value = ScreenState.Render(MainState.ShowJokeList(jokeList = wrapper.value.boToVo()))
    }

    private fun handleError(failureBo: FailureBo) {
        _screenState.value = ScreenState.Render(MainState.ShowError(failure = failureBo.boToVoFailure()))
    }

}
