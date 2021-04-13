package es.plexus.android.marvelpedia.presentationlayer.feature.main.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.plexus.android.marvelpedia.presentationlayer.R
import es.plexus.android.marvelpedia.presentationlayer.base.BaseViewHolder
import es.plexus.android.marvelpedia.presentationlayer.domain.CharacterVo
import es.plexus.android.marvelpedia.presentationlayer.feature.main.view.viewholder.CharacterViewHolder

/**
 * A [RecyclerView.Adapter] which is in charge of handling joke data to properly render them.
 *
 * @property [itemList] A list of [CharacterVo] type which represents each joke
 * @property [onItemSelected] A callback which allows to perform an action over a [CharacterVo]
 */
class CharacterListAdapter(
    private var itemList: List<CharacterVo>,
    private val onItemSelected: (CharacterVo) -> Unit
) : RecyclerView.Adapter<BaseViewHolder<CharacterVo>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<CharacterVo> =
        CharacterViewHolder(
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.character_item, parent, false)
        )

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: BaseViewHolder<CharacterVo>, position: Int) {
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
    fun updateData(newData: List<CharacterVo>) {
        itemList = newData
        notifyDataSetChanged()
    }

}
