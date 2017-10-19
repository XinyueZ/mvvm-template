package com.qiaoqiao.views

import android.net.Uri
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class GalleryViewPagerAdapter(fm: FragmentManager, private val uris: List<Uri> = emptyList()) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int) = GalleryImageFragment.newInstance(uris[position])

    override fun getCount() = uris.size
}