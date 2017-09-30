package com.template.mvvm.source.local.entities.products

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.net.Uri
import com.template.mvvm.domain.products.Product

@Entity(tableName = "products")
class ProductEntity(
        @PrimaryKey
        @ColumnInfo(name = "product_id")
        val pid: String,
        @ColumnInfo(name = "title")
        val title: String,
        @ColumnInfo(name = "description")
        val description: String,
        @ColumnInfo(name = "thumbnail")
        val thumbnail: Uri,
        @Embedded(prefix = "brand_")
        val brand: BrandEntity,
        @ColumnInfo(name = "genders")
        val genders: List<String>,
        @ColumnInfo(name = "ageGroups")
        val ageGroups: List<String>,
        @ColumnInfo(name = "categoryKeys")
        val categoryKeys: List<String>
) {
    companion object {
        fun from(product: Product) = ProductEntity(
                product.pid,
                product.title,
                product.description,
                product.thumbnail,
                BrandEntity.from(product.brand),
                product.genders,
                product.ageGroups,
                product.categoryKeys
        )
    }

    fun toProduct() = Product.from(this)
}