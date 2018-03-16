package com.template.mvvm.repository.source.local.entities.products

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.template.mvvm.repository.domain.products.ProductCategory

@Entity(tableName = "product_categories")
data class ProductCategoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "cid")
    val cid: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "create_time")
    val createTime: Long
) {
    companion object {
        fun from(productCategory: ProductCategory) = ProductCategoryEntity(
            productCategory.cid,
            productCategory.name,
            System.currentTimeMillis()
        )
    }

}