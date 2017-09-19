package com.template.mvvm.binding.recycler

import android.databinding.ObservableList
import android.os.Looper
import android.util.Log
import java.lang.ref.WeakReference

class OnListChanged<T>(adapter: RecyclerAdapter<T>, val items: ObservableList<T>) : ObservableList.OnListChangedCallback<ObservableList<T>>() {


    val reference = WeakReference(adapter)

    override fun onChanged(list: ObservableList<T>?) {
        val adapter = reference.get() ?: return

        ensureChangeOnMainThread()
        adapter.notifyDataSetChanged()
    }

    override fun onItemRangeInserted(list: ObservableList<T>?, from: Int, count: Int) {
        val adapter = reference.get() ?: return

        ensureChangeOnMainThread()
        adapter.notifyItemRangeInserted(from, count)
    }

    override fun onItemRangeMoved(list: ObservableList<T>?, from: Int, to: Int, count: Int) {
        val adapter = reference.get() ?: return

        ensureChangeOnMainThread()
        for (i in 0..count - 1) {
            adapter.notifyItemMoved(from + i, to + i)
        }
    }

    override fun onItemRangeRemoved(list: ObservableList<T>?, from: Int, count: Int) {
        val adapter = reference.get() ?: return

        ensureChangeOnMainThread()
        adapter.notifyItemRangeRemoved(from, count)
    }

    override fun onItemRangeChanged(list: ObservableList<T>?, from: Int, count: Int) {
        val adapter = reference.get() ?: return

        ensureChangeOnMainThread()
        adapter.notifyItemMoved(from, count)
    }

    private fun ensureChangeOnMainThread() {
        Log.d("OnListChanged", "something changed")

        if (Thread.currentThread() !== Looper.getMainLooper().thread) {
            throw IllegalStateException("You must only modify the ObservableList on the main thread.")
        }
    }
}