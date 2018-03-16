package com.template.mvvm.repository.source.local.entities.products

import android.arch.persistence.room.Entity
import android.net.Uri
import com.template.mvvm.repository.domain.products.Image

@Entity(tableName = "images", primaryKeys = ["pid", "uri"])
data class ImageEntity(
        val pid: Long,
        val size: String,
        val uri: Uri
) {
    companion object {
        fun from(image: Image) = ImageEntity(
                image.pid,
                image.size,
                image.uri
        )
    }
}