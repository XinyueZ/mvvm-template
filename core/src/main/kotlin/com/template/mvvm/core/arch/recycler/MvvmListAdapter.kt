package com.template.mvvm.core.arch.recycler

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.view.updateLayoutParams
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.isAccessible

class MvvmListAdapter(
    @LayoutRes private val itemLayout: Int,
    private val onListItemBound: OnListItemBoundListener?,
    layout: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var isLoading = false
    private val collection = arrayListOf<ViewModel>()
    private val paramsFactory = ProgressViewContainerLayoutParamsFactory(layout)

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
                launch {
                    delay(500)
                    withContext(UI) { notifyItemRemoved(position) }
                }
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
        collection.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        VIEW_TYPE_LOAD -> ProgressViewHolder(parent, paramsFactory)
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
    parent: ViewGroup,
    private val paramsFactory: ProgressViewContainerLayoutParamsFactory
) : RecyclerView.ViewHolder(
    ProgressViewContainer(parent.context, paramsFactory)
) {

    fun adjustProgressType(init: Boolean) {
        with(itemView as ViewGroup) {
            if (init) paramsFactory.init(itemView)
            else paramsFactory.update(itemView)
        }
    }
}

class ProgressViewContainerLayoutParamsFactory(private val layout: String) {
    fun create() = when (layout) {
        "linear-v" -> ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        else -> ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    fun init(viewGroup: ViewGroup) {
        viewGroup.updateLayoutParams {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.MATCH_PARENT
        }
    }

    fun update(viewGroup: ViewGroup) {
        when (layout) {
            "linear-v" -> viewGroup.updateLayoutParams {
                width = ViewGroup.LayoutParams.MATCH_PARENT
                height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            else -> viewGroup.updateLayoutParams {
                width = ViewGroup.LayoutParams.WRAP_CONTENT
                height = ViewGroup.LayoutParams.MATCH_PARENT
            }
        }
    }
}

private class ProgressViewContainer(
    context: Context,
    private val paramsFactory: ProgressViewContainerLayoutParamsFactory
) : FrameLayout(context) {

    private fun init() {
        /**
         * Layout for container.
         */
        layoutParams = paramsFactory.create()

        /**
         * Added a progress UI, default is [FrameLayout.LayoutParams.WRAP_CONTENT].
         */
        ProgressBar(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).also { it.gravity = Gravity.CENTER }


            setBackgroundColor(
                ResourcesCompat.getColor(
                    resources,
                    android.R.color.transparent,
                    null
                )
            )

            addView(this)
        }
    }

    init {
        init()
    }
}
