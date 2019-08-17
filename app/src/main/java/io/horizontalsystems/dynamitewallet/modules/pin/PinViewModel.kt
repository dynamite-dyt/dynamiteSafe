package io.horizontalsystems.dynamitewallet.modules.pin

import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.horizontalsystems.dynamitewallet.SingleLiveEvent
import io.horizontalsystems.dynamitewallet.core.IKeyStoreSafeExecute
import io.horizontalsystems.dynamitewallet.modules.pin.edit.EditPinModule
import io.horizontalsystems.dynamitewallet.modules.pin.set.SetPinModule
import io.horizontalsystems.dynamitewallet.modules.pin.unlock.UnlockPinModule
import java.util.*

class PinViewModel: ViewModel(), PinModule.IPinView, SetPinModule.ISetPinRouter, EditPinModule.IEditPinRouter, IKeyStoreSafeExecute, UnlockPinModule.IUnlockPinRouter {

    lateinit var delegate: PinModule.IPinViewDelegate
    val titleLiveDate = MutableLiveData<Int>()
    val addPagesEvent = MutableLiveData<List<PinPage>>()
    val showPageAtIndex = MutableLiveData<Int>()
    val showError = MutableLiveData<Int>()
    val showErrorForPage = MutableLiveData<Pair<Int, Int>>()
    val fillPinCircles = MutableLiveData<Pair<Int, Int>>()
    val navigateToMainLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Unit>()
    val hideToolbar = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Unit>()
    val dismissLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Unit>()
    val showBackButton = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Unit>()
    val showFingerprintInputLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<FingerprintManagerCompat.CryptoObject>()
    val resetCirclesWithShakeAndDelayForPage = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Int>()
    val keyStoreSafeExecute = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Triple<Runnable, Runnable?, Runnable?>>()
    val showAttemptsLeftError = MutableLiveData<Pair<Int, Int>>()
    val showLockedView = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Date>()
    val closeApplicationLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Unit>()


    fun init(interactionType: PinInteractionType, showCancelButton: Boolean) {
        when(interactionType) {
            PinInteractionType.SET_PIN -> SetPinModule.init(this, this, this)
            PinInteractionType.UNLOCK -> UnlockPinModule.init(this, this, this, showCancelButton)
            PinInteractionType.EDIT_PIN -> EditPinModule.init(this, this, this)
        }
        delegate.viewDidLoad()
    }

    override fun setTitle(title: Int) {
        titleLiveDate.value = title
    }

    override fun hideToolbar() {
        hideToolbar.call()
    }

    override fun addPages(pages: List<PinPage>) {
        addPagesEvent.value = pages
    }

    override fun showPage(index: Int) {
        showPageAtIndex.value = index
    }

    override fun showErrorForPage(error: Int, pageIndex: Int) {
        showErrorForPage.value = Pair(error, pageIndex)
    }

    override fun showError(error: Int) {
        showError.value = error
    }

    override fun showPinWrong(pageIndex: Int) {
        resetCirclesWithShakeAndDelayForPage.value = pageIndex
    }

    override fun showFingerprintDialog(cryptoObject: FingerprintManagerCompat.CryptoObject) {
        showFingerprintInputLiveEvent.value = cryptoObject
    }

    override fun showBackButton() {
        showBackButton.call()
    }

    override fun fillCircles(length: Int, pageIndex: Int) {
        fillPinCircles.value = Pair(length, pageIndex)
    }

    override fun navigateToMain() {
        navigateToMainLiveEvent.call()
    }

    override fun safeExecute(action: Runnable, onSuccess: Runnable?, onFailure: Runnable?) {
        keyStoreSafeExecute.value = Triple(action, onSuccess, onFailure)
    }

    override fun closeApplication() {
        closeApplicationLiveEvent.call()
    }

    override fun dismiss() {
        dismissLiveEvent.call()
    }

    override fun showLockView(until: Date) {
        showLockedView.postValue(until)
    }

    override fun showAttemptsLeft(attempts: Int?, pageIndex: Int) {
        attempts?.let {
            showAttemptsLeftError.postValue(Pair(attempts, pageIndex))
        }
    }
}
