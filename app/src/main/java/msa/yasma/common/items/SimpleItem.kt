package msa.yasma.common.items

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import msa.domain.actionstate.AlbumAction
import msa.domain.actionstate.PostAction
import msa.domain.core.Action
import msa.yasma.R
import msa.yasma.base.BaseEpoxyHolder

/**
 * Created by Abhi Muktheeswarar.
 */

@EpoxyModelClass(layout = R.layout.item_simple)
abstract class SimpleItemModel : EpoxyModelWithHolder<SimpleItemModel.SimpleItemHolder>() {

    @EpoxyAttribute
    open var itemId: Int = -1
    @EpoxyAttribute
    open var userId: Int = -1
    @EpoxyAttribute
    lateinit var itemType: SimpleItemType
    @EpoxyAttribute
    lateinit var title: String
    @EpoxyAttribute
    lateinit var name: String
    @EpoxyAttribute
    lateinit var body: String
    @EpoxyAttribute(hash = false)
    lateinit var itemActionListener: (action: Action) -> Unit

    override fun bind(holder: SimpleItemHolder) {
        super.bind(holder)
        holder.titleTextView.text = title
        holder.nameTextView.text = name
        if (::body.isInitialized) {
            holder.bodyTextView.text = body
            holder.bodyTextView.visibility = View.VISIBLE

        } else holder.bodyTextView.visibility = View.GONE

        holder.rootItemView.setOnClickListener {

            if (itemType == SimpleItemType.POST) itemActionListener(
                PostAction.LoadPostDetailAction(
                    postId = itemId,
                    userId = userId
                )
            ) else if (itemType == SimpleItemType.ALBUM) itemActionListener(
                AlbumAction.LoadAlbumDetailAction(
                    albumId = itemId,
                    userId = userId
                )
            )
        }
    }

    override fun unbind(holder: SimpleItemHolder) {
        super.unbind(holder)
        holder.rootItemView.setOnClickListener(null)
    }

    class SimpleItemHolder : BaseEpoxyHolder() {

        val rootItemView by bind<ViewGroup>(R.id.constraintLayout_item_simple)
        val titleTextView by bind<TextView>(R.id.text_title)
        val nameTextView by bind<TextView>(R.id.text_name)
        val bodyTextView by bind<TextView>(R.id.text_body)

    }

    enum class SimpleItemType {

        POST, ALBUM
    }

}