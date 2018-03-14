package com.template.mvvm.core.arch.recycler

import android.arch.lifecycle.ViewModel
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.view.get
import androidx.view.updateLayoutParams
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
            /**When [isLoading] true the loading progress as item should be extra added.*/
        collection.size + if (isLoading) 1 else 0

    fun add(newCollection: Collection<ViewModel>) {
        val position = collection.size
        if (newCollection.isNotEmpty()) {
            /**
             * There's valid [newCollection] coming, the loading progress must be removed,
             * append [newCollection] to [collection], notify [MvvmListAdapter] to load next.
             * The important +1 at [notifyItemRangeInserted] is for next loading progress.
             */
            notifyItemRemoved(position)
            isLoading = false
            collection.addAll(newCollection)
            isLoading = true
            notifyItemRangeInserted(
                position,
                newCollection.size + 1
            )
        } else {
            /**
             * When [newCollection] is empty, that means the init-loading
             * or end-loading if current [collection] is not empty.
             */
            if (collection.isEmpty()) {
                isLoading = true
                notifyItemInserted(0)
            } else {
                isLoading = false
            }
        }
    }

    fun update(newCollection: Collection<ViewModel>) {
        if (newCollection.isNotEmpty()) {
            notifyItemRemoved(0)
            isLoading = false
            collection.clear()
            collection.addAll(newCollection)
            notifyDataSetChanged()
        } else {
            /**
             * When [newCollection] is empty, that means the init-loading
             * or end-loading if current [collection] is not empty.
             */
            if (collection.isEmpty()) {
                isLoading = true
                notifyItemInserted(0)
            } else {
                isLoading = false
            }
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
                (holder as ProgressViewHolder).adjustProgressType(collection.isEmpty())
                onListItemBound?.onBound(position)
            } // Should load on the bound.
            else -> (holder as MvvmItemViewHolder).bindViewModel(collection[position])
        }
    }

    override fun getItemViewType(position: Int) = when (position) {
    /**
     * When the [position] equals to size of [collection],
     * means showing loading progress.
     */
        collection.size -> VIEW_TYPE_LOAD
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
        R.layout.item_load,
        parent,
        false
    )
) {
    fun adjustProgressType(init: Boolean) {
        val pb: ProgressBar = (itemView as ViewGroup)[0] as ProgressBar
        if(init) {
            itemView.updateLayoutParams {  height = ViewGroup.LayoutParams.MATCH_PARENT }
        }  else {
            itemView.updateLayoutParams {  height = ViewGroup.LayoutParams.WRAP_CONTENT }
        }
    }
}