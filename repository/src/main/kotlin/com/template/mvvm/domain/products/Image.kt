package com.template.mvvm.domain.products

import android.net.Uri
import com.template.mvvm.feeds.products.ProductData
import com.template.mvvm.feeds.products.SizeType
import com.template.mvvm.source.local.entities.products.ImageEntity

data class Image(
        val pid: String,
        val size: String,
        val uri: Uri
) {
    companion object {
        fun from(imageEntity: ImageEntity) = Image(
                imageEntity.pid,
                imageEntity.size,
                imageEntity.uri
        )

        fun from(productData: ProductData, sizeType: SizeType) = Image(
                productData.pid,
                sizeType.sizeName,
                Uri.parse(sizeType.url)
        )
    }
}