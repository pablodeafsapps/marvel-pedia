package es.plexus.android.plexuschuck.presentationlayer.feature.main.view.state

import es.plexus.android.plexuschuck.presentationlayer.base.BaseState
import es.plexus.android.plexuschuck.presentationlayer.domain.FailureVo
import es.plexus.android.plexuschuck.presentationlayer.domain.JokeVo

/**
 * Models all available states for the 'Main' feature view
 */
sealed class MainState : BaseState() {
    class ShowJokeList(val jokeList: List<JokeVo>) : MainState()
    class ShowJokeDetail(val joke: JokeVo) : MainState()
    class ShowError(val failure: FailureVo?) : MainState()
}
