package com.template.mvvm.repository.source.local.entities.licenses

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.template.mvvm.repository.domain.licenses.Library

@Entity(tableName = "libraries")
data class LibraryEntity(
        @PrimaryKey
        @ColumnInfo(name = "name")
        var name: String,
        @ColumnInfo(name = "owner")
        var owner: String?,
        @ColumnInfo(name = "copyright")
        var copyright: String?,
        @Embedded(prefix = "license_")
        var license: LicenseEntity

) {
    companion object {
        fun from(library: Library) = LibraryEntity(library.name, library.owner, library.copyright, LicenseEntity.from(library.license))
    }

    fun toLibrary() = Library.from(this)
}
