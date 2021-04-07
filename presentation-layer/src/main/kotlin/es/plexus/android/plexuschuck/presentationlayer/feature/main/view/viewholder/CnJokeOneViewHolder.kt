package es.plexus.android.plexuschuck.presentationlayer.feature.main.view.viewholder

import android.view.View
import android.widget.TextView
import androidx.core.text.HtmlCompat
import es.plexus.android.plexuschuck.presentationlayer.base.BaseViewHolder
import es.plexus.android.plexuschuck.presentationlayer.domain.JokeVo
import es.plexus.android.plexuschuck.presentationlayer.feature.main.view.adapter.CnJokeActionView
import es.plexus.android.plexuschuck.presentationlayer.feature.main.view.adapter.CnJokeView
import kotlinx.android.synthetic.main.joke_item_one.view.*

private const val EMPTY_STRING = ""

/**
 * Holds the widgets composing the view which depicts joke data (type 1)
 *
 * It extends parametrized [BaseViewHolder] which constraints the data types handled.
 */
class CnJokeOneViewHolder(itemView: View) : BaseViewHolder<CnJokeView, CnJokeActionView>(itemView) {

    private val container by lazy { itemView }
    private val tvJoke: TextView? by lazy { itemView.joke_item_one__tv__joke }
    private val tvCategories: TextView? by lazy { itemView.joke_item_one__tv__categories }

    override fun onBind(item: CnJokeView, callback: (CnJokeActionView) -> Unit) {
        (item as? JokeVo)?.let { jokeVo ->
            tvJoke?.text = HtmlCompat.fromHtml(jokeVo.joke ?: EMPTY_STRING, HtmlCompat.FROM_HTML_MODE_COMPACT)
            tvCategories?.text = jokeVo.categories.takeIf { it?.isNotEmpty() == true }?.toString()
            container.setOnClickListener { callback(CnJokeActionView.JokeItemTapped(item = item)) }
        }
    }

}
