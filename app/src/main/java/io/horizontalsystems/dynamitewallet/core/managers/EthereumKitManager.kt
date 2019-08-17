package io.horizontalsystems.dynamitewallet.core.managers

import io.horizontalsystems.dynamitewallet.R
import io.horizontalsystems.dynamitewallet.core.App
import io.horizontalsystems.dynamitewallet.core.IAppConfigProvider
import io.horizontalsystems.dynamitewallet.core.IEthereumKitManager
import io.horizontalsystems.dynamitewallet.entities.AuthData
import io.horizontalsystems.ethereumkit.core.EthereumKit

class EthereumKitManager(appConfig: IAppConfigProvider) : IEthereumKitManager {
    private var kit: EthereumKit? = null
    private var useCount = 0
    private val testMode = appConfig.testMode
    private val infuraProjectId = io.horizontalsystems.dynamitewallet.core.App.instance.getString(R.string.infuraProjectId)
    private val infuraSecretKey = io.horizontalsystems.dynamitewallet.core.App.instance.getString(R.string.infuraSecretKey)
    private val etherscanKey = io.horizontalsystems.dynamitewallet.core.App.instance.getString(R.string.etherscanKey)

    override val ethereumKit: EthereumKit?
        get() = kit

    override fun ethereumKit(authData: AuthData): EthereumKit {
        useCount += 1

        kit?.let { return it }
        val syncMode = EthereumKit.WordsSyncMode.ApiSyncMode()
        val infuraCredentials = EthereumKit.InfuraCredentials(infuraProjectId, infuraSecretKey)
        val networkType = if (testMode) EthereumKit.NetworkType.Ropsten else EthereumKit.NetworkType.MainNet
        kit = EthereumKit.getInstance(io.horizontalsystems.dynamitewallet.core.App.instance, authData.words, syncMode, networkType, infuraCredentials, etherscanKey, authData.walletId)

        kit?.start()

        return kit!!
    }

    override fun unlink() {
        useCount -= 1

        if (useCount < 1) {
            kit?.stop()
            kit = null
        }
    }
}
