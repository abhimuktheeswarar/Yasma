package msa.yasma.post.list

import android.view.ViewGroup
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import msa.domain.core.Action
import msa.yasma.R
import msa.yasma.base.BaseEpoxyHolder

/**
 * Created by Abhi Muktheeswarar.
 */

@EpoxyModelClass(layout = R.layout.item_post)
abstract class PostItemModel : EpoxyModelWithHolder<PostItemModel.PostItemHolder>() {

    @EpoxyAttribute
    open var postId: Int = -1
    @EpoxyAttribute
    lateinit var title: String
    @EpoxyAttribute
    lateinit var name: String
    @EpoxyAttribute
    lateinit var body: String
    @EpoxyAttribute(hash = false)
    lateinit var postItemActionListener: (action: Action) -> Unit

    override fun bind(holder: PostItemHolder) {
        super.bind(holder)
        holder.titleTextView.text = title
        holder.nameTextView.text = name
        holder.bodyTextView.text = body

        holder.rootItemView.setOnClickListener { }
    }

    override fun unbind(holder: PostItemHolder) {
        super.unbind(holder)
        holder.rootItemView.setOnClickListener(null)
    }

    class PostItemHolder : BaseEpoxyHolder() {

        val rootItemView by bind<ViewGroup>(R.id.constraintLayout_item_post)
        val titleTextView by bind<TextView>(R.id.text_title)
        val nameTextView by bind<TextView>(R.id.text_name)
        val bodyTextView by bind<TextView>(R.id.text_body)

    }

}