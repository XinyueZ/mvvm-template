package com.template.mvvm.base.ext.lang

fun Int.inverted() = 0x00FFFFFF - (this or -0x1000000) or (this and -0x1000000)