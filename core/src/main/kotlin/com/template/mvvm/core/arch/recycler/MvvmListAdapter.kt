package com.template.mvvm.core.arch.recycler

import android.arch.lifecycle.ViewModel
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.template.mvvm.base.utils.LL

class MvvmListAdapter(
    @LayoutRes private val itemLayout: Int,
    private val vmItemLayout: Int,
    private val onListItemBound: OnListItemBoundListener?
) : RecyclerView.Adapter<MvvmItemViewHolder>() {

    private val list = arrayListOf<ViewModel>()

    override fun getItemCount() = list.size

    fun add(list: List<ViewModel>) {
        val position =  this.list.size
        if (list.isEmpty()) {
            onListItemBound?.onBound(position)
            LL.d("MvvmListAdapter.add, force on-bound.")
        } else {
            this.list.addAll(list)
            notifyItemRangeInserted(position, list.size)

        }
    }

    fun update(list: List<ViewModel>) {
        if (list.isEmpty()) {
            onListItemBound?.onBound(0)
            LL.d("MvvmListAdapter.update, force on-bound.")
        } else {
            this.list.clear()
            this.list.addAll(list)
            notifyDataSetChanged()
        }
    }

    fun delete() {
        this.list.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MvvmItemViewHolder, position: Int) {
        holder.bindViewModel(list[position])
        onListItemBound?.onBound(position)
        LL.d("MvvmListAdapter.onBindViewHolder: $position")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MvvmItemViewHolder(parent, itemLayout, vmItemLayout)
}

interface OnListItemBoundListener {
    fun onBound(position: Int)
}
