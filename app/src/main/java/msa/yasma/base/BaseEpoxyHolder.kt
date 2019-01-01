package msa.yasma.base

import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


abstract class BaseEpoxyHolder : EpoxyHolder() {

    lateinit var rootView: View

    override fun bindView(itemView: View) {
        rootView = itemView
    }

    protected fun <V : View> bind(id: Int): ReadOnlyProperty<BaseEpoxyHolder, V> =
        Lazy { epoxyHolder: BaseEpoxyHolder, prop ->
            epoxyHolder.rootView.findViewById(id) as V?
                ?: throw IllegalStateException("View ID $id for '${prop.name}' not found.")
        }


    private class Lazy<V>(
        private val initializer: (BaseEpoxyHolder, KProperty<*>) -> V
    ) : ReadOnlyProperty<BaseEpoxyHolder, V> {
        private object EMPTY

        private var value: Any? = EMPTY

        override fun getValue(thisRef: BaseEpoxyHolder, property: KProperty<*>): V {
            if (value == EMPTY) {
                value = initializer(thisRef, property)
            }
            @Suppress("UNCHECKED_CAST")
            return value as V
        }
    }
}
