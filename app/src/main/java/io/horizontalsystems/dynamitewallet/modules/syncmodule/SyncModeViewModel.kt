package io.horizontalsystems.dynamitewallet.modules.syncmodule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.horizontalsystems.dynamitewallet.SingleLiveEvent
import io.horizontalsystems.dynamitewallet.core.IKeyStoreSafeExecute
import io.horizontalsystems.dynamitewallet.entities.SyncMode

class SyncModeViewModel : ViewModel(), SyncModeModule.IView, SyncModeModule.IRouter, IKeyStoreSafeExecute {

    lateinit var delegate: SyncModeModule.IViewDelegate

    val errorLiveData = MutableLiveData<Int>()
    val navigateToSetPinLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Void>()
    val syncModeUpdatedLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<SyncMode>()
    val showConfirmationDialogLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Void>()
    val keyStoreSafeExecute = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Triple<Runnable, Runnable?, Runnable?>>()

    fun init() {
        SyncModeModule.init(this, this, this)
        delegate.viewDidLoad()
    }

    override fun navigateToSetPin() {
        navigateToSetPinLiveEvent.call()
    }

    override fun updateSyncMode(syncMode: SyncMode) {
        syncModeUpdatedLiveEvent.value = syncMode
    }

    override fun safeExecute(action: Runnable, onSuccess: Runnable?, onFailure: Runnable?) {
        keyStoreSafeExecute.value = Triple(action, onSuccess, onFailure)
    }

    override fun showError(error: Int) {
        errorLiveData.value = error
    }

    override fun showConfirmationDialog() {
        showConfirmationDialogLiveEvent.call()
    }
}
