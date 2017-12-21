package com.template.mvvm.repository.source.local.entities.licenses

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.template.mvvm.repository.domain.licenses.License

@Entity(tableName = "licenses")
class LicenseEntity(
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