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

    @Query("SELECT * FROM licenses")
    fun getLicenseList(): Flowable<List<LicenseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLibrary(libraryEntity: LibraryEntity)

    @Query("SELECT * FROM libraries")
    fun getLibraryList(): Flowable<List<LibraryEntity>>


}

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(productEntity: ProductEntity)
}





