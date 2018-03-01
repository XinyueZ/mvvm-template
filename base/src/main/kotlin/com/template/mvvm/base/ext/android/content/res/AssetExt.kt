package com.template.mvvm.base.ext.android.content.res

import android.content.res.AssetManager
import java.io.IOException

@Throws(IOException::class)
internal fun AssetManager.rd(filePath: String) = open(filePath).bufferedReader().use { it.readText() }

fun AssetManager.read(filePath: String) = rd(filePath)