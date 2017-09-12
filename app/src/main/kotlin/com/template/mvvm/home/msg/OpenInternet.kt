package com.template.mvvm.home.msg

import com.template.mvvm.actor.Detail
import com.template.mvvm.actor.Message

class OpenInternet(private val msg: String) : Message<Detail<String>> {
    override fun getDetail() = Detail(msg)
}