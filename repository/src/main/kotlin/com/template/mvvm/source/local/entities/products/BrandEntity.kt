package com.template.mvvm.source.local.entities.products

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.net.Uri
import com.template.mvvm.domain.products.Brand

@Entity(tableName = "brands")
class BrandEntity
(
        @PrimaryKey
        @ColumnInfo(name = "brand_key")
        val key: String,
        @ColumnInfo(name = "name")
        val name: String,
        @ColumnInfo(name = "logo")
        val logo: Uri,
        @ColumnInfo(name = "shop")
        val shop: Uri
) {
    companion object {
        fun from(brand: Brand) = BrandEntity(brand.key, brand.name, brand.logo, brand.shop)
    }

    fun toBrand() = Brand.from(this)
}