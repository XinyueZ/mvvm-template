package com.template.mvvm.actor

open class Detail(private val body: (() -> Unit)? = null) {
    companion object {
        val DEFAULT = object: Detail() {
            override fun toString(): String {
                return "Default detail of some actor."
            }
        }
    }

    fun executeBody() {
        body?.let {
            it()
        }
    }
}
