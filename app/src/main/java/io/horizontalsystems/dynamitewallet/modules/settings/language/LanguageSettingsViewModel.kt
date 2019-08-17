package io.horizontalsystems.dynamitewallet.modules.settings.language

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.horizontalsystems.dynamitewallet.SingleLiveEvent

class LanguageSettingsViewModel: ViewModel(), LanguageSettingsModule.ILanguageSettingsView, LanguageSettingsModule.ILanguageSettingsRouter {

    lateinit var delegate: LanguageSettingsModule.ILanguageSettingsViewDelegate
    val languageItems = MutableLiveData<List<LanguageItem>>()
    val reloadAppLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Unit>()

    fun init() {
        LanguageSettingsModule.init(this, this)
        delegate.viewDidLoad()
    }

    override fun show(items: List<LanguageItem>) {
        languageItems.value = items
    }

    override fun reloadAppInterface() {
        reloadAppLiveEvent.call()
    }
}
