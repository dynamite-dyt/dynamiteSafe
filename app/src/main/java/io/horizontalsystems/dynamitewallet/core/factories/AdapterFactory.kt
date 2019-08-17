package io.horizontalsystems.dynamitewallet.core.factories

import android.content.Context
import io.horizontalsystems.dynamitewallet.core.*
import io.horizontalsystems.dynamitewallet.core.utils.AddressParser
import io.horizontalsystems.dynamitewallet.entities.AuthData
import io.horizontalsystems.dynamitewallet.entities.Coin
import io.horizontalsystems.dynamitewallet.entities.CoinType

class AdapterFactory(
        private val context: Context,
        private val appConfigProvider: IAppConfigProvider,
        private val localStorage: ILocalStorage,
        private val ethereumKitManager: IEthereumKitManager,
        private val feeRateProvider: IFeeRateProvider) {

    fun adapterForCoin(coin: Coin, authData: AuthData): IAdapter? = when (coin.type) {
        is CoinType.Bitcoin -> io.horizontalsystems.dynamitewallet.core.BitcoinAdapter(coin, authData, localStorage.syncMode, appConfigProvider.testMode, feeRateProvider)
        is CoinType.BitcoinCash -> io.horizontalsystems.dynamitewallet.core.BitcoinCashAdapter(coin, authData, localStorage.syncMode, appConfigProvider.testMode, feeRateProvider)
        is CoinType.Dash -> io.horizontalsystems.dynamitewallet.core.DashAdapter(coin, authData, localStorage.syncMode, appConfigProvider.testMode, feeRateProvider)
        is CoinType.Ethereum -> {
            val addressParser = AddressParser("ethereum", true)
            io.horizontalsystems.dynamitewallet.core.EthereumAdapter(coin, ethereumKitManager.ethereumKit(authData), addressParser, feeRateProvider)
        }
        is CoinType.Erc20 -> {
            val addressParser = AddressParser("ethereum", true)
            io.horizontalsystems.dynamitewallet.core.Erc20Adapter(context, coin, ethereumKitManager.ethereumKit(authData), coin.type.decimal, coin.type.fee, coin.type.address, addressParser, feeRateProvider)
        }
    }

    fun unlinkAdapter(adapter: IAdapter) {
        when (adapter) {
            is io.horizontalsystems.dynamitewallet.core.EthereumBaseAdapter -> {
                ethereumKitManager.unlink()
            }
        }
    }
}
