package com.template.mvvm.domain.products

import android.net.Uri
import com.template.mvvm.source.local.entities.products.ProductEntity

data class Product(
        val title: String = "title",
        val description: String = "description",
        val thumbnail: Uri = Uri.EMPTY,
        val brandLogo: Uri = Uri.EMPTY) {
    companion object {
        fun from(productEntity: ProductEntity) = Product(productEntity.title, productEntity.description, productEntity.thumbnail, productEntity.brandLogo)
    }
}