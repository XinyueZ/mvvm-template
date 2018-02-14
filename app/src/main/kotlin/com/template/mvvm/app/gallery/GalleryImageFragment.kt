package com.template.mvvm.app.gallery

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ImageView.ScaleType.CENTER_CROP
import com.template.mvvm.app.binding.remoteImageUri

class GalleryImageFragment : Fragment() {

    companion object {
        val EXTRAS_IMAGE_URI = "image-uri"
        fun newInstance(imageUri: Uri): GalleryImageFragment {
            val args = Bundle(1)
            args.putParcelable(EXTRAS_IMAGE_URI, imageUri)
            return GalleryImageFragment().apply { arguments = args }
        }
    }

    private val imageView: ImageView by lazy {
        ImageView(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = imageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            imageView.apply {
                remoteImageUri(
                    it[EXTRAS_IMAGE_URI] as Uri,
                    android.R.drawable.ic_menu_gallery,
                    android.R.drawable.ic_menu_gallery
                )
                scaleType = CENTER_CROP
            }
        }
    }
}