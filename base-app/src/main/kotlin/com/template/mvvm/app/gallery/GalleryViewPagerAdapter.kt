package com.template.mvvm.app.gallery

import androidx.databinding.ObservableList
import android.net.Uri
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class GalleryViewPagerAdapter private constructor(fm: androidx.fragment.app.FragmentManager, private val uris: List<Uri> = emptyList()) : androidx.fragment.app.FragmentPagerAdapter(fm) {

    override fun getItem(position: Int) = GalleryImageFragment.newInstance(uris[position])

    override fun getCount() = uris.size

    companion object {
        @JvmStatic
        fun build(fm: androidx.fragment.app.FragmentManager, uris: ObservableList<Uri>) = GalleryViewPagerAdapter(fm, uris.toList())
    }
}