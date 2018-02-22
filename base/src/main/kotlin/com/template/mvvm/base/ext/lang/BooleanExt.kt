package com.template.mvvm.base.ext.lang

inline fun Boolean?.execute(trueCase: () -> Unit, falseCase:() ->Unit) {
    when (this) {
        true -> trueCase()
        else -> falseCase()
    }
}