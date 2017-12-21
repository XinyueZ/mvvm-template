package com.template.mvvm.repository.contract

import android.content.Context
import com.template.mvvm.repository.domain.licenses.Library
import kotlinx.coroutines.experimental.channels.produce
import kotlin.coroutines.experimental.CoroutineContext

interface LicensesDataSource : DataSource {
    suspend fun getAllLibraries(coroutineContext: CoroutineContext, localOnly: Boolean = true) = produce<List<Library>?>(coroutineContext) {}
    suspend fun saveLibraries(coroutineContext: CoroutineContext, source: List<Library>) = produce<Unit>(coroutineContext) {}
    suspend fun getLicense(context: Context, coroutineContext: CoroutineContext, library: Library, localOnly: Boolean = true) = produce<String>(coroutineContext) {}
}