package msa.yasma.common.items

import android.widget.ProgressBar
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import msa.yasma.R
import msa.yasma.base.BaseEpoxyHolder


@EpoxyModelClass(layout = R.layout.item_loading)
abstract class LoadingItemModel : EpoxyModelWithHolder<LoadingItemModel.LoadingItemViewEpoxyHolder>() {


    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
        return totalSpanCount
    }

    class LoadingItemViewEpoxyHolder : BaseEpoxyHolder() {

        val progressBar by bind<ProgressBar>(R.id.progressBar_list)

    }
}