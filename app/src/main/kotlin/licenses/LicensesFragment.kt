package licenses

import android.arch.lifecycle.LifecycleFragment
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Bundle.EMPTY
import android.support.annotation.Nullable
import android.support.v4.app.LoaderManager
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.Loader
import android.support.v4.util.ArrayMap
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.google.gson.Gson
import com.qiaoqiao.licenses.readTextFile
import com.template.mvvm.R
import com.template.mvvm.actor.Interactor
import com.template.mvvm.actor.Message
import com.template.mvvm.databinding.ChildBinding
import com.template.mvvm.databinding.FragmentLicensesBinding
import com.template.mvvm.databinding.GroupBinding
import licenses.bus.CloseExpandableListGroupEvent
import java.io.IOException
import java.io.InputStreamReader

private const val LAYOUT = R.layout.fragment_licenses
private const val LAYOUT_GROUP = R.layout.list_licenses_item_group
private const val LAYOUT_CHILD = R.layout.list_licenses_item_child
private const val ID_LOAD_LICENCES_TASK = 0x54
private const val LICENCES_LIST_JSON = "licenses-list.json"
private const val LICENCES_BOX = "licenses-box"
private const val COPYRIGHT_HOLDERS = "<copyright holders>"
private const val YEAR = "<year>"
private const val LICENCE_BOX_LOCATION_FORMAT = "%s/%s.txt"

class LicensesFragment : LifecycleFragment() {
    private lateinit var binding: FragmentLicensesBinding
    private val gson = Gson()

    private fun collapseGroup(msg: Message<Any>) {
        val ev = msg as CloseExpandableListGroupEvent
        binding.licencesList.collapseGroup(ev.groupIndex)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            collapseExpandedGroup()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, LAYOUT, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View?, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadLicences()
        Interactor.start(this)
                .subscribe(CloseExpandableListGroupEvent::class, this::collapseGroup)
                .register()
    }

    private fun collapseExpandedGroup() {
        val adapter = binding.licencesList.expandableListAdapter ?: return
        var groupPos = 0
        val cnt = adapter.groupCount
        while (groupPos < cnt) {
            if (binding.licencesList.isGroupExpanded(groupPos)) {
                binding.licencesList.collapseGroup(groupPos)
            }
            groupPos++
        }
    }

    private fun loadLicences() {
        loaderManager.initLoader(ID_LOAD_LICENCES_TASK, EMPTY, object : LoaderManager.LoaderCallbacks<Licenses> {

            override fun onCreateLoader(id: Int, args: Bundle): Loader<Licenses>? = CreateLoaderTask(context, gson)

            override fun onLoadFinished(loader: Loader<Licenses>, licenses: Licenses?) {
                if (licenses != null) {
                    val adapter = LicencesListAdapter(licenses)
                    binding.licensesCountTv.text = getString(R.string.software_licenses_count,
                            licenses.licenses
                                    .size,
                            adapter.groupCount)
                    binding.licencesList.setAdapter(adapter)
                }
            }

            override fun onLoaderReset(loader: Loader<Licenses>) {

            }
        }).forceLoad()
    }
}

class CreateLoaderTask constructor(context: Context, private val mGson: Gson) : AsyncTaskLoader<Licenses>(context) {
    override fun loadInBackground() = try {
        mGson.fromJson(InputStreamReader(context.assets
                .open(LICENCES_LIST_JSON)), Licenses::class.java)
    } catch (e: IOException) {
        null
    }
}

class LicencesListAdapter(licenses: Licenses) : BaseExpandableListAdapter() {
    private val licencesCount: Int
    private val libraryList: ArrayMap<Library, Pair<String, String>> = ArrayMap()//Pair contains licence's name and description.
    private val licenceContentList: ArrayMap<String, String> = ArrayMap()

    init {
        licenses.licenses.forEach({ license ->
            license.libraries.forEach({ library ->
                libraryList.put(library, Pair(license.name, license.description))
            })
        })
        licencesCount = libraryList.size
    }

    override fun getGroupCount() = licencesCount

    override fun getChildrenCount(groupPosition: Int) = 1

    override fun getGroup(groupPosition: Int): Any {
        throw UnsupportedOperationException("This adapter doesn't need group.")
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        throw UnsupportedOperationException("This adapter doesn't need children.")
    }

    override fun getGroupId(groupPosition: Int) = groupPosition.toLong()

    override fun getChildId(groupPosition: Int, childPosition: Int) = (childPosition * childPosition).toLong()

    override fun hasStableIds() = true

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var cv = convertView
        val binding: GroupBinding
        if (cv == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), LAYOUT_GROUP, parent, false)
            cv = binding.root
            cv!!.tag = binding
        } else {
            binding = cv.tag as GroupBinding
        }
        val library = libraryList.keyAt(groupPosition)
        val nameDesc = libraryList[library]

        binding.title = library.name
        binding.description = nameDesc?.second
        binding.executePendingBindings()
        return cv
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View? {
        var cv = convertView
        val binding: ChildBinding
        if (cv == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), LAYOUT_CHILD, parent, false)
            cv = binding.root
            cv.tag = binding
        } else {
            binding = cv.tag as ChildBinding
        }
        var content: String?
        val library = libraryList.keyAt(groupPosition)
        val nameDesc = libraryList[library]
        //Licence content will be read from disk firstly and from memory next time.
        if (nameDesc != null && licenceContentList[nameDesc.first] == null) {
            content = loadLicencesContent(parent.context, nameDesc.first)
            licenceContentList.put(nameDesc.first, content)
        } else {
            content = licenceContentList[nameDesc?.first]
        }
        if (content != null && content.contains(YEAR) && content.contains(COPYRIGHT_HOLDERS)) {
            content = content.replace(YEAR,
                    if (TextUtils.isEmpty(library.copyright))
                        ""
                    else
                        library.copyright)
                    .replace(COPYRIGHT_HOLDERS,
                            if (TextUtils.isEmpty(library.owner))
                                ""
                            else
                                library.owner)
        }
        binding.root
                .setOnClickListener({
                    Interactor.post(CloseExpandableListGroupEvent(groupPosition))
                })
        binding.content = content
        binding.executePendingBindings()
        return cv
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int) = true

    private companion object {

        private fun loadLicencesContent(cxt: Context, licenceName: String?): String? = try {
            val licenceLocation = String.format(LICENCE_BOX_LOCATION_FORMAT, LICENCES_BOX, licenceName)

            readTextFile(cxt.assets
                    .open(licenceLocation))
        } catch (e: IOException) {
            null
        }
    }
}