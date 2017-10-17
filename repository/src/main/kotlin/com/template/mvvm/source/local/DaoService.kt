package com.template.mvvm.source.local

import android.arch.persistence.room.*
import com.template.mvvm.source.local.entities.licenses.LibraryEntity
import com.template.mvvm.source.local.entities.licenses.LicenseEntity
import com.template.mvvm.source.local.entities.products.BrandEntity
import com.template.mvvm.source.local.entities.products.ImageEntity
import com.template.mvvm.source.local.entities.products.ProductEntity

@Dao
interface LicensesLibrariesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLicense(licenseEntity: LicenseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLibrary(libraryEntity: LibraryEntity)

    @Query("select * from libraries")
    fun getLibraryList(): List<LibraryEntity>

    @Query("select count(*) as total from libraries")
    fun getLibraryListCount(): List<Count>

    @Query("delete from libraries")
    fun deleteLibraries()

    @Delete
    fun deleteLibrary(libraryEntity: LibraryEntity)

    class Count {
        var total: Int = 0
    }
}

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(productEntity: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBrand(brandEntity: BrandEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImage(imageEntity: ImageEntity)

    @Query("select * from products")
    fun getProductList(): List<ProductEntity>

    @Query("select * from images where pid=:pid")
    fun getImages(pid: String): List<ImageEntity>

    @Query("select * from products where genders=:keyword")
    fun filterProductList(keyword: String?): List<ProductEntity>

    @Query("select * from brands")
    fun getBrandList(): List<BrandEntity>

    @Delete
    fun deleteBrand(brandEntity: BrandEntity)

    @Query("select * from products where brand_brand_key=:brandKey")
    fun getBrandedProductList(brandKey: String?): List<ProductEntity>
}





