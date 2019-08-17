package io.horizontalsystems.dynamitewallet.modules.restore

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import io.horizontalsystems.dynamitewallet.BaseActivity
import io.horizontalsystems.dynamitewallet.R
import io.horizontalsystems.dynamitewallet.core.utils.Utils
import io.horizontalsystems.dynamitewallet.lib.InputTextViewHolder
import io.horizontalsystems.dynamitewallet.lib.WordsInputAdapter
import io.horizontalsystems.dynamitewallet.modules.syncmodule.SyncModeModule
import io.horizontalsystems.dynamitewallet.ui.extensions.TopMenuItem
import io.horizontalsystems.dynamitewallet.viewHelpers.HudHelper
import kotlinx.android.synthetic.main.activity_restore_wallet.*

class RestoreWalletActivity : io.horizontalsystems.dynamitewallet.BaseActivity() {

    private lateinit var viewModel: RestoreViewModel

    private val words = MutableList(12) { "" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restore_wallet)

        shadowlessToolbar.bind(
                title = getString(R.string.Restore_Title),
                leftBtnItem = TopMenuItem(R.drawable.back, { onBackPressed() }),
                rightBtnItem = TopMenuItem(R.drawable.surface1, { viewModel.delegate.restoreDidClick(words) })
        )

        viewModel = ViewModelProviders.of(this).get(RestoreViewModel::class.java)
        viewModel.init()

        viewModel.errorLiveData.observe(this, Observer { errorId ->
            errorId?.let {
                HudHelper.showErrorMessage(it)
            }
        })

        viewModel.navigateToSetSyncModeLiveEvent.observe(this, Observer { words ->
            words?.let {
                SyncModeModule.start(this, it)
            }
        })

        recyclerInputs.isNestedScrollingEnabled = false
        recyclerInputs.layoutManager = GridLayoutManager(this, 2)
        recyclerInputs.adapter = WordsInputAdapter(object : InputTextViewHolder.WordsChangedListener {
            override fun set(position: Int, value: String) {
                if (isUsingNativeKeyboard()) {
                    words[position] = value.toLowerCase()
                }
            }

            override fun done() {
                viewModel.delegate.restoreDidClick(words)
            }

        })
    }

    private fun isUsingNativeKeyboard(): Boolean {
        if (Utils.isUsingCustomKeyboard(this)) {
            showCustomKeyboardAlert()
            return false
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
