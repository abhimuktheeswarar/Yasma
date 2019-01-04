package msa.yasma.common.items

import android.widget.TextView
import msa.yasma.R
import msa.yasma.base.BaseKotlinEpoxyModel

/**
 * Created by Abhi Muktheeswarar.
 */

class UserDetailItemModel(
    private val name: String,
    private val email: String,
    private val phone: String,
    private val company: String
) :
    BaseKotlinEpoxyModel(R.layout.item_user_detail) {

    companion object {

        const val ID = "UserDetailItemModel"
    }

    private val nameTextView by bind<TextView>(R.id.text_name)
    private val emailTextView by bind<TextView>(R.id.text_email)
    private val phoneTextView by bind<TextView>(R.id.text_phone)
    private val companyTextView by bind<TextView>(R.id.text_company)

    override fun bind() {

        nameTextView.text = name
        emailTextView.text = email
        phoneTextView.text = phone
        companyTextView.text = company

    }

    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
        return totalSpanCount
    }
}

