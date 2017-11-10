package com.template.mvvm.source.local

import android.support.v7.util.DiffUtil
import android.support.v7.util.ListUpdateCallback
import android.text.TextUtils
import com.template.mvvm.LL
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Brand
import com.template.mvvm.domain.products.Product
import com.template.mvvm.source.local.entities.products.BrandEntity
import com.template.mvvm.source.local.entities.products.ProductEntity
import io.reactivex.Single

class ProductsLocal : ProductsDataSource {

    override fun getAllProducts(localOnly: Boolean) = DB.INSTANCE.productDao()
            .getProductList()
            .flatMap({
                val v: List<Product> = (mutableListOf<Product>()).apply {
                    it.forEach { this.add(it.toProduct()) }
                    LL.d("products loaded from db")
                }
                Single.just(v)
            })

    override fun filterProduct(keyword: String, localOnly: Boolean) = DB.INSTANCE.productDao()
            .filterProductList(keyword)
            .flatMap({
                val v: List<Product> = (mutableListOf<Product>()).apply {
                    it.forEach { this.add(it.toProduct()) }
                    LL.d("filtered $keyword products and loaded from db")
                }
                Single.just(v)
            })

    override fun getAllBrands(localOnly: Boolean) = DB.INSTANCE.productDao()
            .getBrandList()
            .flatMap({
                val v: List<Brand> = (mutableListOf<Brand>()).apply {
                    it.forEach { this.add(it.toBrand()) }
                    LL.d("brands loaded from db")
                }
                Single.just(v)
            })

    override fun saveProducts(source: List<Product>) = source.apply {
        DB.INSTANCE.productDao().apply {
            forEach {
                insertProduct(ProductEntity.from(it))
                insertBrand(BrandEntity.from(it.brand))
            }
        }
        LL.w("products write to db")
    }

    override fun saveBrands(source: List<Brand>) = source.apply {
        DB.INSTANCE.productDao().apply {
            mutableListOf<Brand>().apply {
                getBrandListDirectly().forEach { this.add(it.toBrand()) }
                val diffResult = DiffUtil.calculateDiff(BrandsDiffCallback(this, source))
                diffResult.dispatchUpdatesTo(BrandListUpdateCallback(this))
                source.forEach { insertBrand(BrandEntity.from(it)) }
                LL.w("brands write to db")
            }
        }
    }
}

class BrandsDiffCallback(private val oldList: List<Brand>, private val newList: List<Brand>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = TextUtils.equals(oldList[oldItemPosition].key, newList[newItemPosition].key)

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = TextUtils.equals(oldList[oldItemPosition].key, newList[newItemPosition].key)
}

class BrandListUpdateCallback(private val oldList: List<Brand>) : ListUpdateCallback {
    override fun onRemoved(position: Int, count: Int) {
        val topToDel = position + count - 1
        for (i in position..topToDel) {
            DB.INSTANCE.productDao().apply {
                val brand = oldList[i]
                getBrandedProductList(brand.key).takeIf { it.isEmpty() }?.let {
                    LL.d("[brand: ${brand.key}] onRemoved at $position, total: $count")
                    deleteBrand(BrandEntity.from(brand))
                } ?: kotlin.run {
                    LL.d("[brand: ${brand.key}] has products and won't be deleted.")
                }
            }
        }
    }

    override fun onInserted(position: Int, count: Int) {
        LL.d("brands onInserted: $position, $count")
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        LL.d("brands onChanged: $position, $count")
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        LL.d("brands onMoved: $fromPosition, $toPosition")
    }
}