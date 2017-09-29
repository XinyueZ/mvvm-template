package com.template.mvvm.domain.products

import android.net.Uri
import android.text.TextUtils
import com.template.mvvm.feeds.products.BrandData
import com.template.mvvm.source.local.entities.products.BrandEntity

data class Brand
(
        val key: String = "",
        val name: String = "",
        val logo: Uri = Uri.EMPTY,
        val shop: Uri = Uri.EMPTY
) {
    companion object {
        val EMPTY = Brand()
        fun from(brandEntity: BrandEntity) = Brand(brandEntity.key,
                brandEntity.name,
                if (TextUtils.isEmpty(brandEntity.logo.toString())) Uri.EMPTY else brandEntity.logo,
                if (TextUtils.isEmpty(brandEntity.shop.toString())) Uri.EMPTY else brandEntity.shop)

        fun from(brandData: BrandData) = Brand(
                brandData.key,
                brandData.name,
                brandData.logo ?: (brandData.logoDefault ?: Uri.EMPTY),
                brandData.shop ?: Uri.EMPTY)
    }
}