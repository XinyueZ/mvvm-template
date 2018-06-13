package com.template.mvvm.repository.source.local.entities.products

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
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