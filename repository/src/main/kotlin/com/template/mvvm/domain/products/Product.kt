package com.template.mvvm.domain.products

import com.template.mvvm.feeds.products.ProductData
import com.template.mvvm.source.local.entities.products.ImageEntity
import com.template.mvvm.source.local.entities.products.ProductEntity

data class Product(
        val pid: String,
        val title: String = "title",
        val description: String = "description",
        val pictures: List<Image> = emptyList(),
        val brand: Brand = Brand.EMPTY,
        val genders: List<String> = emptyList(),
        val ageGroups: List<String> = emptyList(),
        val categoryKeys: List<String> = emptyList()) {
    companion object {
        fun from(productEntity: ProductEntity, imageEntities: List<ImageEntity>) = Product(
                productEntity.pid,
                productEntity.title,
                productEntity.description,
                imageEntities.map {
                    Image.from(it)
                },
                Brand.from(productEntity.brand),
                productEntity.genders,
                productEntity.ageGroups,
                productEntity.categoryKeys
        )

        fun from(productData: ProductData) = Product(
                productData.pid,
                productData.name,
                String.format("%s//%s//%s", productData.brand.name, productData.genders.joinToString(), productData.ageGroups.joinToString()),
                productData.media.images.map {
                    Image.from(productData, it)
                },
                Brand.from(productData.brand),
                productData.genders,
                productData.ageGroups,
                productData.categoryKeys
        )
    }
}