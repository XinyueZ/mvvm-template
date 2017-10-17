package com.template.mvvm.domain.products

import android.net.Uri
import com.template.mvvm.feeds.products.ImageData
import com.template.mvvm.feeds.products.ProductData
import com.template.mvvm.source.local.entities.products.ImageEntity

data class Image(
        val pid: String,
        val orderNumber: Int,
        val thumbnailHdUrl: Uri,
        val largeHdUrl: Uri
) {
    companion object {
        fun from(imageEntity: ImageEntity) = Image(
                imageEntity.pid,
                imageEntity.orderNumber,
                imageEntity.thumbnailHdUrl,
                imageEntity.largeHdUrl
        )

        fun from(productData: ProductData, imageData: ImageData) = Image(
                productData.pid,
                imageData.orderNumber,
                imageData.thumbnailHdUrl,
                imageData.largeHdUrl
        )
    }
}