package io.horizontalsystems.dynamitewallet.core.managers

import android.security.keystore.UserNotAuthenticatedException
import io.horizontalsystems.dynamitewallet.core.*
import io.horizontalsystems.dynamitewallet.entities.AuthData
import io.horizontalsystems.dynamitewallet.entities.SyncMode
import io.reactivex.subjects.PublishSubject

class AuthManager(private val secureStorage: ISecuredStorage,
                  private val localStorage: ILocalStorage,
                  private val coinManager: ICoinManager,
                  private val rateManager: RateManager,
                  private val ethereumKitManager: IEthereumKitManager,
                  private val appConfigProvider: IAppConfigProvider) {

    var adapterManager: IAdapterManager? = null
    var pinManager: IPinManager? = null

    var authData: AuthData? = null
    var authDataSignal = PublishSubject.create<Unit>()

    var isLoggedIn: Boolean = false
        get() = !secureStorage.noAuthData()


    @Throws(UserNotAuthenticatedException::class)
    fun safeLoad() {
        authData = secureStorage.authData
        authDataSignal.onNext(Unit)
    }

    @Throws(UserNotAuthenticatedException::class)
    fun login(words: List<String>, syncMode: SyncMode) {
        AuthData(words).let {
            secureStorage.saveAuthData(it)
            localStorage.syncMode = syncMode
            authData = it
            coinManager.enableDefaultCoins()
        }
    }

    fun logout() {
        adapterManager?.stopKits()

        io.horizontalsystems.dynamitewallet.core.EthereumAdapter.clear(io.horizontalsystems.dynamitewallet.core.App.instance)
        io.horizontalsystems.dynamitewallet.core.Erc20Adapter.clear(io.horizontalsystems.dynamitewallet.core.App.instance)

        authData?.let { authData ->
            io.horizontalsystems.dynamitewallet.core.BitcoinAdapter.clear(io.horizontalsystems.dynamitewallet.core.App.instance, authData.walletId, appConfigProvider.testMode)
            io.horizontalsystems.dynamitewallet.core.BitcoinCashAdapter.clear(io.horizontalsystems.dynamitewallet.core.App.instance, authData.walletId, appConfigProvider.testMode)
            io.horizontalsystems.dynamitewallet.core.DashAdapter.clear(io.horizontalsystems.dynamitewallet.core.App.instance, authData.walletId, appConfigProvider.testMode)
        }

        pinManager?.clear()
        localStorage.clear()
        coinManager.clear()
        rateManager.clear()

//        todo: clear authData from secureStorage. note clearing localstorage also clears auth data
//        secureStorage.clearAuthData()
        authData = null

    }

}
