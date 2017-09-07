package com.template.mvvm.actor

open class Detail<out T>(val thing: T? = null, private val body: (() -> Unit)? = null) {

    companion object {
        val DEFAULT = object : Detail<Any>() {
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

    override fun toString() =
            when (thing) {
                null -> super.toString()
                else -> thing.toString()
            }
}
