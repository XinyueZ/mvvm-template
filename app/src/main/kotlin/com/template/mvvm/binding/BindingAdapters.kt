package com.template.mvvm.binding

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.annotation.DrawableRes
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.template.mvvm.GlideApp
import com.template.mvvm.LL
import com.template.mvvm.R

@BindingAdapter(value = *arrayOf("width", "height"), requireAll = true)
fun viewSizePlaceHolding(view: View, width: Int, height: Int) {
    view.layoutParams.width = width
    view.layoutParams.height = height
}

@BindingAdapter("stopLoading")
fun stopLoadingPlaceHolding(view: SwipeRefreshLayout, stopLoading: Boolean) {
    view.isRefreshing = !stopLoading
}

@BindingAdapter("reload")
fun reloadPlaceHolding(view: SwipeRefreshLayout, l: OnReloadListener) {
    view.setOnRefreshListener {
        l.onReload()
    }
}

@BindingAdapter(value = *arrayOf("remoteImageUri", "placeholderRes", "errorDrawableRes"), requireAll = false)
fun remoteImageUriPlaceHolding(view: View, uri: Uri?, @DrawableRes placeholderRes: Int, @DrawableRes errorDrawableRes: Int) {
    uri?.let {
        if (view is ImageView) {
            remoteImageUrisPlaceHolding(view, arrayOf(uri), placeholderRes, errorDrawableRes)
        }
    }
}

@BindingAdapter(value = *arrayOf("remoteImageUris", "placeholderRes", "errorDrawableRes"), requireAll = false)
fun remoteImageUrisPlaceHolding(view: ImageView, imageUris: Array<Uri>?, @DrawableRes placeholderRes: Int, @DrawableRes errorDrawableRes: Int) {
    when (imageUris == null || imageUris.isEmpty()) {
        true -> {
            when (errorDrawableRes > 0) {
                true -> view.setImageResource(errorDrawableRes)
                false -> view.setImageBitmap(null)
            }
            return
        }
    }

    imageUris?.let {
        val error: Drawable? = when (errorDrawableRes <= 0) {
            true -> null
            else -> AppCompatResources.getDrawable(view.context, errorDrawableRes)
        }

        val placeholder: Drawable? = when (placeholderRes <= 0) {
            true -> null
            else -> AppCompatResources.getDrawable(view.context, placeholderRes)
        }

        var pos = 0
        val listener = object : RequestListener<Bitmap> {
            override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: com.bumptech.glide.load.DataSource?, isFirstResource: Boolean): Boolean {
                // let glide set the image by returning false
                return false
            }

            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                // load next one if possible
                when (pos < (imageUris.size - 1)) {
                    true -> {
                        pos++
                        loadImagePlaceHolding(view, imageUris[pos], placeholder, error, this)
                        return true
                    }
                }
                return false
            }
        }

        loadImagePlaceHolding(view, imageUris[pos], placeholder, error, listener)
    }
}

private fun loadImagePlaceHolding(view: ImageView, uri: Uri, placeholder: Drawable?, errorDrawable: Drawable?, listener: RequestListener<Bitmap>) {
    GlideApp.with(view)
            .asBitmap()
            .load(uri)
            .format(DecodeFormat.PREFER_RGB_565)
            .placeholder(placeholder)
            .error(errorDrawable)
            .dontAnimate()
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .listener(listener)
            .into(view)
}

@BindingAdapter("goBack")
fun goBackPlaceHolding(view: View, goBack: Boolean) {
    if (goBack) ActivityCompat.finishAfterTransition(view.context as Activity)
}

@BindingAdapter("dataLoaded")
fun dataLoadedPlaceHolding(view: View, loaded: Boolean) {
    view.visibility = if (loaded) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("command")
fun commandPlaceHolding(view: NavigationView, l: OnCommandListener) {
    view.setNavigationItemSelectedListener {
        it.isChecked = true
        l.onCommand(it.itemId)

        true
    }
}

@BindingAdapter("command")
fun commandPlaceHolding(toolbar: Toolbar, l: OnCommandListener) {
    toolbar.setNavigationOnClickListener { l.onCommand(R.id.action_app_bar_indicator) }
}

@BindingAdapter("command")
fun commandPlaceHolding(view: BottomNavigationView, l: OnCommandListener) {
    view.setOnNavigationItemSelectedListener {
        l.onCommand(it.itemId)

        true
    }
}

@BindingAdapter(value = *arrayOf("command", "vm"), requireAll = true)
fun commandPlaceHolding(view: View, l: OnItemCommandListener, vm: ViewModel) {
    view.setOnClickListener {
        l.onCommand(vm)
    }
}

