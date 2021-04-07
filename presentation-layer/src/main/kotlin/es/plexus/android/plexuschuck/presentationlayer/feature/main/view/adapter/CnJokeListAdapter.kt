package es.plexus.android.plexuschuck.presentationlayer.feature.main.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.plexus.android.plexuschuck.presentationlayer.R
import es.plexus.android.plexuschuck.presentationlayer.base.BaseViewHolder
import es.plexus.android.plexuschuck.presentationlayer.feature.main.view.viewholder.CnJokeOneViewHolder

/**
 * A [RecyclerView.Adapter] which is in charge of handling joke data to properly render them.
 *
 * @property [itemList] A list of [CnJokeView] type which represents each joke
 * @property [onItemSelected] A callback which allows to perform an action over a [CnJokeActionView]
 */
class CnJokeListAdapter(
    private var itemList: List<CnJokeView>,
    private val onItemSelected: (CnJokeActionView) -> Unit
) : RecyclerView.Adapter<BaseViewHolder<CnJokeView, CnJokeActionView>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<CnJokeView, CnJokeActionView> {
        val layoutRes = getLayoutResourceIdByViewType(viewType = viewType)
        return CnJokeOneViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        )
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int = itemList[position].viewType

    override fun onBindViewHolder(
        holder: BaseViewHolder<CnJokeView, CnJokeActionView>,
        position: Int
    ) {
        holder.onBind(
            item = itemList[position],
            callback = { item -> onItemSelected.invoke(item) }
        )
    }

    /**
     * Updates the data displayed by the adapter
     *
     * @param [newData] A list with the new data to update
     */
    fun updateData(newData: List<CnJokeView>) {
        itemList = newData
        notifyDataSetChanged()
    }

    private fun getLayoutResourceIdByViewType(viewType: Int): Int = when (viewType) {
        CnJokeView.JokeViewType.TYPE_ONE.type -> R.layout.joke_item_one
        CnJokeView.JokeViewType.TYPE_TWO.type -> R.layout.joke_item_two
        else -> -1
    }

}
