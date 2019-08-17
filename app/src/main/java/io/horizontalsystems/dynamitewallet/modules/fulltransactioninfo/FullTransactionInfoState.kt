package io.horizontalsystems.dynamitewallet.modules.fulltransactioninfo

import io.horizontalsystems.dynamitewallet.entities.Coin
import io.horizontalsystems.dynamitewallet.entities.FullTransactionRecord
import io.horizontalsystems.dynamitewallet.modules.transactions.CoinCode

class FullTransactionInfoState(override val coin: Coin, override val transactionHash: String)
    : FullTransactionInfoModule.State {

    override var transactionRecord: FullTransactionRecord? = null
}
