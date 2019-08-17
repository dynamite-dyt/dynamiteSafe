package io.horizontalsystems.dynamitewallet.modules.settings.security

import android.content.Context
import android.content.Intent
import io.horizontalsystems.dynamitewallet.core.App
import io.horizontalsystems.dynamitewallet.entities.BiometryType

object SecuritySettingsModule {

    interface ISecuritySettingsView {
        fun setBiometricUnlockOn(biometricUnlockOn: Boolean)
        fun setBiometryType(biometryType: BiometryType)
        fun setBackedUp(backedUp: Boolean)
        fun reloadApp()
    }

    interface ISecuritySettingsViewDelegate {
        fun viewDidLoad()
        fun didSwitchBiometricUnlock(biometricUnlockOn: Boolean)
        fun didTapEditPin()
        fun didTapBackupWallet()
        fun didTapRestoreWallet()
        fun confirmedUnlinkWallet()
        fun onClear()
    }

    interface ISecuritySettingsInteractor {
        var isBackedUp: Boolean
        val biometryType: BiometryType
        fun getBiometricUnlockOn(): Boolean
        fun setBiometricUnlockOn(biometricUnlockOn: Boolean)
        fun unlinkWallet()
        fun didTapOnBackupWallet()
        fun clear()
    }

    interface ISecuritySettingsInteractorDelegate {
        fun didBackup()
        fun didUnlinkWallet()
        fun openBackupWallet()
        fun accessIsRestricted()
    }

    interface ISecuritySettingsRouter{
        fun showEditPin()
        fun showBackupWallet()
        fun showRestoreWallet()
        fun showPinUnlock()
    }

    fun start(context: Context) {
        val intent = Intent(context, SecuritySettingsActivity::class.java)
        context.startActivity(intent)
    }

    fun init(view: SecuritySettingsViewModel, router: ISecuritySettingsRouter) {
        val interactor = SecuritySettingsInteractor(io.horizontalsystems.dynamitewallet.core.App.authManager, io.horizontalsystems.dynamitewallet.core.App.wordsManager, io.horizontalsystems.dynamitewallet.core.App.localStorage, io.horizontalsystems.dynamitewallet.core.App.systemInfoManager, io.horizontalsystems.dynamitewallet.core.App.lockManager)
        val presenter = SecuritySettingsPresenter(router, interactor)

        view.delegate = presenter
        presenter.view = view
        interactor.delegate = presenter
    }
}
