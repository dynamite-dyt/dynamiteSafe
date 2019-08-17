package io.horizontalsystems.dynamitewallet.modules.settings.security

import io.horizontalsystems.dynamitewallet.core.ILocalStorage
import io.horizontalsystems.dynamitewallet.core.ILockManager
import io.horizontalsystems.dynamitewallet.core.ISystemInfoManager
import io.horizontalsystems.dynamitewallet.core.IWordsManager
import io.horizontalsystems.dynamitewallet.core.managers.AuthManager
import io.horizontalsystems.dynamitewallet.entities.BiometryType
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class SecuritySettingsInteractor(
        private val authManager: AuthManager,
        private val wordsManager: IWordsManager,
        private val localStorage: ILocalStorage,
        private val systemInfoManager: ISystemInfoManager,
        private val lockManager: ILockManager) : SecuritySettingsModule.ISecuritySettingsInteractor {

    var delegate: SecuritySettingsModule.ISecuritySettingsInteractorDelegate? = null
    private var lockStateUpdateDisposable: Disposable? = null
    private var disposables: CompositeDisposable = CompositeDisposable()

    init {
        wordsManager.backedUpSignal.subscribe {
            onUpdateBackedUp()
        }.let { disposables.add(it) }
    }

    private fun onUpdateBackedUp() {
        if (wordsManager.isBackedUp) {
            delegate?.didBackup()
        }
    }

    override val biometryType: BiometryType
        get() = systemInfoManager.biometryType

    override var isBackedUp: Boolean = wordsManager.isBackedUp

    override fun getBiometricUnlockOn(): Boolean {
        return localStorage.isBiometricOn
    }

    override fun setBiometricUnlockOn(biometricUnlockOn: Boolean) {
        localStorage.isBiometricOn = biometricUnlockOn
    }

    override fun unlinkWallet() {
        authManager.logout()
        delegate?.didUnlinkWallet()
    }

    override fun didTapOnBackupWallet() {
        delegate?.accessIsRestricted()
        lockStateUpdateDisposable?.dispose()
        lockStateUpdateDisposable = lockManager.lockStateUpdatedSignal.subscribe {
            if (!lockManager.isLocked) {
                delegate?.openBackupWallet()
                lockStateUpdateDisposable?.dispose()
            }
        }
    }

    override fun clear() {
        disposables.clear()
    }

}
