package io.horizontalsystems.dynamitewallet.core.storage

import io.horizontalsystems.dynamitewallet.core.IEnabledCoinStorage
import io.horizontalsystems.dynamitewallet.entities.EnabledCoin
import io.reactivex.Flowable
import java.util.concurrent.Executors

class EnabledCoinsRepository(private val appDatabase: AppDatabase) : IEnabledCoinStorage {

    private val executor = Executors.newSingleThreadExecutor()

    override fun enabledCoinsObservable(): Flowable<List<EnabledCoin>> {
        return appDatabase.coinsDao().getEnabledCoins()
    }

    override fun save(coins: List<EnabledCoin>) {
        executor.execute {
            appDatabase.coinsDao().deleteAll()
            appDatabase.coinsDao().insertCoins(coins)
        }
    }

    override fun deleteAll() {
        executor.execute {
            appDatabase.coinsDao().deleteAll()
        }
    }

}
