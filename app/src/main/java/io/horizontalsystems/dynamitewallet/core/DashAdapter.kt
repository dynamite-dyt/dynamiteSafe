package io.horizontalsystems.dynamitewallet.core

import android.content.Context
import io.horizontalsystems.dynamitewallet.core.utils.AddressParser
import io.horizontalsystems.dynamitewallet.entities.AuthData
import io.horizontalsystems.dynamitewallet.entities.Coin
import io.horizontalsystems.dynamitewallet.entities.SyncMode
import io.horizontalsystems.dynamitewallet.entities.TransactionRecord
import io.horizontalsystems.dynamitewallet.viewHelpers.DateHelper
import io.horizontalsystems.bitcoincore.BitcoinCore
import io.horizontalsystems.bitcoincore.models.BlockInfo
import io.horizontalsystems.dashkit.DashKit
import io.horizontalsystems.dashkit.DashKit.NetworkType
import io.horizontalsystems.dashkit.models.DashTransactionInfo
import io.reactivex.Single
import java.math.BigDecimal
import java.util.*

class DashAdapter(coin: Coin, override val kit: DashKit, addressParser: AddressParser, private val feeRateProvider: io.horizontalsystems.dynamitewallet.core.IFeeRateProvider) :
        io.horizontalsystems.dynamitewallet.core.BitcoinBaseAdapter(coin, kit, addressParser), DashKit.Listener {

    constructor(coin: Coin, authData: AuthData, syncMode: SyncMode, testMode: Boolean, feeRateProvider: io.horizontalsystems.dynamitewallet.core.IFeeRateProvider) :
            this(coin, io.horizontalsystems.dynamitewallet.core.DashAdapter.Companion.createKit(authData, syncMode, testMode), AddressParser("dash", true), feeRateProvider)


    init {
        kit.listener = this
    }

    //
    // BitcoinBaseAdapter
    //

    override val satoshisInBitcoin: BigDecimal = BigDecimal.valueOf(Math.pow(10.0, decimal.toDouble()))

    override fun feeRate(feePriority: io.horizontalsystems.dynamitewallet.core.FeeRatePriority): Int {
        return feeRateProvider.dashFeeRate(feePriority).toInt()
    }

    //
    // DashKit Listener
    //

    override fun onBalanceUpdate(balance: Long) {
        balanceUpdatedSubject.onNext(Unit)
    }

    override fun onLastBlockInfoUpdate(blockInfo: BlockInfo) {
        lastBlockHeightUpdatedSubject.onNext(Unit)
    }

    override fun onKitStateUpdate(state: BitcoinCore.KitState) {
        when (state) {
            is BitcoinCore.KitState.Synced -> {
                if (this.state !is io.horizontalsystems.dynamitewallet.core.AdapterState.Synced) {
                    this.state = io.horizontalsystems.dynamitewallet.core.AdapterState.Synced
                }
            }
            is BitcoinCore.KitState.NotSynced -> {
                if (this.state !is io.horizontalsystems.dynamitewallet.core.AdapterState.NotSynced) {
                    this.state = io.horizontalsystems.dynamitewallet.core.AdapterState.NotSynced
                }
            }
            is BitcoinCore.KitState.Syncing -> {
                this.state.let { currentState ->
                    val newProgress = (state.progress * 100).toInt()
                    val newDate = kit.lastBlockInfo?.timestamp?.let { Date(it * 1000) }

                    if (currentState is io.horizontalsystems.dynamitewallet.core.AdapterState.Syncing && currentState.progress == newProgress) {
                        val currentDate = currentState.lastBlockDate
                        if (newDate != null && currentDate != null && DateHelper.isSameDay(newDate, currentDate)) {
                            return
                        }
                    }

                    this.state = io.horizontalsystems.dynamitewallet.core.AdapterState.Syncing(newProgress, newDate)
                }
            }
        }
    }

    override fun onTransactionsUpdate(inserted: List<DashTransactionInfo>, updated: List<DashTransactionInfo>) {
        val records = mutableListOf<TransactionRecord>()

        for (info in inserted) {
            records.add(transactionRecord(info))
        }

        for (info in updated) {
            records.add(transactionRecord(info))
        }

        transactionRecordsSubject.onNext(records)
    }

    override fun onTransactionsDelete(hashes: List<String>) {
        // ignored for now
    }

    override fun getTransactions(from: Pair<String, Int>?, limit: Int): Single<List<TransactionRecord>> {
        return kit.transactions(from?.first, limit).map { it.map { tx -> transactionRecord(tx) } }
    }

    companion object {

        private fun getNetworkType(testMode: Boolean) =
                if (testMode) NetworkType.TestNet else NetworkType.MainNet

        private fun createKit(authData: AuthData, syncMode: SyncMode, testMode: Boolean): DashKit {
            return DashKit(io.horizontalsystems.dynamitewallet.core.App.Companion.instance, authData.words, authData.walletId, syncMode = syncMode.bitcoinKitMode(), networkType = io.horizontalsystems.dynamitewallet.core.DashAdapter.Companion.getNetworkType(testMode))
        }

        fun clear(context: Context, walletId: String, testMode: Boolean) {
            DashKit.clear(context, io.horizontalsystems.dynamitewallet.core.DashAdapter.Companion.getNetworkType(testMode), walletId)
        }
    }
}
