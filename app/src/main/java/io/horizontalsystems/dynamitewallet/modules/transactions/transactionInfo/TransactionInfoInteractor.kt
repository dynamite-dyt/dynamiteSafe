package io.horizontalsystems.dynamitewallet.modules.transactions.transactionInfo

import io.horizontalsystems.dynamitewallet.core.IClipboardManager

class TransactionInfoInteractor(private var clipboardManager: IClipboardManager) : TransactionInfoModule.Interactor {
    var delegate: TransactionInfoModule.InteractorDelegate? = null

    override fun onCopy(value: String) {
        clipboardManager.copyText(value)
    }

}
