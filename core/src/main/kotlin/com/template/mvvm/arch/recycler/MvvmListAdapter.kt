package com.template.mvvm.arch.recycler

import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedListAdapter
import android.support.annotation.LayoutRes
import android.support.v7.recyclerview.extensions.DiffCallback
import android.view.ViewGroup

class MvvmListAdapter(
        private @LayoutRes val itemLayout: Int,
        private val vmItemLayout: Int
) : PagedListAdapter<ViewModel, MvvmItemViewHolder>(diffCallback) {
    override fun onBindViewHolder(holder: MvvmItemViewHolder, position: Int) {
        holder.bindViewModel(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MvvmItemViewHolder(parent, itemLayout, vmItemLayout)

    companion object {
        private val diffCallback = object : DiffCallback<ViewModel>() {
            override fun areItemsTheSame(oldItem: ViewModel, newItem: ViewModel) = oldItem == newItem
            override fun areContentsTheSame(oldItem: ViewModel, newItem: ViewModel) = oldItem == newItem
        }
    }
}