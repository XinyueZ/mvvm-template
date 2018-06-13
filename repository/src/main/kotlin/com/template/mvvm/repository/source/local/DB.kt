package com.template.mvvm.repository.source.local

import android.net.Uri
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.template.mvvm.repository.source.local.entities.licenses.LibraryEntity
import com.template.mvvm.repository.source.local.entities.licenses.LicenseEntity
import com.template.mvvm.repository.source.local.entities.products.ImageEntity
import com.template.mvvm.repository.source.local.entities.products.ProductCategoryEntity
import com.template.mvvm.repository.source.local.entities.products.ProductEntity

@Database(
    entities = [(LicenseEntity::class), (LibraryEntity::class), (ProductEntity::class), (ImageEntity::class), (ProductCategoryEntity::class)],
    version = 3
)
@TypeConverters(FieldConverter::class)
abstract class DB : RoomDatabase() {

    abstract fun licensesLibrariesDao(): LicensesLibrariesDao
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        lateinit var INSTANCE: DB
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
