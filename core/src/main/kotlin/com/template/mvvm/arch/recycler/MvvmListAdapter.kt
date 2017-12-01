package com.template.mvvm.arch.recycler

import android.arch.lifecycle.ViewModel
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.template.mvvm.LL
import com.template.mvvm.binding.OnListItemBoundListener

class MvvmListAdapter(
        private @LayoutRes val itemLayout: Int,
        private val vmItemLayout: Int,
        private val onListItemBound: OnListItemBoundListener?
) : RecyclerView.Adapter<MvvmItemViewHolder>() {

    private val list = arrayListOf<ViewModel>()

    override fun getItemCount() = list.size

    fun add(list: List<ViewModel>) {
        this.list.addAll(list)
        notifyItemRangeInserted(this.list.size, list.size)
    }

    fun update(list: List<ViewModel>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MvvmItemViewHolder, position: Int) {
        holder.bindViewModel(list[position])
        onListItemBound?.onBound(position)
        LL.d("onBindViewHolder: $position")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MvvmItemViewHolder(parent, itemLayout, vmItemLayout)
}