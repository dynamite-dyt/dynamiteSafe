package io.horizontalsystems.dynamitewallet.modules.settings.security

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.horizontalsystems.dynamitewallet.SingleLiveEvent
import io.horizontalsystems.dynamitewallet.entities.BiometryType

class SecuritySettingsViewModel : ViewModel(), SecuritySettingsModule.ISecuritySettingsView, SecuritySettingsModule.ISecuritySettingsRouter {

    lateinit var delegate: SecuritySettingsModule.ISecuritySettingsViewDelegate

    val biometryTypeLiveDate = MutableLiveData<BiometryType>()

    val backedUpLiveData = MutableLiveData<Boolean>()
    val biometricUnlockOnLiveDate = MutableLiveData<Boolean>()
    val openEditPinLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Unit>()
    val openBackupWalletLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Unit>()
    val openRestoreWalletLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Unit>()
    val reloadAppLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Unit>()
    val showPinUnlockLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Unit>()

    fun init() {
        SecuritySettingsModule.init(this, this)
        delegate.viewDidLoad()
    }

    override fun setBiometricUnlockOn(biometricUnlockOn: Boolean) {
        biometricUnlockOnLiveDate.value = biometricUnlockOn
    }

    override fun setBiometryType(biometryType: BiometryType) {
        biometryTypeLiveDate.value = biometryType
    }

    override fun setBackedUp(backedUp: Boolean) {
        backedUpLiveData.value = backedUp
    }

    override fun showEditPin() {
        openEditPinLiveEvent.call()
    }

    override fun showBackupWallet() {
        openBackupWalletLiveEvent.call()
    }

    override fun showRestoreWallet() {
        openRestoreWalletLiveEvent.call()
    }

    override fun reloadApp() {
        reloadAppLiveEvent.call()
    }

    override fun showPinUnlock() {
        showPinUnlockLiveEvent.call()
    }

    override fun onCleared() {
        delegate.onClear()
    }

}
