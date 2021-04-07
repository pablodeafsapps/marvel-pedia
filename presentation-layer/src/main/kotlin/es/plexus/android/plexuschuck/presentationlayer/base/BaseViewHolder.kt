package es.plexus.android.plexuschuck.presentationlayer.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * A baseline [RecyclerView.ViewHolder] which defines certain mandatory operations
 *
 * @param [itemView] A [View] item held by the [RecyclerView]
 */
abstract class BaseViewHolder<S, T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    /**
     * Defines a blueprint so that an action can be performed over an item
     *
     * @param [item] The data which is linked to the action
     * @param [callback] A lambda which depicts an action to perform
     */
    abstract fun onBind(item: S, callback: (T) -> Unit = {})

}
