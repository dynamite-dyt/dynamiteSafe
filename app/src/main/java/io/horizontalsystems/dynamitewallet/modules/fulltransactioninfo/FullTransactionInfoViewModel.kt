package io.horizontalsystems.dynamitewallet.modules.fulltransactioninfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.horizontalsystems.dynamitewallet.SingleLiveEvent
import io.horizontalsystems.dynamitewallet.entities.Coin

class FullTransactionInfoViewModel : ViewModel(), FullTransactionInfoModule.View, FullTransactionInfoModule.Router {

    lateinit var delegate: FullTransactionInfoModule.ViewDelegate

    val loadingLiveData = MutableLiveData<Boolean>()
    val reloadLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Void>()
    val showCopiedLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Unit>()
    val showErrorLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Pair<Boolean, String?>>()
    val showShareLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<String>()
    val openLinkLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<String>()
    val openProviderSettingsEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Pair<Coin, String>>()

    fun init(transactionHash: String, coin: Coin) {
        FullTransactionInfoModule.init(this, this, coin, transactionHash)
        delegate.viewDidLoad()
    }

    fun retry() {
        delegate.onRetryLoad()
    }

    fun share() {
        delegate.onShare()
    }

    fun changeProvider() {
        delegate.onTapChangeProvider()
    }

    //
    // View
    //
    override fun show() {
    }

    override fun showLoading() {
        loadingLiveData.value = true
    }

    override fun hideLoading() {
        loadingLiveData.value = false
    }

    override fun showError(providerName: String?) {
        showErrorLiveEvent.value = Pair(true, providerName)
    }

    override fun hideError() {
        showErrorLiveEvent.value = Pair(false, null)
    }

    override fun reload() {
        reloadLiveEvent.call()
    }

    override fun showCopied() {
        showCopiedLiveEvent.call()
    }

    override fun openProviderSettings(coin: Coin, transactionHash: String) {
        openProviderSettingsEvent.value = Pair(coin, transactionHash)
    }

    override fun openUrl(url: String) {
        openLinkLiveEvent.value = url
    }

    override fun share(url: String) {
        showShareLiveEvent.value = url
    }

    override fun onCleared() {
        delegate.onClear()
    }

}
