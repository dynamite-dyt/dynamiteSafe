package io.horizontalsystems.dynamitewallet.modules.backup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.horizontalsystems.dynamitewallet.R
import io.horizontalsystems.dynamitewallet.SingleLiveEvent
import io.horizontalsystems.dynamitewallet.core.IKeyStoreSafeExecute

class BackupViewModel : ViewModel(), BackupModule.IView, BackupModule.IRouter, IKeyStoreSafeExecute {
    lateinit var delegate: BackupModule.IViewDelegate

    val loadPageLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Int>()
    val errorLiveData = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Int>()
    val wordsLiveData = MutableLiveData<List<String>>()
    val wordIndexesToConfirmLiveData = MutableLiveData<List<Int>>()
    val validateWordsLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Void>()
    val closeLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Void>()
    val navigateToSetPinLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Void>()
    val showConfirmationCheckDialogLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Void>()
    val keyStoreSafeExecute = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Triple<Runnable, Runnable?, Runnable?>>()

    fun init(dismissMode: BackupPresenter.DismissMode) {
        BackupModule.init(this, this, this, dismissMode)
    }

    // view

    override fun loadPage(page: Int) {
        loadPageLiveEvent.value = page
    }

    override fun showWords(words: List<String>) {
        wordsLiveData.value = words
    }

    override fun showConfirmationWords(indexes: List<Int>) {
        wordIndexesToConfirmLiveData.value = indexes
    }

    override fun showConfirmationError() {
        errorLiveData.value = R.string.Backup_Confirmation_FailureAlertText
    }

    override fun showTermsConfirmDialog() {
        showConfirmationCheckDialogLiveEvent.call()
    }

    override fun validateWords() {
        validateWordsLiveEvent.call()
    }

    // router

    override fun close() {
        closeLiveEvent.call()
    }

    override fun navigateToSetPin() {
        navigateToSetPinLiveEvent.call()
    }

    override fun safeExecute(action: Runnable, onSuccess: Runnable?, onFailure: Runnable?) {
        keyStoreSafeExecute.value = Triple(action, onSuccess, onFailure)
    }
}
