package com.template.mvvm.source.local

import android.support.v7.util.DiffUtil
import android.support.v7.util.ListUpdateCallback
import android.text.TextUtils
import com.template.mvvm.LL
import com.template.mvvm.contract.ProductsDataSource
import com.template.mvvm.domain.products.Brand
import com.template.mvvm.domain.products.Product
import com.template.mvvm.source.local.entities.products.BrandEntity
import com.template.mvvm.source.local.entities.products.ImageEntity
import com.template.mvvm.source.local.entities.products.ProductEntity
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.produce

class ProductsLocal : ProductsDataSource {

    override suspend fun getAllProducts(job: Job, localOnly: Boolean) = produce<List<Product>>(job) {
        mutableListOf<Product>().apply {
            with(DB.INSTANCE.productDao()) {
                getProductList().forEach {
                    val imageList = getImages(it.pid)
                    this@apply.add(Product.from(it, imageList))
                }
            }
            LL.d("products loaded from db")
            send(this)
        }
    }

    override suspend fun filterProduct(job: Job, keyword: String, localOnly: Boolean) = produce<List<Product>>(job) {
        mutableListOf<Product>().apply {
            with(DB.INSTANCE.productDao()) {
                filterProductList(keyword).forEach {
                    val imageList = getImages(it.pid)
                    this@apply.add(Product.from(it, imageList))
                }
            }
            LL.d("filtered $keyword products and loaded from db")
            send(this)
        }
    }

    override suspend fun getAllBrands(job: Job, localOnly: Boolean) = produce<List<Brand>>(job) {
        mutableListOf<Brand>().apply {
            DB.INSTANCE.productDao().getBrandList().forEach {
                this.add(it.toBrand())
            }
            LL.d("brands loaded from db")
            send(this)
        }
    }

    override suspend fun saveProducts(job: Job, source: List<Product>) = produce<Byte>(job) {
        DB.INSTANCE.productDao().apply {
            source.forEach {
                insertBrand(BrandEntity.from(it.brand))
                insertImage(ImageEntity.from(it.pictures.first()))
                insertProduct(ProductEntity.from(it))
            }
            send(1)
            LL.w("products write to db")
        }
    }

    override suspend fun saveBrands(job: Job, source: List<Brand>) = produce<Byte>(job) {
        DB.INSTANCE.productDao().apply {
            mutableListOf<Brand>().apply {
                getBrandList().forEach { this.add(it.toBrand()) }
                val diffResult = DiffUtil.calculateDiff(BrandsDiffCallback(this, source))
                diffResult.dispatchUpdatesTo(BrandListUpdateCallback(this))
                source.forEach { insertBrand(BrandEntity.from(it)) }
                send(1)
                LL.w("brands write to db")
            }
        }
    }

    override suspend fun savePictures(job: Job, source: List<Product>) = produce<Byte>(job) {
        DB.INSTANCE.productDao().apply {
            source.forEach {
                it.pictures.forEach {
                    insertImage(ImageEntity.from(it))
                }
            }
            send(1)
            LL.w("pictures(images) write to db")
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