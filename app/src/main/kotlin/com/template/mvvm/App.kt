package com.template.mvvm

import android.support.multidex.MultiDexApplication
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

class App : MultiDexApplication()


@GlideModule
class ExAppGlideModule : AppGlideModule()