package com.template.mvvm.source.local.entities.licenses

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "libraries",
        foreignKeys = arrayOf(
                ForeignKey(entity = LicenseEntity::class, parentColumns = arrayOf("name"), childColumns = arrayOf("licenseName"))
        ))
class LibraryEntity(
        @PrimaryKey
        @ColumnInfo(name = "name")
        var name: String,
        @ColumnInfo(name = "owner")
        var owner: String,
        @ColumnInfo(name = "copyright")
        var copyright: String,
        @ColumnInfo(name = "licenseName")
        var licenseName: String
)
