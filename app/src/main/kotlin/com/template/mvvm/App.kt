package com.template.mvvm

import com.template.mvvm.models.BrandItemViewModel
import com.template.mvvm.models.ProductItemViewModel
import com.template.mvvm.models.SoftwareLicenseItemViewModel
import me.tatarka.bindingcollectionadapter2.ItemBinding

class App : CoreModule() {
    override fun onCoreStart() {
        super.onCoreStart()

        with(Injection.getInstance(this)) {
            addItemBindingOf(BrandItemViewModel::class.java, ItemBinding.of<BrandItemViewModel>(BR.vm, R.layout.item_brand))
            addItemBindingOf(ProductItemViewModel::class.java, ItemBinding.of<ProductItemViewModel>(BR.vm, R.layout.item_product))
            addItemBindingOf(SoftwareLicenseItemViewModel::class.java, ItemBinding.of<SoftwareLicenseItemViewModel>(BR.vm, R.layout.item_software_license))
        }
    }
}

