package com.template.mvvm.domain.products

import com.template.mvvm.feeds.products.ProductData
import com.template.mvvm.source.local.entities.products.ImageEntity
import com.template.mvvm.source.local.entities.products.ProductEntity

data class Product(
        val pid: String,
        val title: String = "title",
        val description: String = "description",
        val pictures: Map<String, Image> = emptyMap(),
        val genders: List<String> = emptyList()) {
    companion object {
        fun from(productEntity: ProductEntity, imageEntities: List<ImageEntity>) = Product(
                productEntity.pid,
                productEntity.title,
                productEntity.description,
                mutableMapOf<String, Image>().apply {
                    imageEntities.forEach {
                        put(it.size, Image.from(it))
                    }

                },
                productEntity.genders
        )

        fun from(productData: ProductData, genders: List<String>) = Product(
                productData.pid,
                productData.name,
                productData.description,
                mutableMapOf<String, Image>().apply {
                    productData.image.sizes.run {
                        put(this.small.sizeName, Image.from(productData, this.small))
                        put(this.xLarge.sizeName, Image.from(productData, this.xLarge))
                        put(this.medium.sizeName, Image.from(productData, this.medium))
                        put(this.large.sizeName, Image.from(productData, this.large))
                        put(this.best.sizeName, Image.from(productData, this.best))
                        put(this.original.sizeName, Image.from(productData, this.original))
                        put(this.iPhone.sizeName, Image.from(productData, this.iPhone))
                        put(this.iPhoneSmall.sizeName, Image.from(productData, this.iPhoneSmall))
                    }
                },
                genders
        )
    }
}