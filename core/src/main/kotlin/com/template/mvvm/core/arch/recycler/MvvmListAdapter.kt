package com.template.mvvm.core.arch.recycler

import android.arch.lifecycle.ViewModel
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.template.mvvm.core.R
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.isAccessible

class MvvmListAdapter(
    @LayoutRes private val itemLayout: Int,
    private val onListItemBound: OnListItemBoundListener?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var isLoading = false
    private val collection = arrayListOf<ViewModel>()

    override fun getItemCount() =
        collection.size + if (isLoading) 1 else 0// The 1 is for the loading progress.

    fun add(newCollection: Collection<ViewModel>) {
        val position = collection.size
        if (newCollection.isEmpty()) {
            isLoading = true
            if (collection.isNotEmpty()) notifyItemInserted(position)
        } else {
            isLoading = false
            notifyItemRemoved(position) // Remove loading progress.
            collection.addAll(newCollection)
            notifyItemRangeInserted(position, newCollection.size)
        }
    }

    fun update(newCollection: Collection<ViewModel>) {
        val position = collection.size
        if (newCollection.isEmpty()) {
            isLoading = true
            notifyItemInserted(position)
        } else {
            isLoading = false
            notifyItemRemoved(position)  // Remove loading progress.
            collection.clear()
            collection.addAll(newCollection)
            notifyDataSetChanged()
        }
    }

    fun delete() {
        isLoading = false
        this.collection.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        VIEW_TYPE_LOAD -> ProgressViewHolder(parent)
        else -> MvvmItemViewHolder(parent, itemLayout)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_LOAD -> {
                onListItemBound?.onBound(position)
            }
            else -> {
                (holder as MvvmItemViewHolder).bindViewModel(collection[position])
                if (position == itemCount - 1) onListItemBound?.onBound(position)
            }
        }
    }

    override fun getItemViewType(position: Int) = when (position) {
        itemCount - 1 -> VIEW_TYPE_LOAD
        else -> VIEW_TYPE_ITEM
    }

    companion object {
        private const val VIEW_TYPE_LOAD = 0x9
        private const val VIEW_TYPE_ITEM = 0x10
    }
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

class ProgressViewHolder(
    parent: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(
        R.layout.item_loading_progress,
        parent,
        false
    )
)