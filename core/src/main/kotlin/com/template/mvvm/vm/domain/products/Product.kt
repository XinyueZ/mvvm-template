package com.template.mvvm.vm.domain.products

import android.net.Uri

data class Product(
        val title: String = "title",
        val description: String = "description",
        val thumbnail: Uri = Uri.EMPTY,
        val brandLogo: Uri = Uri.EMPTY)