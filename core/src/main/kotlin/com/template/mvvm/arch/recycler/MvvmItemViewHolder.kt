package com.template.mvvm.arch.recycler

import android.arch.lifecycle.ViewModel
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class MvvmItemViewHolder(
        parent: ViewGroup,
        @LayoutRes layout: Int,
        private val vmItemLayout: Int,
        private val binding: ViewDataBinding = DataBindingUtil.bind<ViewDataBinding>(LayoutInflater.from(parent.context).inflate(layout, parent, false)))
    : RecyclerView.ViewHolder(binding.root) {

    fun bindViewModel(vm: ViewModel?) {
        vm?.let {
            binding.setVariable(vmItemLayout, vm)
            binding.executePendingBindings()
        }
    }
}