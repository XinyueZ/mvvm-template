package com.template.mvvm.repository.source.local

import android.content.Context
import android.text.TextUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.google.gson.Gson
import com.template.mvvm.base.ext.android.content.res.read
import com.template.mvvm.base.utils.LL
import com.template.mvvm.repository.contract.LicensesDataSource
import com.template.mvvm.repository.domain.licenses.Library
import com.template.mvvm.repository.source.local.entities.licenses.LibraryEntity
import com.template.mvvm.repository.source.local.entities.licenses.LicenseEntity
import com.template.mvvm.repository.source.remote.feeds.licenses.LicensesData
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.produce
import java.io.InputStreamReader
import kotlin.coroutines.experimental.CoroutineContext

class LicensesLocal(private val context: Context) : LicensesDataSource {

    companion object {
        private val LICENCES_LIST_JSON = "licenses-list.json"
        private val LICENCES_BOX = "licenses-box"
        private val COPYRIGHT_HOLDERS = "<copyright holders>"
        private val YEAR = "<year>"
        private val LICENCE_BOX_LOCATION_FORMAT = "%s/%s.txt"
    }

    private val gson = Gson()

    override suspend fun getAllLibraries(coroutineContext: CoroutineContext, localOnly: Boolean): ReceiveChannel<List<Library>> {
        DB.INSTANCE.licensesLibrariesDao()
                .getLibraryListCount().takeIf { it.isNotEmpty() }?.let {
            return when (it[0].total == 0) {
                true -> loadLicensesFromAsset(coroutineContext)
                else -> loadLicensesFromDB(coroutineContext)
            }
        } ?: kotlin.run {
            return produce(coroutineContext) {
                send(emptyList())
            }
        }
    }

    private suspend fun loadLicensesFromDB(coroutineContext: CoroutineContext) = produce(coroutineContext) {
        LL.d("licenses loaded from db")
        send(DB.INSTANCE.licensesLibrariesDao()
                .getLibraryList().map { it.toLibrary() })
    }

    private suspend fun loadLicensesFromAsset(coroutineContext: CoroutineContext) = produce(coroutineContext) {
        val licensesData = gson.fromJson(InputStreamReader(this@LicensesLocal.context.assets
                .open(LICENCES_LIST_JSON)), LicensesData::class.java)
        val v: List<Library> = mutableListOf<Library>().apply {
            licensesData.licenses.forEach({ licenseData ->
                licenseData.libraries.forEach({ libraryData ->
                    this@apply.add(Library.from(libraryData, licenseData))
                })
            })
        }
        LL.d("licenses loaded from asset")
        send(v)
    }

    override suspend fun saveLibraries(coroutineContext: CoroutineContext, source: List<Library>) = produce(coroutineContext) {
        DB.INSTANCE.licensesLibrariesDao().apply {
            mutableListOf<Library>().apply {
                getLibraryList().forEach { this.add(it.toLibrary()) }
                val diffResult = DiffUtil.calculateDiff(DifferentCallback(this, source))
                diffResult.dispatchUpdatesTo(DifferentListUpdateCallback(this))

                insertLibraries(source.map {
                    LibraryEntity.from(it)
                })
                insertLicenses(source.map {
                    LicenseEntity.from(it.license)
                })
                send(Unit)
                LL.w("licenses write to db")
            }
        }
    }

    override suspend fun getLicense(context: Context, coroutineContext: CoroutineContext, library: Library, localOnly: Boolean) = produce(coroutineContext) {
        val source = context.assets.read(String.format(LICENCE_BOX_LOCATION_FORMAT, LICENCES_BOX, library.license.name))
        send(source.replace(YEAR, library.copyright ?: "")
                .replace(COPYRIGHT_HOLDERS, library.owner ?: "")
        )
        LL.w("read licenses detail from asset")
    }
}

class DifferentCallback(private val oldList: List<Library>, private val newList: List<Library>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = TextUtils.equals(oldList[oldItemPosition].name, newList[newItemPosition].name)

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = TextUtils.equals(oldList[oldItemPosition].name, newList[newItemPosition].name)
}

class DifferentListUpdateCallback(private val oldList: List<Library>) : ListUpdateCallback {

    override fun onRemoved(position: Int, count: Int) {
        LL.d("licenses onRemoved: $position, $count")
        val topToDel = position + count - 1
        for (i in position..topToDel) {
            val library = oldList[i]
            LL.d("[library: ${library.name}] onRemoved at $position, total: $count")
            DB.INSTANCE.licensesLibrariesDao().deleteLibrary(LibraryEntity.from(library))
        }
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        LL.d("licenses onChanged: $position, $count")
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        LL.d("licenses onMoved: $fromPosition, $toPosition")
    }

    override fun onInserted(position: Int, count: Int) {
        LL.d("licenses onInserted: $position, $count")
    }

}