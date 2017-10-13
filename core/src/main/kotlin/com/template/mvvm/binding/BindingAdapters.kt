package com.template.mvvm.binding

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
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
import com.template.mvvm.arch.recycler.MvvmListAdapter

@BindingAdapter(value = *arrayOf("itemList", "itemLayout", "vmItemLayout", "layout"), requireAll = true)
fun RecyclerView.setUpBindingChris(
        itemList: LiveData<PagedList<ViewModel>>?,
        @LayoutRes itemLayout: Int,
        vmItemLayout: Int,
        layoutManager: String
) {
    if (adapter == null) {
        this.layoutManager = if (TextUtils.equals(layoutManager, "linear"))
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        else {
            val sp = (layoutManager.split("-"))[1].toInt()
            GridLayoutManager(context, sp)
        }
        val mvvmListAdapter = MvvmListAdapter(itemLayout, vmItemLayout)
        adapter = mvvmListAdapter
    }
    itemList?.observe(context as FragmentActivity, Observer((adapter as MvvmListAdapter)::setList))

}

@BindingAdapter(value = *arrayOf("width", "height"), requireAll = true)
fun View.viewSizeChris(width: Int, height: Int) {
    layoutParams.width = width
    layoutParams.height = height
}

@BindingAdapter("stopLoading")
fun SwipeRefreshLayout.stopLoadingChris(stopLoading: Boolean) {
    isRefreshing = !stopLoading
}

@BindingAdapter("reload")
fun SwipeRefreshLayout.reloadChris(l: OnReloadListener) {
    setOnRefreshListener {
        l.onReload()
    }
}

@BindingAdapter(value = *arrayOf("remoteImageUri", "placeholderRes", "errorDrawableRes"), requireAll = false)
fun View.remoteImageUriChris(uri: Uri?, @DrawableRes placeholderRes: Int, @DrawableRes errorDrawableRes: Int) {
    uri?.let {
        if (this is ImageView) {
            remoteImageUrisChris(arrayOf(uri), placeholderRes, errorDrawableRes)
        }
    }
}

@BindingAdapter(value = *arrayOf("remoteImageUris", "placeholderRes", "errorDrawableRes"), requireAll = false)
fun ImageView.remoteImageUrisChris(imageUris: Array<Uri>?, @DrawableRes placeholderRes: Int, @DrawableRes errorDrawableRes: Int) {
    when (imageUris == null || imageUris.isEmpty()) {
        true -> {
            when (errorDrawableRes > 0) {
                true -> setImageResource(errorDrawableRes)
                false -> setImageBitmap(null)
            }
            return
        }
    }

    imageUris?.let {
        val error: Drawable? = when (errorDrawableRes <= 0) {
            true -> null
            else -> AppCompatResources.getDrawable(context, errorDrawableRes)
        }

        val placeholder: Drawable? = when (placeholderRes <= 0) {
            true -> null
            else -> AppCompatResources.getDrawable(context, placeholderRes)
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
                        loadImageChris(imageUris[pos], placeholder, error, this)
                        return true
                    }
                }
                return false
            }
        }

        loadImageChris(imageUris[pos], placeholder, error, listener)
    }
}

private fun ImageView.loadImageChris(uri: Uri, placeholder: Drawable?, errorDrawable: Drawable?, listener: RequestListener<Bitmap>) {
    GlideApp.with(this)
            .asBitmap()
            .load(uri)
            .format(DecodeFormat.PREFER_RGB_565)
            .placeholder(placeholder)
            .error(errorDrawable)
            .dontAnimate()
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .listener(listener)
            .into(this)
}

@BindingAdapter("goBack")
fun View.goBackChris(goBack: Boolean) {
    if (goBack) ActivityCompat.finishAfterTransition(context as Activity)
}

@BindingAdapter("dataLoaded")
fun View.dataLoadedChris(loaded: Boolean) {
    visibility = if (loaded) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("command")
fun NavigationView.commandChris(l: OnCommandListener) {
    setNavigationItemSelectedListener {
        it.isChecked = true
        l.onCommand(it.itemId)

        true
    }
}

@BindingAdapter("command")
fun Toolbar.commandChris(l: OnCommandListener) {
    setNavigationOnClickListener { l.onCommand(R.id.action_app_bar_indicator) }
}

@BindingAdapter("command")
fun BottomNavigationView.commandChris(l: OnCommandListener) {
    disableShiftMode()
    setOnNavigationItemSelectedListener {
        l.onCommand(it.itemId)

        true
    }
}

@BindingAdapter(value = *arrayOf("command", "vm"), requireAll = true)
fun View.commandChris(l: OnItemCommandListener, vm: ViewModel) {
    setOnClickListener {
        l.onCommand(vm)
    }
}

//
// Some view-ext, helpers
//
@SuppressLint("RestrictedApi")
fun BottomNavigationView.disableShiftMode() {
    val menuView = this.getChildAt(0) as BottomNavigationMenuView
    try {
        val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
        shiftingMode.isAccessible = true
        shiftingMode.setBoolean(menuView, false)
        shiftingMode.isAccessible = false
        for (i in 0 until menuView.childCount) {
            val item = menuView.getChildAt(i) as BottomNavigationItemView
            item.setShiftingMode(false)
            item.setChecked(item.itemData.isChecked)
        }
    } catch (e: NoSuchFieldException) {
        LL.e("Unable to get shift mode field", e)
    } catch (e: IllegalAccessException) {
        LL.e("Unable to change value of shift mode", e)
    }
}

