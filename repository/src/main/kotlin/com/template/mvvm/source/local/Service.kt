package com.template.mvvm.source.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.template.mvvm.source.local.entities.licenses.LibraryEntity
import com.template.mvvm.source.local.entities.licenses.LicenseEntity
import com.template.mvvm.source.local.entities.products.ImageEntity
import com.template.mvvm.source.local.entities.products.ProductEntity

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

    @Query("delete from products where genders=:keyword")
    fun deleteProducts(keyword: String?)

    @Query("delete from images")
    fun deleteImages()

    @Query("select * from products order by create_time asc limit 10 offset :offset")
    fun getProductList(offset: Int): List<ProductEntity>

    @Query("select * from products where pid=:pid")
    fun getProduct(pid: Long): List<ProductEntity>

    @Query("select * from images where pid=:pid")
    fun getImages(pid: Long): List<ImageEntity>

    @Query("select * from products where genders=:keyword order by create_time asc limit 10 offset :offset")
    fun filterProductList(offset: Int, keyword: String?): List<ProductEntity>
}





