package io.horizontalsystems.dynamitewallet.core.managers

import io.horizontalsystems.dynamitewallet.BuildConfig
import io.horizontalsystems.dynamitewallet.R
import io.horizontalsystems.dynamitewallet.core.App
import io.horizontalsystems.dynamitewallet.core.IAppConfigProvider
import io.horizontalsystems.dynamitewallet.entities.Coin
import io.horizontalsystems.dynamitewallet.entities.CoinType
import io.horizontalsystems.dynamitewallet.entities.Currency
import java.math.BigDecimal

class AppConfigProvider : IAppConfigProvider {

    override val ipfsId = "QmXTJZBMMRmBbPun6HFt3tmb3tfYF2usLPxFoacL7G5uMX"
    override val ipfsMainGateway = "ipfs-ext.horizontalsystems.xyz"
    override val ipfsFallbackGateway = "ipfs.io"
    override val dytMainGateway = "dyt.meany.xyz"
    override val infuraProjectId = "2a1306f1d12f4c109a4d4fb9be46b02e"
    override val infuraProjectSecret = "fc479a9290b64a84a15fa6544a130218"

    override val fiatDecimal: Int = 2
    override val maxDecimal: Int = 8

    override val testMode: Boolean = BuildConfig.testMode

    override val currencies: List<Currency> = listOf(
            Currency(code = "USD", symbol = "\u0024")
    )

    override val localizations: List<String>
        get() {
            val coinsString = io.horizontalsystems.dynamitewallet.core.App.instance.getString(R.string.localizations)
            return coinsString.split(",")
        }
    override val defaultCoinCodes: List<String>
        get() = listOf("DYT", "ETH")

    override val coins: List<Coin> = listOf(
            Coin("Dynamite Token", "DYT", CoinType.Erc20("0xad95a3c0fdc9bc4b27fd79e028a0a808d5564aa4", 18)),
            Coin("Ethereum", "ETH", CoinType.Ethereum)
    )
}

