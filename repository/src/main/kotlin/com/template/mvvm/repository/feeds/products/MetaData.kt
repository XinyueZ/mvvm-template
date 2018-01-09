package com.template.mvvm.repository.feeds.products

import com.google.gson.annotations.SerializedName

data class MetaData(@SerializedName("category") val category: Category)

data class Category(@SerializedName("localizedId") val localizedId: String)