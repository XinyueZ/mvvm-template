package com.template.mvvm.source.local

import android.arch.persistence.room.*
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

    @Query("select * from products")
    fun getProductList(): List<ProductEntity>

    @Query("select * from products where pid=:pid")
    fun getProduct(pid: String): List<ProductEntity>

    @Query("select * from images where pid=:pid")
    fun getImages(pid: String): List<ImageEntity>

    @Query("select * from products where genders=:keyword")
    fun filterProductList(keyword: String?): List<ProductEntity>
}





