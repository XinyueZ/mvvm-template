package com.template.mvvm.source.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.TypeConverters
import android.net.Uri
import com.template.mvvm.source.local.entities.licenses.LibraryEntity
import com.template.mvvm.source.local.entities.licenses.LicenseEntity
import com.template.mvvm.source.local.entities.products.BrandEntity
import com.template.mvvm.source.local.entities.products.ImageEntity
import com.template.mvvm.source.local.entities.products.ProductEntity

@Database(entities = arrayOf(LicenseEntity::class, LibraryEntity::class, ProductEntity::class, BrandEntity::class, ImageEntity::class), version = 1)
@TypeConverters(FieldConverter::class)
abstract class DB : RoomDatabase() {

    abstract fun licensesLibrariesDao(): LicensesLibrariesDao
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile lateinit var INSTANCE: DB
    }
}

class FieldConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromUri(uri: Uri) = uri.toString()

        @TypeConverter
        @JvmStatic
        fun toUri(uriStr: String) = Uri.parse(uriStr)

        @TypeConverter
        @JvmStatic
        fun fromStringList(list: List<String>) = list.joinToString("|")

        @TypeConverter
        @JvmStatic
        fun toStringList(uriStr: String) = uriStr.split("|").toList()
    }
}
