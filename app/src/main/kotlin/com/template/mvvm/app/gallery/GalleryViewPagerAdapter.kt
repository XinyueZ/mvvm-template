package com.template.mvvm.app.gallery

import android.databinding.ObservableList
import android.net.Uri
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class GalleryViewPagerAdapter private constructor(fm: FragmentManager, private val uris: List<Uri> = emptyList()) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int) = GalleryImageFragment.newInstance(uris[position])

    override fun getCount() = uris.size

    companion object {
        @JvmStatic
        fun build(fm: FragmentManager, uris: ObservableList<Uri>) = GalleryViewPagerAdapter(fm, uris.toList())
    }
}