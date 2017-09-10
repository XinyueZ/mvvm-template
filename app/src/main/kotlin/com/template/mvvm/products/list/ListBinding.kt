package com.template.mvvm.products.list

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import com.template.mvvm.BR
import com.template.mvvm.R
import de.immowelt.mobile.livestream.core.ui.binding.recycler.Binding
import de.immowelt.mobile.livestream.core.ui.binding.recycler.RecyclerAdapter

private const val DEFAULT_BINDING_ITEM = 0

class ListBinding : Binding.OnBind<ProductItemViewModel> {
    override fun onBind(binding: Binding<ProductItemViewModel>, position: Int, data: ProductItemViewModel) {
        binding.set(BR.vm, DEFAULT_BINDING_ITEM)
    }
}

private const val FALLBACK_LAYOUT = R.layout.item_default

class ListViewFactory(val types: Map<Int, Int> = mapOf(0 to R.layout.item_product)) : RecyclerAdapter.ViewBindingFactory {
    override fun create(type: Int, inflater: LayoutInflater, parent: ViewGroup): ViewDataBinding {
        val layout = types[type]
        return DataBindingUtil.inflate<ViewDataBinding>(inflater, layout ?: FALLBACK_LAYOUT, parent, false)
    }
}