package com.template.mvvm.repository.source.local.entities.products

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.template.mvvm.repository.domain.products.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    @ColumnInfo(name = "pid")
    val pid: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "filter")
    val filter: List<String>,
    @ColumnInfo(name = "create_time")
    val createTime: Long
) {
    companion object {
        fun from(product: Product) = ProductEntity(
            product.pid,
            product.title,
            product.description,
            product.filter,
            System.currentTimeMillis()
        )
    }

}