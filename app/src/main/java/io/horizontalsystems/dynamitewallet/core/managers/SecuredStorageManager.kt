package io.horizontalsystems.dynamitewallet.core.managers

import android.text.TextUtils
import io.horizontalsystems.dynamitewallet.core.App
import io.horizontalsystems.dynamitewallet.core.IEncryptionManager
import io.horizontalsystems.dynamitewallet.core.ISecuredStorage
import io.horizontalsystems.dynamitewallet.entities.AuthData


class SecuredStorageManager(private val encryptionManager: IEncryptionManager) : ISecuredStorage {

    private val AUTH_DATA = "auth_data"
    private val LOCK_PIN = "lock_pin"


    override val authData: AuthData?
        get() {
            io.horizontalsystems.dynamitewallet.core.App.preferences.getString(AUTH_DATA, null)?.let { string ->
                return AuthData(encryptionManager.decrypt(string))
            }
            return null
        }

    override fun saveAuthData(authData: AuthData) {
        io.horizontalsystems.dynamitewallet.core.App.preferences.edit().putString(AUTH_DATA, encryptionManager.encrypt(authData.toString())).apply()
    }

    override fun noAuthData(): Boolean {
        val words = io.horizontalsystems.dynamitewallet.core.App.preferences.getString(AUTH_DATA, null)
        return words.isNullOrEmpty()
    }

    override val savedPin: String?
        get() {
            val string = io.horizontalsystems.dynamitewallet.core.App.preferences.getString(LOCK_PIN, null)
            return if (TextUtils.isEmpty(string)) {
                null
            } else {
                encryptionManager.decrypt(string)
            }
        }

    override fun savePin(pin: String) {
        io.horizontalsystems.dynamitewallet.core.App.preferences.edit().putString(LOCK_PIN, encryptionManager.encrypt(pin)).apply()
    }

    override fun pinIsEmpty(): Boolean {
        val string = io.horizontalsystems.dynamitewallet.core.App.preferences.getString(LOCK_PIN, null)
        return string.isNullOrEmpty()
    }

}
