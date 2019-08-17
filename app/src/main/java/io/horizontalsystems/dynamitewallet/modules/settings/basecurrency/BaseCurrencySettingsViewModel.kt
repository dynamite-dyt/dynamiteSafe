package io.horizontalsystems.dynamitewallet.modules.settings.basecurrency

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.horizontalsystems.dynamitewallet.SingleLiveEvent

class BaseCurrencySettingsViewModel: ViewModel(), BaseCurrencySettingsModule.IBaseCurrencySettingsView {

    lateinit var delegate: BaseCurrencySettingsModule.IBaseCurrencySettingsViewDelegate
    val currencyItems = MutableLiveData<List<CurrencyItem>>()
    val closeLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Unit>()


    fun init() {
        BaseCurrencySettingsModule.init(this)
        delegate.viewDidLoad()
    }

    override fun show(items: List<CurrencyItem>) {
        currencyItems.value = items
    }

    override fun close() {
        closeLiveEvent.call()
    }
}
