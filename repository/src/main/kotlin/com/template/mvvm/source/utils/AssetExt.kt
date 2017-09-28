package com.template.mvvm.source.utils

import android.content.res.AssetManager
import io.reactivex.Single
import java.io.IOException

@Throws(IOException::class)
internal fun AssetManager.rd(filePath: String) = open(filePath).bufferedReader().use { it.readText() }

fun AssetManager.read(filePath: String) = Single.just(rd(filePath))