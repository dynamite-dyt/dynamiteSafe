package io.horizontalsystems.dynamitewallet.modules.transactions

import io.horizontalsystems.dynamitewallet.entities.Coin
import io.horizontalsystems.dynamitewallet.entities.Currency
import io.horizontalsystems.dynamitewallet.entities.CurrencyValue
import java.math.BigDecimal

class TransactionMetadataDataSource {

    private val lastBlockHeights = mutableMapOf<Coin, Int>()
    private val thresholds = mutableMapOf<Coin, Int>()
    private val rates = mutableMapOf<Coin, MutableMap<Long, CurrencyValue>>()

    fun setLastBlockHeight(lastBlockHeight: Int, coin: Coin) {
        lastBlockHeights[coin] = lastBlockHeight
    }

    fun getLastBlockHeight(coinCode: Coin): Int? {
        return lastBlockHeights[coinCode]
    }

    fun setConfirmationThreshold(confirmationThreshold: Int, coin: Coin) {
        thresholds[coin] = confirmationThreshold
    }

    fun getConfirmationThreshold(coin: Coin): Int? =
            thresholds[coin]

    fun setRate(rateValue: BigDecimal, coin: Coin, currency: Currency, timestamp: Long) {
        if (!rates.containsKey(coin)) {
            rates[coin] = mutableMapOf()
        }

        rates[coin]?.set(timestamp, CurrencyValue(currency, rateValue))
    }

    fun getRate(coin: Coin, timestamp: Long): CurrencyValue? {
        return rates[coin]?.get(timestamp)
    }

    fun clearRates() {
        rates.clear()
    }

}
