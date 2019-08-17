package io.horizontalsystems.dynamitewallet.modules.transactions.transactionInfo

import androidx.lifecycle.ViewModel
import io.horizontalsystems.dynamitewallet.SingleLiveEvent
import io.horizontalsystems.dynamitewallet.entities.Coin
import io.horizontalsystems.dynamitewallet.modules.transactions.TransactionViewItem

class TransactionInfoViewModel : ViewModel(), TransactionInfoModule.View, TransactionInfoModule.Router {

    lateinit var delegate: TransactionInfoModule.ViewDelegate

    val transactionLiveData = io.horizontalsystems.dynamitewallet.SingleLiveEvent<TransactionViewItem>()
    val showFullInfoLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Pair<String, Coin>>()
    val showCopiedLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Unit>()

    fun init() {
        TransactionInfoModule.init(this, this)
    }

    override fun showCopied() {
        showCopiedLiveEvent.call()
    }

    override fun openFullInfo(transactionHash: String, coin: Coin) {
        showFullInfoLiveEvent.value = Pair(transactionHash, coin)
    }

    fun setViewItem(transactionViewItem: TransactionViewItem) {
        transactionLiveData.value = transactionViewItem
    }

    fun onClickTransactionId() {
        transactionLiveData.value?.let {
            delegate.onCopy(it.transactionHash)
        }
    }

    fun onClickOpenFillInfo() {
        transactionLiveData.value?.let {
            delegate.openFullInfo(it.transactionHash, it.coin)
        }
    }

    fun onClickFrom() {
        transactionLiveData.value?.from?.let {
            delegate.onCopy(it)
        }
    }

    fun onClickTo() {
        transactionLiveData.value?.to?.let {
            delegate.onCopy(it)
        }
    }
}
