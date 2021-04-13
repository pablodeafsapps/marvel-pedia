package es.plexus.android.marvelpedia.presentationlayer.feature.main.view.viewholder

import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.Glide
import es.plexus.android.marvelpedia.presentationlayer.R
import es.plexus.android.marvelpedia.presentationlayer.base.BaseViewHolder
import es.plexus.android.marvelpedia.presentationlayer.domain.CharacterVo

/**
 * Holds the widgets composing the view which depicts character data
 *
 * It extends parametrized [BaseViewHolder] which constraints the data types handled.
 */
class CharacterViewHolder(itemView: View) : BaseViewHolder<CharacterVo>(itemView) {

    private val container by lazy { itemView }
    private val tvCharacter: AppCompatTextView? by lazy {
        itemView.findViewById(R.id.character_item__tv__character)
    }
    private val tvDescription: AppCompatTextView? by lazy {
        itemView.findViewById(R.id.character_item__tv__description)
    }
    private val ivIcon: ImageView? by lazy { itemView.findViewById(R.id.character_item__iv__image) }

    override fun onBind(item: CharacterVo, callback: (CharacterVo) -> Unit) {
        (item as? CharacterVo).let { character ->
            tvCharacter?.text = character?.name
            tvDescription?.text = character?.description
            ivIcon?.let { img ->
                Glide.with(itemView)
                    .load("${item.thumbnail.path}/standard_xlarge.${item.thumbnail.extension}")
                    .centerInside()
                    .placeholder(R.drawable.img_marvel_placeholder)
                    .into(img)
            }
            container.setOnClickListener { callback(item) }
        }
    }

}
