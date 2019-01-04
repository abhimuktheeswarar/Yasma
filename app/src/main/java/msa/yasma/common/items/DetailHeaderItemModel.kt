package msa.yasma.common.items

import android.view.View
import android.widget.TextView
import msa.yasma.R
import msa.yasma.base.BaseKotlinEpoxyModel

/**
 * Created by Abhi Muktheeswarar.
 */

data class DetailHeaderItemModel(private val title: String, private val body: String? = null) :
    BaseKotlinEpoxyModel(R.layout.item_detail_header) {

    companion object {

        const val ID = "DetailHeaderItemModel"
    }

    private val titleTextView by bind<TextView>(R.id.text_title)
    private val bodyTextView by bind<TextView>(R.id.text_body)

    override fun bind() {

        titleTextView.text = title
        if (body == null) bodyTextView.visibility = View.GONE
        else {

            bodyTextView.visibility = View.VISIBLE
            bodyTextView.text = body
        }
    }

    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
        return totalSpanCount
    }
}
