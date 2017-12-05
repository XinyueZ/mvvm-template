package com.template.mvvm.source.local

import android.arch.persistence.room.Room
import android.content.Context

internal object DatabaseInjection {
    fun provideDatabase(context: Context) = Room.inMemoryDatabaseBuilder(context, DB::class.java).fallbackToDestructiveMigration().build()

}