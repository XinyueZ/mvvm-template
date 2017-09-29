package com.template.mvvm.domain.products

import android.net.Uri
import com.template.mvvm.source.local.entities.products.ProductEntity

data class Product(
        val pid: String,
        val title: String = "title",
        val description: String = "description",
        val thumbnail: Uri = Uri.EMPTY,
        val brand: Brand = Brand.EMPTY) {
    companion object {
        fun from(productEntity: ProductEntity) = Product(productEntity.pid, productEntity.title, productEntity.description, productEntity.thumbnail, Brand.from(productEntity.brand))
    }
}