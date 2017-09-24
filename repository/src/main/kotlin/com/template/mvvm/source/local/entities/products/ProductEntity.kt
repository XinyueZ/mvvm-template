package com.template.mvvm.source.local.entities.products

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.net.Uri
import com.template.mvvm.domain.products.Product

@Entity(tableName = "products")
class ProductEntity(
        @PrimaryKey
        @ColumnInfo(name = "title")
        val title: String,
        @ColumnInfo(name = "description")
        val description: String,
        @ColumnInfo(name = "thumbnail")
        val thumbnail: Uri,
        @ColumnInfo(name = "brandLogo")
        val brandLogo: Uri
) {
    companion object {
        fun from(product: Product) = ProductEntity(product.title, product.description, product.thumbnail, product.brandLogo)
    }
}