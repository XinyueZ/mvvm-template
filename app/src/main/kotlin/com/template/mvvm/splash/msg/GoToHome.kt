package com.template.mvvm.splash.msg

import com.template.mvvm.actor.Detail
import com.template.mvvm.actor.Message

class GoToHome : Message<Detail> {
    override fun getDetail() = Detail.DEFAULT
}