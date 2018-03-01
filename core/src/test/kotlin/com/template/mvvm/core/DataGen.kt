package com.template.mvvm.core

import android.net.Uri
import com.template.mvvm.repository.domain.licenses.Library
import com.template.mvvm.repository.domain.licenses.License
import com.template.mvvm.repository.domain.products.Image
import com.template.mvvm.repository.domain.products.Product
import com.template.mvvm.repository.domain.products.ProductDetail
import io.kotlintest.properties.Gen
import java.lang.StringBuilder

fun generateLicenseList(size: Int) = object :
    Gen<List<Library>> {
    override fun generate(): List<Library> = mutableListOf<Library>().apply {
        for (i in 0 until size) {
            add(
                Library(
                    Gen.string().generate(),
                    Gen.string().generate(),
                    Gen.string().generate(),
                    License(
                        Gen.string().generate(),
                        Gen.string().generate()
                    )
                )
            )
        }
    }
}

fun generateProductList(size: Int) = object :
    Gen<List<Product>> {
    override fun generate(): List<Product> = mutableListOf<Product>().apply {
        for (i in 0 until size) {
            add(Product(Gen.positiveIntegers().generate().toLong()))
        }
    }
}

fun generateProductDetail(pid: Long, withPictures: Boolean = true) = object :
    Gen<ProductDetail> {
    override fun generate(): ProductDetail {
        return ProductDetail(
            pid,
            Gen.string().generate(),
            Gen.string().generate(),
            if (!withPictures) emptyList() else generateProductImages(pid).generate()
        )
    }
}

fun generateProductImages(pid: Long) = object :
    Gen<List<Image>> {
    override fun generate() =
        mutableListOf(generateProductImage(pid).generate()).apply {
            (1 until Gen.choose(5, 10).generate()).asSequence()
                .forEach { this += generateProductImage(pid).generate() }
        }
}

fun generateProductImage(pid: Long) = object :
    Gen<Image> {
    override fun generate() = Image(
        pid,
        generateProductImageSize().generate(),
        Uri.parse(
            StringBuilder().append("http://")
                .append("www.")
                .append("test")
                .append(Gen.string().generate())
                .append(".com")
                .toString()
        )
    )
}

fun generateProductImageSize() = object :
    Gen<String> {
    override fun generate() = Gen.oneOf(
        listOf(
            "Small",
            "XLarge",
            "Medium",
            "Large",
            "Original",
            "IPhone",
            "IPhoneSmall"
        )
    ).generate()
}