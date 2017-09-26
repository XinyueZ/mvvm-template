package com.template.mvvm.domain.licenses

import com.template.mvvm.feeds.licenses.LibraryData
import com.template.mvvm.feeds.licenses.LicenseData
import com.template.mvvm.source.local.entities.licenses.LibraryEntity

data class Library(var name: String,
                   var owner: String?,
                   var copyright: String?,
                   var license: License) {
    companion object {
        fun from(libraryData: LibraryData, licenseData: LicenseData) = Library(libraryData.name, libraryData.owner, libraryData.copyright, License(licenseData.name, licenseData.description))
        fun from(libraryEntity: LibraryEntity) = Library(libraryEntity.name, libraryEntity.owner, libraryEntity.copyright, License(libraryEntity.license.name, libraryEntity.license.description))
    }
}
