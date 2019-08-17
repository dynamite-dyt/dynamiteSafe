package io.horizontalsystems.dynamitewallet.core.managers

import android.os.Build
import io.horizontalsystems.dynamitewallet.core.App
import io.horizontalsystems.dynamitewallet.core.IAppConfigProvider
import io.horizontalsystems.dynamitewallet.core.ILanguageManager
import io.horizontalsystems.dynamitewallet.core.ILocalStorage
import java.util.*


class LanguageManager(
        private val localStorage: ILocalStorage,
        private val appConfigProvider: IAppConfigProvider,
        fallbackLanguage: Locale) : ILanguageManager {

    override val availableLanguages: List<Locale>
        get() = appConfigProvider.localizations.map { Locale(it) }

    override var currentLanguage: Locale
        get() = language
        set(value) {
            language = value
            localStorage.currentLanguage = value.language

            val configuration = io.horizontalsystems.dynamitewallet.core.App.instance.resources.configuration
            configuration.setLocale(value)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                io.horizontalsystems.dynamitewallet.core.App.instance.createConfigurationContext(configuration)
            } else {
                val displayMetrics = io.horizontalsystems.dynamitewallet.core.App.instance.resources.displayMetrics
                io.horizontalsystems.dynamitewallet.core.App.instance.resources.updateConfiguration(configuration, displayMetrics)
            }
        }

    private var language: Locale = localStorage.currentLanguage?.let { Locale(localStorage.currentLanguage) }
            ?: preferredSystemLanguage ?: fallbackLanguage

    private val preferredSystemLanguage: Locale?
        get() {
            return when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> io.horizontalsystems.dynamitewallet.core.App.instance.resources.configuration.locales.get(0)
                else -> io.horizontalsystems.dynamitewallet.core.App.instance.resources.configuration.locale
            }
        }

}
