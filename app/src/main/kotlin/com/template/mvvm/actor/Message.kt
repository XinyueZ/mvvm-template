package com.template.mvvm.actor

interface Message<out T> {
    fun getDetail(): T
}