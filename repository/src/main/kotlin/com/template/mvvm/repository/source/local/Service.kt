package com.template.mvvm.repository.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.template.mvvm.repository.source.local.entities.licenses.LibraryEntity
import com.template.mvvm.repository.source.local.entities.licenses.LicenseEntity
import com.template.mvvm.repository.source.local.entities.products.ImageEntity
import com.template.mvvm.repository.source.local.entities.products.ProductCategoryEntity
import com.template.mvvm.repository.source.local.entities.products.ProductEntity

@Dao
interface LicensesLibrariesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLicenses(licenseEntity: List<LicenseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLibraries(libraryEntity: List<LibraryEntity>)

    @Query("select * from libraries")
    fun getLibraryList(): List<LibraryEntity>

    @Query("select count(*) as total from libraries")
    fun getLibraryListCount(): List<Count>

    @Delete
    fun deleteLibrary(libraryEntity: LibraryEntity)

    class Count {
        var total: Int = 0
    }
}

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProducts(productEntity: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImages(imageEntity: List<ImageEntity>)

    @Query("delete from products")
    fun deleteProducts()

    @Query("delete from products where filter=:keyword")
    fun deleteProducts(keyword: String?)

    @Query("delete from images")
    fun deleteImages()

    @Query("delete from images where pid=:pid")
    fun deleteImages(pid: Long)

    @Query("select * from products where filter=:keyword")
    fun getProductList(keyword: String?): List<ProductEntity>

    @Query("select * from products order by create_time asc limit 10 offset :offset")
    fun getProductList(offset: Int): List<ProductEntity>

    @Query("select * from products where pid=:pid")
    fun getProduct(pid: Long): List<ProductEntity>

    @Query("select * from images where pid=:pid")
    fun getImages(pid: Long): List<ImageEntity>

    @Query("select * from images")
    fun getImages(): List<ImageEntity>

    @Query("select * from products where filter=:keyword order by create_time asc limit 10 offset :offset")
    fun filterProductList(offset: Int, keyword: String?): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProductCategories(productCategoryEntity: List<ProductCategoryEntity>)

    @Query("delete from product_categories")
    fun deleteProductCategories()

    @Query("select * from product_categories order by create_time asc limit 999999 offset :offset")
    fun getProductCategoryList(offset: Int): List<ProductCategoryEntity>
}





