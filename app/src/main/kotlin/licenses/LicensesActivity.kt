package licenses

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Bundle.EMPTY
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.template.mvvm.R
import com.template.mvvm.databinding.ActivityLicensesBinding
import com.template.mvvm.ext.setupActionBar

private const val LAYOUT: Int = R.layout.activity_licenses

class LicensesActivity : AppCompatActivity() {
    lateinit var binding: ActivityLicensesBinding

    companion object {
        fun showInstance(cxt: Activity) {
            val intent = Intent(cxt, LicensesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            ActivityCompat.startActivity(cxt, intent, EMPTY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityLicensesBinding>(this, LAYOUT).apply {
            this@LicensesActivity.setupActionBar(toolbar) {
                setHomeButtonEnabled(true)
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> supportFinishAfterTransition()
        }
        return super.onOptionsItemSelected(item)
    }
}