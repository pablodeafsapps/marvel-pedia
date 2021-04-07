package es.plexus.android.plexuschuck.presentationlayer.feature.main.view.adapter

import es.plexus.android.plexuschuck.presentationlayer.domain.JokeVo

/**
 * A sealed class which models all actions available for a joke view
 */
sealed class CnJokeActionView {
    data class JokeItemTapped(val item: JokeVo) : CnJokeActionView()
    object JokeItemLongSelected : CnJokeActionView()
}
