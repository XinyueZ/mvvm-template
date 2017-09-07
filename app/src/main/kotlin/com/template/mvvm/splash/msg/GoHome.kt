package com.template.mvvm.splash.msg

import com.template.mvvm.actor.Detail
import com.template.mvvm.actor.Message

class GoHome(private val msg: String) : Message<Detail<String>> {
    override fun getDetail() = Detail(msg)
}