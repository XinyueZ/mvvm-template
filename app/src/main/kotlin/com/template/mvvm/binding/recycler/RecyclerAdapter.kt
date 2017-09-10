package de.immowelt.mobile.livestream.core.ui.binding.recycler

import android.content.Context
import android.databinding.*
import android.databinding.ObservableList.OnListChangedCallback
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.template.mvvm.R

private const val VIEW_TYPE_LOADING = Int.MAX_VALUE

class RecyclerAdapter<T> constructor(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val inflater: LayoutInflater = LayoutInflater.from(context)
    var onListChanged: OnListChangedCallback<ObservableList<T>>? = null
    var binding: Binding<T>? = null
    var recyclerView: RecyclerView? = null
    var holderFactory: ViewHolderFactory? = null
    var bindingFactory: ViewBindingFactory? = null

    var items = ObservableArrayList<T>()
        set(value) {
            field = value

            if (isAttachedToRecyclerView) {
                onListChanged = OnListChanged(this, field as ObservableList<T>)
                (field as ObservableList<T>).addOnListChangedCallback(onListChanged)
            }

            notifyDataSetChanged()
        }

    private var isAttachedToRecyclerView = false
    var itemClick: ItemClickListener? = null
    var itemBound: ItemBoundListener? = null

    private var isLoading = false

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        binding?.let {
            val item = items[position]
            val bind: ViewDataBinding = DataBindingUtil.getBinding(holder.itemView)
            bind.root.setOnClickListener { itemClick?.onItemClicked(it, holder.adapterPosition) }
            onBindBinding(bind, item)
        }

        itemBound?.onItemBound(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>) {
        if (position == items.size) {
            // loading -> need no ui updates
            return
        }

        if (isForDataBinding(payloads)) {
            val binding: ViewDataBinding = DataBindingUtil.getBinding(holder.itemView)
            onBindViewHolder(holder, position)
            binding.executePendingBindings()
            return
        }

        super.onBindViewHolder(holder, position, payloads)
    }

    private fun isForDataBinding(payloads: List<Any>?): Boolean {
        payloads?.let {
            it.forEach {
                if (it !== DATA_INVALIDATION) {
                    return false
                }
            }

            return true
        }

        return false
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): RecyclerView.ViewHolder {
        if (type == VIEW_TYPE_LOADING) {
            return onCreateLoadingViewHolder(parent)
        }

        val binding = onCreateBinding(inflater, type, parent)
        val holder = onCreateViewHolder(binding)

        binding.addOnRebindCallback(object : OnRebindCallback<ViewDataBinding>() {
            override fun onPreBind(bind: ViewDataBinding?): Boolean {
                return recyclerView == null || recyclerView?.isComputingLayout as Boolean
            }

            override fun onCanceled(bind: ViewDataBinding?) {
                if (recyclerView == null || recyclerView?.isComputingLayout as Boolean) {
                    return
                }

                val position = holder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    notifyItemChanged(position, DATA_INVALIDATION)
                }
            }
        })

        return holder
    }

    fun onCreateViewHolder(binding: ViewDataBinding): ViewHolder {
        holderFactory?.let {
            return it.create(binding)
        }

        return BindingViewHolder(binding)
    }

    fun onCreateBinding(inflater: LayoutInflater, type: Int, viewGroup: ViewGroup): ViewDataBinding {
        bindingFactory?.let {
            return it.create(type, inflater, viewGroup)
        }

        return DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.item_default, viewGroup, false)
    }

    override fun getItemCount(): Int {
        return items.size + if (isLoading) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        if (position == items.size) {
            return VIEW_TYPE_LOADING
        }

        binding?.onItemBind(position, items.get(position))
        return binding?.type ?: Binding.TYPE_DEFAULT
    }

    fun onBindBinding(dataBinding: ViewDataBinding, data: T) {
        binding?.bind(dataBinding, data)
        dataBinding.executePendingBindings()
    }

    override fun onAttachedToRecyclerView(view: RecyclerView?) {
        isAttachedToRecyclerView = true

        if (this.recyclerView == null) {
            onListChanged = OnListChanged(this, items as ObservableList<T>)
            (items as ObservableList<T>).addOnListChangedCallback(onListChanged)
        }

        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        isAttachedToRecyclerView = false

        if (this.recyclerView != null) {
            (items as ObservableList<T>).removeOnListChangedCallback(onListChanged)
            onListChanged = null
        }

        this.recyclerView = null
    }

    override fun onViewRecycled(holder: ViewHolder?) {
        super.onViewRecycled(holder)

        if (holder == null) {
            return
        }

        holder.itemView.setOnClickListener(null)
    }

    interface ViewHolderFactory {
        fun create(binding: ViewDataBinding): RecyclerView.ViewHolder
    }

    interface ViewBindingFactory {
        fun create(type: Int, inflater: LayoutInflater, parent: ViewGroup): ViewDataBinding
    }

    private class BindingViewHolder(binding: ViewDataBinding) : ViewHolder(binding.root)

    private object DATA_INVALIDATION


    interface ItemClickListener {
        fun onItemClicked(view: View, position: Int)
    }

    interface ItemBoundListener {
        fun onItemBound(position: Int)
    }

    fun showLoadingIndicator(show: Boolean) {
        if (show == isLoading) {
            return
        }

        isLoading = show

        if (show) {
            notifyItemInserted(items.size)
        } else {
            notifyItemRemoved(items.size)
        }
    }

    private fun onCreateLoadingViewHolder(parent: ViewGroup): ViewHolder {
        return BindingViewHolder(createLoadingViewDataBinding(inflater, parent))
    }

    /**
     * Can be overwritten to use custom view binding (layout) for loading indicator.
     */
    fun createLoadingViewDataBinding(inflater: LayoutInflater, parent: ViewGroup): ViewDataBinding {
        return DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.item_loading, parent, false)
    }
}