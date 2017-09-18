package com.template.mvvm.binding.recycler

import android.content.Context
import android.databinding.BindingAdapter
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.support.annotation.DrawableRes
import android.support.annotation.IntDef
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

@BindingAdapter(value = *arrayOf("items", "binding", "factory", "onItemClicked", "onItemBound", "showLoading"), requireAll = false)
fun <T> bindItems(view: RecyclerView, items: ObservableList<T>?, binding: Binding.OnBind<T>?, factory: RecyclerAdapter.ViewBindingFactory?, onItemClicked: RecyclerAdapter.ItemClickListener?, onItemBound: RecyclerAdapter.ItemBoundListener?, showLoading: Boolean?) {
    if (items == null) {
        return
    }

    @Suppress("UNCHECKED_CAST")
    val adapter: RecyclerAdapter<T> =
            if (view.adapter is RecyclerAdapter<*>)
                view.adapter as RecyclerAdapter<T>
            else
                RecyclerAdapter(view.context)

    with(adapter) {
        showLoading?.let {
            showLoadingIndicator(it)
        }

        bindingFactory = factory

        if (this.items != items) {
            this.items = items as? ObservableArrayList<T> ?: ObservableArrayList()
        }

        binding?.let {
            this.binding = Binding(it)
        }

        itemBound = onItemBound
        itemClick = onItemClicked
    }

    with(view) {
        if (this.adapter == null) {
            this.adapter = adapter
        }
    }
}

@BindingAdapter("layoutManager")
fun layoutManager(view: RecyclerView, @LayoutManagerType type: Long?) {
    val layoutManager = when (type) {
        LINEAR_VERTICAL_REVERSE -> LayoutManager.linear(LinearLayoutManager.VERTICAL, true)
        else -> LayoutManager.linear(LinearLayoutManager.VERTICAL, false)
    }
    view.layoutManager = layoutManager.create(view.context)
}

@BindingAdapter("verticalItemDivider")
fun verticalItemDivider(view: RecyclerView, @DrawableRes verticalItemDivider: Int?) {
    if (verticalItemDivider == null || verticalItemDivider <= 0) {
        return
    }

    val divider = DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL)
    divider.setDrawable(ContextCompat.getDrawable(view.context, verticalItemDivider))
    view.addItemDecoration(divider)
}

class LayoutManager {
    interface LayoutManagerFactory {
        fun create(context: Context): RecyclerView.LayoutManager
    }

    @IntDef(LinearLayoutManager.HORIZONTAL.toLong(), LinearLayoutManager.VERTICAL.toLong())
    annotation class Orientation

    companion object {
        fun linear(@Orientation orientation: Int = LinearLayoutManager.VERTICAL, reverse: Boolean = false): LayoutManagerFactory {
            return object : LayoutManagerFactory {
                override fun create(context: Context): RecyclerView.LayoutManager {
                    return LinearLayoutManager(context, orientation, reverse)
                }
            }
        }
    }
}
