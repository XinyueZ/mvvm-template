package com.template.mvvm.source.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.template.mvvm.source.local.entities.licenses.LibraryEntity
import com.template.mvvm.source.local.entities.licenses.LicenseEntity
import com.template.mvvm.source.local.entities.products.ProductEntity
import io.reactivex.Flowable

@Dao
interface LicensesLibrariesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLicense(licenseEntity: LicenseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLibrary(libraryEntity: LibraryEntity)

    @Query("select * from licenses")
    fun getLicenseList(): Flowable<List<LicenseEntity>>

    @Query("select * from libraries")
    fun getLibraryList(): Flowable<List<LibraryEntity>>

    @Query("select count(*) as total from libraries")
    fun getLibraryListCount(): Flowable<List<Count>>

    class Count {
        var total: Int = 0
    }
}

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(productEntity: ProductEntity)

    @Query("select * from products")
    fun getProductList(): Flowable<List<ProductEntity>>
}





