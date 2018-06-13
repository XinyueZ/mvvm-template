package com.template.mvvm.repository.source.local

import android.content.Context
import androidx.room.Room

internal object DatabaseInjection {
    fun provideDatabase(context: Context) = Room.inMemoryDatabaseBuilder(context, DB::class.java).fallbackToDestructiveMigration().build()

}