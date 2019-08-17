package io.horizontalsystems.dynamitewallet.core.managers

import io.horizontalsystems.dynamitewallet.core.IAppConfigProvider
import io.horizontalsystems.dynamitewallet.core.ICurrencyManager
import io.horizontalsystems.dynamitewallet.core.ILocalStorage
import io.horizontalsystems.dynamitewallet.entities.Currency
import io.reactivex.subjects.PublishSubject

class CurrencyManager(private val localStorage: ILocalStorage, private val appConfigProvider: IAppConfigProvider) : ICurrencyManager {

    override val baseCurrency: Currency
        get() {
            val currencies = appConfigProvider.currencies
            val storedCode = localStorage.baseCurrencyCode
            return storedCode?.let { code ->
                currencies.first { it.code == code }
            } ?: currencies.first()
        }

    override val currencies: List<Currency>
        get() = appConfigProvider.currencies

    override val baseCurrencyUpdatedSignal = PublishSubject.create<Unit>()

    override fun setBaseCurrency(code: String) {
        localStorage.baseCurrencyCode = code

        baseCurrencyUpdatedSignal.onNext(Unit)
    }

}
