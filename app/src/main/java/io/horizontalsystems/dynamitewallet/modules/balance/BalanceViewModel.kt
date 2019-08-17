package io.horizontalsystems.dynamitewallet.modules.balance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.horizontalsystems.dynamitewallet.SingleLiveEvent

class BalanceViewModel : ViewModel(), BalanceModule.IView, BalanceModule.IRouter {

    lateinit var delegate: BalanceModule.IViewDelegate

    val openSendDialog = io.horizontalsystems.dynamitewallet.SingleLiveEvent<String>()
    val openReceiveDialog = io.horizontalsystems.dynamitewallet.SingleLiveEvent<String>()
    val balanceColorLiveDate = MutableLiveData<Int>()
    val didRefreshLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Void>()
    val openManageCoinsLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Void>()
    val openSortingTypeDialogLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<BalanceSortType>()

    val reloadLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Void>()
    val reloadHeaderLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Void>()
    val reloadItemLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Int>()
    val enabledCoinsCountLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Int>()
    val setSortingOnLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Boolean>()

    fun init() {
        BalanceModule.init(this, this)

        delegate.viewDidLoad()
    }

    override fun enabledCoinsCount(size: Int) {
        enabledCoinsCountLiveEvent.value = size
    }

    override fun reload() {
        reloadLiveEvent.postValue(null)
    }

    override fun updateItem(position: Int) {
        reloadItemLiveEvent.value = position
    }

    override fun updateHeader() {
        reloadHeaderLiveEvent.postValue(null)
    }

    override fun openReceiveDialog(coin: String) {
        openReceiveDialog.value = coin
    }

    override fun openSendDialog(coin: String) {
        openSendDialog.value = coin
    }

    fun onReceiveClicked(position: Int) {
        delegate.onReceive(position)
    }

    fun onSendClicked(position: Int) {
        delegate.onPay(position)
    }

    override fun didRefresh() {
        didRefreshLiveEvent.call()
    }

    override fun openManageCoins() {
        openManageCoinsLiveEvent.call()
    }

    override fun onCleared() {
        delegate.onClear()
    }

    override fun openSortTypeDialog(sortingType: BalanceSortType) {
        openSortingTypeDialogLiveEvent.postValue(sortingType)
    }

    override fun setSortingOn(isOn: Boolean) {
        setSortingOnLiveEvent.postValue(isOn)
    }
}
