package io.horizontalsystems.dynamitewallet.modules.receive

import io.horizontalsystems.dynamitewallet.core.IAdapterManager
import io.horizontalsystems.dynamitewallet.core.IClipboardManager
import io.horizontalsystems.dynamitewallet.modules.receive.viewitems.AddressItem
import io.horizontalsystems.dynamitewallet.modules.transactions.CoinCode

class ReceiveInteractor(
        private var coinCode: CoinCode?,
        private var adapterManager: IAdapterManager,
        private var clipboardManager: IClipboardManager) : ReceiveModule.IInteractor {

    var delegate: ReceiveModule.IInteractorDelegate? = null

    override fun getReceiveAddress() {
        adapterManager.adapters.firstOrNull { it.coin.code == coinCode }?.let {
            val addressItem = AddressItem(it.receiveAddress, it.coin)
            delegate?.didReceiveAddress(addressItem)
        }
    }

    override fun copyToClipboard(coinAddress: String) {
        clipboardManager.copyText(coinAddress)
        delegate?.didCopyToClipboard()
    }
}
