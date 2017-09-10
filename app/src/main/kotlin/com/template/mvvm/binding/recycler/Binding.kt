package de.immowelt.mobile.livestream.core.ui.binding.recycler

import android.arch.lifecycle.ViewModel
import android.databinding.ViewDataBinding

class Binding<in T> constructor(private val bind: OnBind<T>) {

    interface OnBind<T> {
        fun onBind(binding: Binding<T>, position: Int, data: T)
    }

    companion object {
        val TYPE_DEFAULT = 0

        private val VAR_NONE = 0
        private val VAR_INVALID = -1
    }

    var variable = VAR_NONE
        private set

    var type = TYPE_DEFAULT
        private set

    fun set(variableId: Int, type: Int) {
        this.variable = variableId
        this.type = type
    }

    fun onItemBind(position: Int, data: T) {
        variable = VAR_NONE
        type = TYPE_DEFAULT
        bind.onBind(this, position, data)
        if (variable == VAR_INVALID) {
            throw IllegalStateException("variableId not set in onItemBind()")
        }
    }

    fun bind(binding: ViewDataBinding, data: T) {
        val result = binding.setVariable(variable, data)
        if (result) {
            if (data is ViewModel) {
                //TODO Here might be some stories, I let empty firstly.
            }
            return
        }
        throw IllegalStateException("variable with id $variable not found")
    }
}