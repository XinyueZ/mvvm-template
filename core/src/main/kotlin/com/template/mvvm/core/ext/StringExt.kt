package com.template.mvvm.core.ext

import android.os.Build
import android.text.Html
import android.text.Spanned

fun String.toHtml(trimmed: Boolean = true): Spanned? {
    val result = when {
        isEmpty() -> null
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> Html.fromHtml(this, 0)
        else -> Html.fromHtml(this)
    }

    return when (trimmed) {
        true -> result?.trim() as Spanned
        false -> result
    }
}