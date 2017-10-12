package com.template.mvvm.arch.recycler

import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListProvider
import android.arch.paging.TiledDataSource

class MvvmListDataProvider(list: List<ViewModel>) : LivePagedListProvider<Int, ViewModel>() {
    private val dataSource = MvvmListDataSource(list)
    override fun createDataSource() = dataSource
}

class MvvmListDataSource(private var list: List<ViewModel>) : TiledDataSource<ViewModel>() {
    override fun countItems() = list.size
    override fun loadRange(startPosition: Int, count: Int) = list
}