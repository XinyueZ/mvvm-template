package com.template.mvvm.home.msg

import com.template.mvvm.actor.Detail
import com.template.mvvm.actor.Message

class OpenItem(private val itemIndex: Int) : Message<Detail<Int>> {
    override fun getDetail() = Detail(itemIndex)
}