package com.template.mvvm.products.msg

import com.template.mvvm.actor.Detail
import com.template.mvvm.actor.Message

class LoadProductList(private val msg: String) : Message<Detail<String>> {
    override fun getDetail() = Detail(msg)
}