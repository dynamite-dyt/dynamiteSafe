package io.horizontalsystems.dynamitewallet.modules.guest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.horizontalsystems.dynamitewallet.SingleLiveEvent
import io.horizontalsystems.dynamitewallet.core.IKeyStoreSafeExecute

class GuestViewModel: ViewModel(), GuestModule.IView, GuestModule.IRouter, IKeyStoreSafeExecute {

    lateinit var delegate: GuestModule.IViewDelegate

    val openBackupScreenLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Void>()
    val openRestoreWalletScreenLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Void>()
    val showErrorDialog = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Void>()
    val keyStoreSafeExecute = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Triple<Runnable, Runnable?, Runnable?>>()
    val appVersionLiveData = MutableLiveData<String>()

    fun init() {
        GuestModule.init(this, this, this)
        delegate.onViewDidLoad()
    }

    override fun setAppVersion(appVersion: String) {
        appVersionLiveData.value = appVersion
    }

    // router
    override fun navigateToBackupRoutingToMain() {
        openBackupScreenLiveEvent.call()
    }

    override fun navigateToRestore() {
        openRestoreWalletScreenLiveEvent.call()
    }

    override fun showError() {
        showErrorDialog.call()
    }

    override fun safeExecute(action: Runnable, onSuccess: Runnable?, onFailure: Runnable?) {
        keyStoreSafeExecute.value = Triple(action, onSuccess, onFailure)
    }
}
