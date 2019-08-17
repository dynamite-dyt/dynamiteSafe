package io.horizontalsystems.dynamitewallet.modules.guest

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.horizontalsystems.dynamitewallet.BaseActivity
import io.horizontalsystems.dynamitewallet.BuildConfig
import io.horizontalsystems.dynamitewallet.R
import io.horizontalsystems.dynamitewallet.core.setOnSingleClickListener
import io.horizontalsystems.dynamitewallet.modules.backup.BackupModule
import io.horizontalsystems.dynamitewallet.modules.backup.BackupPresenter
import io.horizontalsystems.dynamitewallet.modules.restore.RestoreModule
import io.horizontalsystems.dynamitewallet.viewHelpers.HudHelper
import kotlinx.android.synthetic.main.activity_add_wallet.*


class GuestActivity : io.horizontalsystems.dynamitewallet.BaseActivity() {

    private lateinit var viewModel: GuestViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransparentStatusBar()

        setContentView(R.layout.activity_add_wallet)

        viewModel = ViewModelProviders.of(this).get(GuestViewModel::class.java)
        viewModel.init()

        viewModel.openBackupScreenLiveEvent.observe(this, Observer {
            BackupModule.start(this, BackupPresenter.DismissMode.SET_PIN)
            finish()
        })

        viewModel.openRestoreWalletScreenLiveEvent.observe(this, Observer {
            RestoreModule.start(this)
        })

        viewModel.showErrorDialog.observe(this, Observer {
            HudHelper.showErrorMessage(R.string.Error)
        })

        viewModel.appVersionLiveData.observe(this, Observer { appVersion ->
            appVersion?.let {
                var version = it
                if (getString(R.string.is_release) == "false") {
                    version = "$version (${BuildConfig.VERSION_CODE})"
                }
                textVersion.text = getString(R.string.Guest_Version, version)
            }
        })

        buttonCreate.setOnSingleClickListener {
            viewModel.delegate.createWalletDidClick()
        }

        buttonRestore.setOnSingleClickListener {
            viewModel.delegate.restoreWalletDidClick()
        }

        viewModel.keyStoreSafeExecute.observe(this, Observer { triple ->
            triple?.let {
                val (action, onSuccess, onFailure) = it
                safeExecuteWithKeystore(action, onSuccess, onFailure)
            }
        })
    }

}
