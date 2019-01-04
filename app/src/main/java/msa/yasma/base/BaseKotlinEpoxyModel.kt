package msa.yasma.base

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.airbnb.epoxy.EpoxyModel
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by Abhi Muktheeswarar.
 */

abstract class BaseKotlinEpoxyModel(@LayoutRes private val layoutRes: Int) : EpoxyModel<View>() {

    protected var rootView: View? = null

    abstract fun bind()

    override fun bind(view: View) {
        this.rootView = view
        bind()
    }

    override fun unbind(view: View) {
        this.rootView = null
    }

    override fun getDefaultLayout() = layoutRes

    protected fun <V : View> bind(@IdRes id: Int) = object : ReadOnlyProperty<BaseKotlinEpoxyModel, V> {
        override fun getValue(thisRef: BaseKotlinEpoxyModel, property: KProperty<*>): V {
            // This is not efficient because it looks up the rootView by id every time (it loses
            // the pattern of a "holder" to cache that look up). But it is simple to use and could
            // be optimized with a map
            @Suppress("UNCHECKED_CAST")
            return rootView?.findViewById(id) as V?
                ?: throw IllegalStateException("View ID $id for '${property.name}' not found.")
        }
    }
}
