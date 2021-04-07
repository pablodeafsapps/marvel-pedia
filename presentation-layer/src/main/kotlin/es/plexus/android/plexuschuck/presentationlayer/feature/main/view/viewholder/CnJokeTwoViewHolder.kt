package es.plexus.android.plexuschuck.presentationlayer.feature.main.view.viewholder

import android.view.View
import es.plexus.android.plexuschuck.presentationlayer.base.BaseViewHolder
import es.plexus.android.plexuschuck.presentationlayer.feature.main.view.adapter.CnJokeActionView
import es.plexus.android.plexuschuck.presentationlayer.feature.main.view.adapter.CnJokeView

/**
 * Holds the widgets composing the view which depicts joke data (type 2)
 *
 * It extends parametrized [BaseViewHolder] which constraints the data types handled
 */
class CnJokeTwoViewHolder(itemView: View) : BaseViewHolder<CnJokeView, CnJokeActionView>(itemView) {

    override fun onBind(item: CnJokeView, callback: (CnJokeActionView) -> Unit) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

}
