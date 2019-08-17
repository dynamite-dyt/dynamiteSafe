package io.horizontalsystems.dynamitewallet.modules.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import io.horizontalsystems.dynamitewallet.BaseActivity
import io.horizontalsystems.dynamitewallet.R
import io.horizontalsystems.dynamitewallet.ui.extensions.TopMenuItem
import kotlinx.android.synthetic.main.activity_about_settings.*

class AboutSettingsActivity: io.horizontalsystems.dynamitewallet.BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_settings)

        shadowlessToolbar.bind(
                title = getString(R.string.SettingsAbout_Title),
                leftBtnItem = TopMenuItem(R.drawable.back, { onBackPressed() })
        )
    }

    companion object {
        fun start(context: Activity) {
            val intent = Intent(context, AboutSettingsActivity::class.java)
            context.startActivity(intent)
        }
    }
}
