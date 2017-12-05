package com.template.mvvm.binding

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
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
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.CardView
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
import com.template.mvvm.ext.onClick
import com.template.mvvm.ext.onNavigationItemSelected
import com.template.mvvm.ext.onNavigationOnClick

@BindingAdapter(value = ["itemList", "itemLayout", "vmItemLayout", "layout", "onListItemBound", "add"], requireAll = false)
fun RecyclerView.bindingList(
        itemList: LiveData<List<ViewModel>>?,
        @LayoutRes itemLayout: Int,
        vmItemLayout: Int,
        layoutManager: String,
        onListItemBound: OnListItemBoundListener?,
        add: Boolean
) {
    if (adapter == null) {
        this.layoutManager = if (TextUtils.equals(layoutManager, "linear"))
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        else {
            val sp = (layoutManager.split("-"))[1].toInt()
            GridLayoutManager(context, sp)
        }
        adapter = MvvmListAdapter(itemLayout, vmItemLayout, onListItemBound).apply {
            (context as FragmentActivity).run {
                itemList?.let { liveData ->
                    liveData.removeObservers(this)
                    liveData.observe(this, Observer { updatedList ->
                        updatedList?.run {
                            if (add) add(this)
                            else update(this)
                        }
                    })
                }
            }
        }
    }
}

@BindingAdapter("delete")
fun RecyclerView.bindingDelete(deleteList: Boolean) {
    if (deleteList)
        (adapter as? MvvmListAdapter)?.delete()
}

@BindingAdapter(value = ["width", "height", "command", "vm"], requireAll = false)
fun CardView.setUpExtt(width: Int?, height: Int?, l: OnItemCommandListener?, vm: ViewModel?) {
    width?.let { layoutParams.width = it }
    height?.let { layoutParams.height = it }
    l?.let {
        onClick {
            vm?.let {
                l.onCommand(vm)
            }
        }
    }

}

@BindingAdapter("stopLoading", requireAll = false)
fun SwipeRefreshLayout.stopLoading(stopLoading: Boolean?) {
    stopLoading?.let { isRefreshing = !it }
}

@BindingAdapter("reload", requireAll = false)
fun SwipeRefreshLayout.reload(l: OnReloadListener?) {
    setOnRefreshListener {
        l?.onReload()
    }
}

@BindingAdapter("pagerAdapter", requireAll = false)
fun ViewPager.setImageList(adapter: PagerAdapter) {
    this.adapter = adapter
}

@BindingAdapter(value = ["remoteImageUri", "placeholderRes", "errorDrawableRes"], requireAll = false)
fun View.remoteImageUri(uri: Uri?, @DrawableRes placeholderRes: Int, @DrawableRes errorDrawableRes: Int) {
    uri?.let { (this as? ImageView)?.remoteImageUris(arrayOf(it), placeholderRes, errorDrawableRes) }
}

@BindingAdapter(value = ["remoteImageUris", "placeholderRes", "errorDrawableRes"], requireAll = false)
fun ImageView.remoteImageUris(imageUris: Array<Uri>?, @DrawableRes placeholderRes: Int, @DrawableRes errorDrawableRes: Int) {
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
                        loadImage(imageUris[pos], placeholder, error, this)
                        return true
                    }
                }
                return false
            }
        }

        loadImage(imageUris[pos], placeholder, error, listener)
    }
}

private fun ImageView.loadImage(uri: Uri, placeholder: Drawable?, errorDrawable: Drawable?, listener: RequestListener<Bitmap>) {
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

@BindingAdapter("goBack", requireAll = false)
fun View.goBack(goBack: Boolean?) {
    goBack?.let { if (it) ActivityCompat.finishAfterTransition(context as Activity) }
}

@BindingAdapter("dataLoaded", requireAll = false)
fun View.dataLoaded(loaded: Boolean?) {
    visibility = if (loaded != null && loaded) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("command", requireAll = false)
fun NavigationView.command(l: OnCommandListener?) {
    onNavigationItemSelected { l?.onCommand(it) }
}

@BindingAdapter("command", requireAll = false)
fun Toolbar.command(l: OnCommandListener?) {
    onNavigationOnClick { l?.onCommand(R.id.action_app_bar_indicator) }
}

@BindingAdapter("command", requireAll = false)
fun BottomNavigationView.command(l: OnCommandListener?) {
    disableShiftMode()
    onNavigationItemSelected { l?.onCommand(it) }
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

