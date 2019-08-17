package io.horizontalsystems.dynamitewallet.modules.receive

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.horizontalsystems.dynamitewallet.SingleLiveEvent
import io.horizontalsystems.dynamitewallet.modules.receive.viewitems.AddressItem
import io.horizontalsystems.dynamitewallet.modules.transactions.CoinCode

class ReceiveViewModel : ViewModel(), ReceiveModule.IView, ReceiveModule.IRouter {

    lateinit var delegate: ReceiveModule.IViewDelegate

    val showAddressLiveData = MutableLiveData<AddressItem>()
    val showErrorLiveData = MutableLiveData<Int>()
    val showCopiedLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Unit>()
    val shareAddressLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<String>()

    fun init(coinCode: CoinCode) {
        ReceiveModule.init(coinCode, this, this)
        delegate.viewDidLoad()
    }

    override fun showAddress(addresses: AddressItem) {
        showAddressLiveData.value = addresses
    }

    override fun showError(error: Int) {
        showErrorLiveData.value = error
    }

    override fun showCopied() {
        showCopiedLiveEvent.call()
    }

    override fun shareAddress(address: String) {
        shareAddressLiveEvent.value = address
    }
}
