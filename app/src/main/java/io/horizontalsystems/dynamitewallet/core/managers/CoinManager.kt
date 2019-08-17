package io.horizontalsystems.dynamitewallet.core.managers

import io.horizontalsystems.dynamitewallet.core.IAppConfigProvider
import io.horizontalsystems.dynamitewallet.core.ICoinManager
import io.horizontalsystems.dynamitewallet.core.IEnabledCoinStorage
import io.horizontalsystems.dynamitewallet.entities.Coin
import io.horizontalsystems.dynamitewallet.entities.EnabledCoin
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class CoinManager(private val appConfigProvider: IAppConfigProvider, private val enabledCoinStorage: IEnabledCoinStorage) : ICoinManager {

    override val coinsUpdatedSignal: PublishSubject<Unit> = PublishSubject.create()

    init {
        val disposable = enabledCoinStorage.enabledCoinsObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {enabledCoinsFromDb ->
                    val enabledCoins = mutableListOf<Coin>()
                    enabledCoinsFromDb.forEach { enabledCoin ->
                        allCoins.firstOrNull { coin -> coin.code == enabledCoin.coinCode}
                                ?.let { enabledCoins.add(it) }
                    }
                    coins = enabledCoins
                }
    }

    override var coins: List<Coin> = listOf()
        set(value) {
            field = value
            coinsUpdatedSignal.onNext(Unit)
        }

    override val allCoins: List<Coin>
        get() = appConfigProvider.coins

    override fun enableDefaultCoins() {
        val enabledCoins = mutableListOf<EnabledCoin>()
        appConfigProvider.defaultCoinCodes.forEachIndexed{order, coinCode ->
            enabledCoins.add(EnabledCoin(coinCode, order))
        }
        enabledCoinStorage.save(enabledCoins)
    }

    override fun clear() {
        coins = listOf()
        enabledCoinStorage.deleteAll()
    }
}
