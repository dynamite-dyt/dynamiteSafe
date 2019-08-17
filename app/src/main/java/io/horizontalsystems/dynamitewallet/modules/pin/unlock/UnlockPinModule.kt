package io.horizontalsystems.dynamitewallet.modules.pin.unlock

import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import io.horizontalsystems.dynamitewallet.core.App
import io.horizontalsystems.dynamitewallet.core.IKeyStoreSafeExecute
import io.horizontalsystems.dynamitewallet.core.factories.LockoutUntilDateFactory
import io.horizontalsystems.dynamitewallet.core.managers.CurrentDateProvider
import io.horizontalsystems.dynamitewallet.core.managers.LockoutManager
import io.horizontalsystems.dynamitewallet.core.managers.OneTimeTimer
import io.horizontalsystems.dynamitewallet.core.managers.UptimeProvider
import io.horizontalsystems.dynamitewallet.entities.LockoutState
import io.horizontalsystems.dynamitewallet.modules.pin.PinViewModel

object UnlockPinModule {
    interface IUnlockPinRouter {
        fun dismiss()
        fun closeApplication()
    }

    interface IUnlockPinInteractor {
        fun updateLockoutState()
        fun cacheSecuredData()
        fun unlock(pin: String): Boolean
        fun isBiometricOn(): Boolean
        fun onUnlock()
    }

    interface IUnlockPinInteractorDelegate {
        fun didBiometricUnlock()
        fun unlock()
        fun wrongPinSubmitted()
        fun setCryptoObject(cryptoObject: FingerprintManagerCompat.CryptoObject)
        fun updateLockoutState(state: LockoutState)
    }

    fun init(view: PinViewModel, router: IUnlockPinRouter, keystoreSafeExecute: IKeyStoreSafeExecute, showCancelButton: Boolean) {

        val lockoutManager = LockoutManager(io.horizontalsystems.dynamitewallet.core.App.localStorage, UptimeProvider(), LockoutUntilDateFactory(CurrentDateProvider()))
        val timer = OneTimeTimer()
        val interactor = UnlockPinInteractor(keystoreSafeExecute, io.horizontalsystems.dynamitewallet.core.App.localStorage, io.horizontalsystems.dynamitewallet.core.App.authManager, io.horizontalsystems.dynamitewallet.core.App.pinManager, io.horizontalsystems.dynamitewallet.core.App.lockManager, io.horizontalsystems.dynamitewallet.core.App.encryptionManager, lockoutManager, timer)
        val presenter = UnlockPinPresenter(interactor, router, showCancelButton)

        view.delegate = presenter
        presenter.view = view
        interactor.delegate = presenter
    }
}
