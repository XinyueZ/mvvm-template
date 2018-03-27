package com.template.mvvm.base.ext.android.content.res

import android.content.res.Resources

fun Resources.hasIdentifier(resId: Int, type: String, packageName: String) =
    getIdentifier(resId.toString(), type, packageName) != 0