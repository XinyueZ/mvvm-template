package com.template.mvvm.domain.licenses

import com.template.mvvm.data.feeds.licenses.LibraryData
import com.template.mvvm.data.feeds.licenses.LicenseData

data class Library(var name: String?,
                   var owner: String?,
                   var copyright: String?,
                   var license: License) {
    companion object {
        fun from(libraryData: LibraryData, licenseData: LicenseData) = Library(libraryData.name, libraryData.owner, libraryData.copyright, License(licenseData.name, licenseData.description))
    }
}
