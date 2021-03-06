package io.horizontalsystems.dynamitewallet.core

import android.content.Context
import io.horizontalsystems.dynamitewallet.core.utils.AddressParser
import io.horizontalsystems.dynamitewallet.entities.Coin
import io.horizontalsystems.dynamitewallet.entities.TransactionAddress
import io.horizontalsystems.dynamitewallet.entities.TransactionRecord
import io.horizontalsystems.erc20kit.core.Erc20Kit
import io.horizontalsystems.erc20kit.core.TransactionKey
import io.horizontalsystems.erc20kit.models.TransactionInfo
import io.horizontalsystems.ethereumkit.core.EthereumKit
import io.horizontalsystems.ethereumkit.core.hexStringToByteArray
import io.reactivex.Flowable
import io.reactivex.Single
import java.math.BigDecimal

class Erc20Adapter(context: Context,
                   coin: Coin,
                   kit: EthereumKit,
                   decimal: Int,
                   private val fee: BigDecimal,
                   contractAddress: String,
                   addressParser: AddressParser,
                   feeRateProvider: io.horizontalsystems.dynamitewallet.core.IFeeRateProvider) : io.horizontalsystems.dynamitewallet.core.EthereumBaseAdapter(coin, kit, decimal, addressParser, feeRateProvider) {

    private val erc20Kit: Erc20Kit = Erc20Kit.getInstance(context, ethereumKit, contractAddress)

    override val state: io.horizontalsystems.dynamitewallet.core.AdapterState
        get() = when (erc20Kit.syncState) {
            is Erc20Kit.SyncState.Synced -> io.horizontalsystems.dynamitewallet.core.AdapterState.Synced
            is Erc20Kit.SyncState.NotSynced -> io.horizontalsystems.dynamitewallet.core.AdapterState.NotSynced
            is Erc20Kit.SyncState.Syncing -> io.horizontalsystems.dynamitewallet.core.AdapterState.Syncing(50, null)
        }

    override val stateUpdatedFlowable: Flowable<Unit>
        get() = erc20Kit.syncStateFlowable.map { Unit }

    override val balance: BigDecimal
        get() = balanceInBigDecimal(erc20Kit.balance, decimal)

    override val balanceUpdatedFlowable: Flowable<Unit>
        get() = erc20Kit.balanceFlowable.map { Unit }

    override fun getTransactions(from: Pair<String, Int>?, limit: Int): Single<List<TransactionRecord>> {
        return erc20Kit.transactions(from?.let { TransactionKey(it.first.hexStringToByteArray(), it.second) }, limit).map {
            it.map { tx -> transactionRecord(tx) }
        }
    }

    override val transactionRecordsFlowable: Flowable<List<TransactionRecord>>
        get() = erc20Kit.transactionsFlowable.map { it.map { tx -> transactionRecord(tx) } }

    override fun sendSingle(address: String, amount: String, gasPrice: Long): Single<Unit> {
        return erc20Kit.send(address, amount, gasPrice).map { Unit }
    }

    override fun availableBalance(address: String?, feePriority: io.horizontalsystems.dynamitewallet.core.FeeRatePriority): BigDecimal {
        return balance - fee
    }

    override fun fee(value: BigDecimal, address: String?, feePriority: io.horizontalsystems.dynamitewallet.core.FeeRatePriority): BigDecimal {
        return erc20Kit.fee(feeRateProvider.ethereumGasPrice(feePriority)).movePointLeft(18)
    }

    override fun validate(amount: BigDecimal, address: String?, feePriority: io.horizontalsystems.dynamitewallet.core.FeeRatePriority): List<io.horizontalsystems.dynamitewallet.core.SendStateError> {
        val errors = mutableListOf<io.horizontalsystems.dynamitewallet.core.SendStateError>()
        if (amount > availableBalance(address, feePriority)) {
            errors.add(io.horizontalsystems.dynamitewallet.core.SendStateError.InsufficientAmount)
        }
        if (balanceInBigDecimal(ethereumKit.balance, decimal) < fee(amount, address, feePriority)) {
            errors.add(io.horizontalsystems.dynamitewallet.core.SendStateError.InsufficientFeeBalance)
        }
        return errors
    }

    private fun transactionRecord(transaction: TransactionInfo): TransactionRecord {
        val mineAddress = ethereumKit.receiveAddress

        val from = TransactionAddress(transaction.from, transaction.from == mineAddress)
        val to = TransactionAddress(transaction.to, transaction.to == mineAddress)

        var amount: BigDecimal

        transaction.value.toBigDecimal().let {
            amount = it.movePointLeft(decimal)
            if (from.mine) {
                amount = -amount
            }
        }

        return TransactionRecord(
                transactionHash = transaction.transactionHash,
                transactionIndex = transaction.transactionIndex ?: 0,
                interTransactionIndex = transaction.interTransactionIndex,
                blockHeight = transaction.blockNumber,
                amount = amount,
                timestamp = transaction.timestamp,
                from = listOf(from),
                to = listOf(to)
        )
    }

    companion object {
        fun clear(context: Context) {
            Erc20Kit.clear(context)
        }
    }

}
