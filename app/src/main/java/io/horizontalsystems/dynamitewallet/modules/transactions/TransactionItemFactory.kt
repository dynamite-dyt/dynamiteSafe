package io.horizontalsystems.dynamitewallet.modules.transactions

import io.horizontalsystems.dynamitewallet.entities.Coin
import io.horizontalsystems.dynamitewallet.entities.TransactionItem
import io.horizontalsystems.dynamitewallet.entities.TransactionRecord

class TransactionItemFactory {

    fun createTransactionItem(coin: Coin, record: TransactionRecord): TransactionItem {
        return TransactionItem(coin, record)
    }

}
