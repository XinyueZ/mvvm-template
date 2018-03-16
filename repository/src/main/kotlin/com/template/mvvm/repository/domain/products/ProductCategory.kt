package com.template.mvvm.repository.domain.products

import com.template.mvvm.repository.source.local.entities.products.ProductCategoryEntity
import com.template.mvvm.repository.source.remote.feeds.products.ProductCategoryData

data class ProductCategory(val cid: String, val name: String) {
    companion object {
        fun from(productCategoryEntity: ProductCategoryEntity) = ProductCategory(
            productCategoryEntity.cid,
            productCategoryEntity.name
        )

        fun from(productCategoryData: ProductCategoryData) = ProductCategory(
            productCategoryData.cid,
            productCategoryData.name

        )
    }
}