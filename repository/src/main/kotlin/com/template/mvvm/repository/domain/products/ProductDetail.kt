package com.template.mvvm.repository.domain.products

import com.template.mvvm.repository.source.local.entities.products.ImageEntity
import com.template.mvvm.repository.source.local.entities.products.ProductEntity

data class ProductDetail(
        val pid: Long,
        val title: String = "title",
        val description: String = "description",
        val pictures: List<Image> = emptyList()
) {
    companion object {
        fun from(productEntity: ProductEntity, imageEntities: List<ImageEntity>) = ProductDetail(
                productEntity.pid,
                productEntity.title,
                productEntity.description,
                imageEntities.map {
                    Image.from(it)
                }
        )
    }
}