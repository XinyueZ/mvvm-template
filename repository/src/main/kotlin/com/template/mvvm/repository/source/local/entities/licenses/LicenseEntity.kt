package com.template.mvvm.repository.source.local.entities.licenses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.template.mvvm.repository.domain.licenses.License

@Entity(tableName = "licenses")
data class LicenseEntity(
        @PrimaryKey
        @ColumnInfo(name = "name")
        var name: String,
        @ColumnInfo(name = "description")
        var description: String
) {
    companion object {
        fun from(license: License) = LicenseEntity(license.name, license.description)
    }
}