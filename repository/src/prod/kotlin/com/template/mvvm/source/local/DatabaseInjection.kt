package com.template.mvvm.source.local

import android.arch.persistence.room.Room
import android.content.Context

internal object DatabaseInjection {
    fun provideDatabase(context: Context) = Room.databaseBuilder(context.applicationContext, DB::class.java, "mvvm.db").fallbackToDestructiveMigration().build()

}