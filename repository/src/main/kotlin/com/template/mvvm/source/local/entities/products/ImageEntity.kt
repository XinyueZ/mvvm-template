package com.template.mvvm.source.local.entities.products

import android.arch.persistence.room.Entity
import android.net.Uri
import com.template.mvvm.domain.products.Image

@Entity(tableName = "images", primaryKeys = arrayOf("pid", "orderNumber"))
class ImageEntity(
        val pid: String,
        val orderNumber: Int,
        val thumbnailHdUrl: Uri,
        val largeHdUrl: Uri
) {
    companion object {
        fun from(image: Image) = ImageEntity(
                image.pid,
                image.orderNumber,
                image.thumbnailHdUrl,
                image.largeHdUrl
        )
    }
}