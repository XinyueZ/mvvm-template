package com.template.mvvm.core.arch.recycler

import android.arch.lifecycle.ViewModel
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.template.mvvm.base.utils.LL
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.isAccessible

class MvvmListAdapter(
    @LayoutRes private val itemLayout: Int,
    private val onListItemBound: OnListItemBoundListener?
) : RecyclerView.Adapter<MvvmItemViewHolder>() {

    private val list = arrayListOf<ViewModel>()

    override fun getItemCount() = list.size

    fun add(list: List<ViewModel>) {
        val position = this.list.size
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
        MvvmItemViewHolder(parent, itemLayout)

}

interface OnListItemBoundListener {
    fun onBound(position: Int)
}

class MvvmItemViewHolder(
    parent: ViewGroup,
    @LayoutRes layout: Int,
    private val binding: ViewDataBinding? = DataBindingUtil.bind(
        LayoutInflater.from(parent.context).inflate(
            layout,
            parent,
            false
        )
    )
) : RecyclerView.ViewHolder(binding?.root) {
    fun bindViewModel(vm: ViewModel?) = binding.setViewModel(vm)
}

private fun ViewDataBinding?.setViewModel(vm: ViewModel?): ViewDataBinding? {
    try {
        this?.let { binding ->
            vm?.let { vm ->
                binding::class.memberFunctions.find { it.name == "setVm" }?.let { func ->
                    func.isAccessible = true
                    func.call(binding, vm)
                }
            }
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
    } finally {
        this?.executePendingBindings()
        return this
    }
}

