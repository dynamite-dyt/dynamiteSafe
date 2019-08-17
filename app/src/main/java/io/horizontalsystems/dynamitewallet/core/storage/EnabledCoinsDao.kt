package io.horizontalsystems.dynamitewallet.core.storage

import androidx.room.*
import io.horizontalsystems.dynamitewallet.entities.EnabledCoin
import io.reactivex.Flowable

@Dao
interface EnabledCoinsDao {

    @Query("SELECT * FROM EnabledCoin ORDER BY `order` ASC")
    fun getEnabledCoins(): Flowable<List<EnabledCoin>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(storableCoin: EnabledCoin)

    @Query("DELETE FROM EnabledCoin")
    fun deleteAll()

    @Transaction
    fun insertCoins(coins: List<EnabledCoin>) {
        coins.forEach { insert(it) }
    }

}
