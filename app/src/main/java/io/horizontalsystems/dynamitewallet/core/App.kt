package io.horizontalsystems.dynamitewallet.core

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.squareup.leakcanary.LeakCanary
import io.horizontalsystems.dynamitewallet.BuildConfig
import io.horizontalsystems.dynamitewallet.core.factories.AdapterFactory
import io.horizontalsystems.dynamitewallet.core.managers.*
import io.horizontalsystems.dynamitewallet.core.security.EncryptionManager
import io.horizontalsystems.dynamitewallet.core.storage.AppDatabase
import io.horizontalsystems.dynamitewallet.core.storage.EnabledCoinsRepository
import io.horizontalsystems.dynamitewallet.core.storage.RatesRepository
import io.horizontalsystems.dynamitewallet.modules.fulltransactioninfo.FullTransactionInfoFactory
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

class App : Application() {

    companion object {

        lateinit var preferences: SharedPreferences

        lateinit var feeRateProvider: io.horizontalsystems.dynamitewallet.core.IFeeRateProvider
        lateinit var secureStorage: io.horizontalsystems.dynamitewallet.core.ISecuredStorage
        lateinit var localStorage: io.horizontalsystems.dynamitewallet.core.ILocalStorage
        lateinit var encryptionManager: EncryptionManager
        lateinit var wordsManager: WordsManager
        lateinit var authManager: AuthManager
        lateinit var randomManager: io.horizontalsystems.dynamitewallet.core.IRandomProvider
        lateinit var networkManager: io.horizontalsystems.dynamitewallet.core.INetworkManager
        lateinit var currencyManager: io.horizontalsystems.dynamitewallet.core.ICurrencyManager
        lateinit var backgroundManager: BackgroundManager
        lateinit var languageManager: io.horizontalsystems.dynamitewallet.core.ILanguageManager
        lateinit var systemInfoManager: io.horizontalsystems.dynamitewallet.core.ISystemInfoManager
        lateinit var pinManager: io.horizontalsystems.dynamitewallet.core.IPinManager
        lateinit var lockManager: io.horizontalsystems.dynamitewallet.core.ILockManager
        lateinit var appConfigProvider: io.horizontalsystems.dynamitewallet.core.IAppConfigProvider
        lateinit var adapterManager: io.horizontalsystems.dynamitewallet.core.IAdapterManager
        lateinit var coinManager: CoinManager

        lateinit var rateSyncer: RateSyncer
        lateinit var rateManager: RateManager
        lateinit var networkAvailabilityManager: NetworkAvailabilityManager
        lateinit var appDatabase: AppDatabase
        lateinit var rateStorage: io.horizontalsystems.dynamitewallet.core.IRateStorage
        lateinit var enabledCoinsStorage: io.horizontalsystems.dynamitewallet.core.IEnabledCoinStorage
        lateinit var transactionInfoFactory: FullTransactionInfoFactory
        lateinit var transactionDataProviderManager: TransactionDataProviderManager
        lateinit var appCloseManager: AppCloseManager
        lateinit var ethereumKitManager: io.horizontalsystems.dynamitewallet.core.IEthereumKitManager
        lateinit var numberFormatter: io.horizontalsystems.dynamitewallet.core.IAppNumberFormatter

        lateinit var instance: io.horizontalsystems.dynamitewallet.core.App
            private set

        var lastExitDate: Long = 0
    }

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)

        if (!BuildConfig.DEBUG) {
            //Disable logging for lower levels in Release build
            Logger.getLogger("").level = Level.SEVERE
        }

        io.horizontalsystems.dynamitewallet.core.App.Companion.instance = this
        io.horizontalsystems.dynamitewallet.core.App.Companion.preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        val fallbackLanguage = Locale("en")

        io.horizontalsystems.dynamitewallet.core.App.Companion.appConfigProvider = AppConfigProvider()
        io.horizontalsystems.dynamitewallet.core.App.Companion.feeRateProvider = FeeRateProvider(io.horizontalsystems.dynamitewallet.core.App.Companion.instance, io.horizontalsystems.dynamitewallet.core.App.Companion.appConfigProvider)
        io.horizontalsystems.dynamitewallet.core.App.Companion.backgroundManager = BackgroundManager(this)
        io.horizontalsystems.dynamitewallet.core.App.Companion.encryptionManager = EncryptionManager()
        io.horizontalsystems.dynamitewallet.core.App.Companion.secureStorage = SecuredStorageManager(io.horizontalsystems.dynamitewallet.core.App.Companion.encryptionManager)
        io.horizontalsystems.dynamitewallet.core.App.Companion.ethereumKitManager = EthereumKitManager(io.horizontalsystems.dynamitewallet.core.App.Companion.appConfigProvider)

        io.horizontalsystems.dynamitewallet.core.App.Companion.appDatabase = AppDatabase.getInstance(this)
        io.horizontalsystems.dynamitewallet.core.App.Companion.rateStorage = RatesRepository(io.horizontalsystems.dynamitewallet.core.App.Companion.appDatabase)
        io.horizontalsystems.dynamitewallet.core.App.Companion.enabledCoinsStorage = EnabledCoinsRepository(io.horizontalsystems.dynamitewallet.core.App.Companion.appDatabase)
        io.horizontalsystems.dynamitewallet.core.App.Companion.localStorage = LocalStorageManager()

        io.horizontalsystems.dynamitewallet.core.App.Companion.networkManager = NetworkManager(io.horizontalsystems.dynamitewallet.core.App.Companion.appConfigProvider)
        io.horizontalsystems.dynamitewallet.core.App.Companion.rateManager = RateManager(io.horizontalsystems.dynamitewallet.core.App.Companion.rateStorage, io.horizontalsystems.dynamitewallet.core.App.Companion.networkManager)
        io.horizontalsystems.dynamitewallet.core.App.Companion.coinManager = CoinManager(io.horizontalsystems.dynamitewallet.core.App.Companion.appConfigProvider, io.horizontalsystems.dynamitewallet.core.App.Companion.enabledCoinsStorage)
        io.horizontalsystems.dynamitewallet.core.App.Companion.authManager = AuthManager(io.horizontalsystems.dynamitewallet.core.App.Companion.secureStorage, io.horizontalsystems.dynamitewallet.core.App.Companion.localStorage, io.horizontalsystems.dynamitewallet.core.App.Companion.coinManager, io.horizontalsystems.dynamitewallet.core.App.Companion.rateManager, io.horizontalsystems.dynamitewallet.core.App.Companion.ethereumKitManager, io.horizontalsystems.dynamitewallet.core.App.Companion.appConfigProvider)

        io.horizontalsystems.dynamitewallet.core.App.Companion.wordsManager = WordsManager(io.horizontalsystems.dynamitewallet.core.App.Companion.localStorage)
        io.horizontalsystems.dynamitewallet.core.App.Companion.randomManager = RandomProvider()
        io.horizontalsystems.dynamitewallet.core.App.Companion.systemInfoManager = SystemInfoManager()
        io.horizontalsystems.dynamitewallet.core.App.Companion.pinManager = PinManager(io.horizontalsystems.dynamitewallet.core.App.Companion.secureStorage)
        io.horizontalsystems.dynamitewallet.core.App.Companion.lockManager = LockManager(io.horizontalsystems.dynamitewallet.core.App.Companion.secureStorage, io.horizontalsystems.dynamitewallet.core.App.Companion.authManager)
        io.horizontalsystems.dynamitewallet.core.App.Companion.languageManager = LanguageManager(io.horizontalsystems.dynamitewallet.core.App.Companion.localStorage, io.horizontalsystems.dynamitewallet.core.App.Companion.appConfigProvider, fallbackLanguage)
        io.horizontalsystems.dynamitewallet.core.App.Companion.currencyManager = CurrencyManager(io.horizontalsystems.dynamitewallet.core.App.Companion.localStorage, io.horizontalsystems.dynamitewallet.core.App.Companion.appConfigProvider)
        io.horizontalsystems.dynamitewallet.core.App.Companion.numberFormatter = NumberFormatter(io.horizontalsystems.dynamitewallet.core.App.Companion.languageManager)

        io.horizontalsystems.dynamitewallet.core.App.Companion.networkAvailabilityManager = NetworkAvailabilityManager()

        io.horizontalsystems.dynamitewallet.core.App.Companion.adapterManager = AdapterManager(io.horizontalsystems.dynamitewallet.core.App.Companion.coinManager, io.horizontalsystems.dynamitewallet.core.App.Companion.authManager, AdapterFactory(io.horizontalsystems.dynamitewallet.core.App.Companion.instance, io.horizontalsystems.dynamitewallet.core.App.Companion.appConfigProvider, io.horizontalsystems.dynamitewallet.core.App.Companion.localStorage, io.horizontalsystems.dynamitewallet.core.App.Companion.ethereumKitManager, io.horizontalsystems.dynamitewallet.core.App.Companion.feeRateProvider), io.horizontalsystems.dynamitewallet.core.App.Companion.ethereumKitManager)
        io.horizontalsystems.dynamitewallet.core.App.Companion.rateSyncer = RateSyncer(io.horizontalsystems.dynamitewallet.core.App.Companion.rateManager, io.horizontalsystems.dynamitewallet.core.App.Companion.adapterManager, io.horizontalsystems.dynamitewallet.core.App.Companion.currencyManager, io.horizontalsystems.dynamitewallet.core.App.Companion.networkAvailabilityManager)

        io.horizontalsystems.dynamitewallet.core.App.Companion.appCloseManager = AppCloseManager()

        io.horizontalsystems.dynamitewallet.core.App.Companion.transactionDataProviderManager = TransactionDataProviderManager(io.horizontalsystems.dynamitewallet.core.App.Companion.appConfigProvider, io.horizontalsystems.dynamitewallet.core.App.Companion.localStorage)
        io.horizontalsystems.dynamitewallet.core.App.Companion.transactionInfoFactory = FullTransactionInfoFactory(io.horizontalsystems.dynamitewallet.core.App.Companion.networkManager, io.horizontalsystems.dynamitewallet.core.App.Companion.transactionDataProviderManager)

        io.horizontalsystems.dynamitewallet.core.App.Companion.authManager.adapterManager = io.horizontalsystems.dynamitewallet.core.App.Companion.adapterManager
        io.horizontalsystems.dynamitewallet.core.App.Companion.authManager.pinManager = io.horizontalsystems.dynamitewallet.core.App.Companion.pinManager

    }

}
