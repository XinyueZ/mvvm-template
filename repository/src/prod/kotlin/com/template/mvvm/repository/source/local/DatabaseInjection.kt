package com.template.mvvm.repository.source.local

import android.content.Context
import androidx.room.Room

internal object DatabaseInjection {
    fun provideDatabase(context: Context) = Room.databaseBuilder(context.applicationContext, DB::class.java, "mvvm.db").fallbackToDestructiveMigration().build()

}