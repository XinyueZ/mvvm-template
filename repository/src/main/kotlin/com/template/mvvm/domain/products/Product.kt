package com.template.mvvm.domain.products

import android.net.Uri
import com.template.mvvm.feeds.products.ProductData
import com.template.mvvm.source.local.entities.products.ProductEntity

data class Product(
        val pid: String,
        val title: String = "title",
        val description: String = "description",
        val thumbnail: Uri = Uri.EMPTY,
        val brand: Brand = Brand.EMPTY,
        val genders: List<String> = emptyList(),
        val ageGroups: List<String> = emptyList(),
        val categoryKeys: List<String> = emptyList()) {
    companion object {
        fun from(productEntity: ProductEntity) = Product(
                productEntity.pid,
                productEntity.title,
                productEntity.description,
                productEntity.thumbnail,
                Brand.from(productEntity.brand),
                productEntity.genders,
                productEntity.ageGroups,
                productEntity.categoryKeys
        )
        fun from(productData: ProductData) = Product(
                productData.pid,
                productData.name,
                String.format("%s//%s//%s", productData.brand.name, productData.genders.joinToString(), productData.ageGroups.joinToString()),
                productData.media.images.first().largeHdUrl,
                Brand.from(productData.brand),
                productData.genders,
                productData.ageGroups,
                productData.categoryKeys
        )
    }
}