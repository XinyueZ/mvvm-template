package com.template.mvvm.source.local

import android.arch.persistence.room.*
import com.template.mvvm.source.local.entities.licenses.LibraryEntity
import com.template.mvvm.source.local.entities.licenses.LicenseEntity
import com.template.mvvm.source.local.entities.products.BrandEntity
import com.template.mvvm.source.local.entities.products.ProductEntity
import io.reactivex.Single

@Dao
interface LicensesLibrariesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLicenses(licenseEntity: List<LicenseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLibraries(libraryEntity: List<LibraryEntity>)

    @Query("select * from licenses")
    fun getLicenseList(): Single<List<LicenseEntity>>

    @Query("select * from libraries")
    fun getLibraryList(): Single<List<LibraryEntity>>

    @Query("select * from libraries")
    fun getLibraryListDirectly(): List<LibraryEntity>

    @Query("select count(*) as total from libraries")
    fun getLibraryListCount(): Single<List<Count>>

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
    fun insertProducts(productEntity: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBrands(brandEntity: List<BrandEntity>)

    @Query("select * from products")
    fun getProductList(): Single<List<ProductEntity>>

    @Query("select * from products where genders=:keyword")
    fun filterProductList(keyword: String?): Single<List<ProductEntity>>

    @Query("select * from brands")
    fun getBrandList(): Single<List<BrandEntity>>

    @Query("select * from brands")
    fun getBrandListDirectly(): List<BrandEntity>

    @Delete
    fun deleteBrand(brandEntity: BrandEntity)

    @Query("select * from products where brand_brand_key=:brandKey")
    fun getBrandedProductList(brandKey: String?): List<ProductEntity>
}





