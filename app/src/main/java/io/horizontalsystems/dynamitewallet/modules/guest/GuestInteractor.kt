package io.horizontalsystems.dynamitewallet.modules.guest

import io.horizontalsystems.dynamitewallet.core.IKeyStoreSafeExecute
import io.horizontalsystems.dynamitewallet.core.ISystemInfoManager
import io.horizontalsystems.dynamitewallet.core.IWordsManager
import io.horizontalsystems.dynamitewallet.core.managers.AuthManager
import io.horizontalsystems.dynamitewallet.entities.SyncMode

class GuestInteractor(
        private val authManager: AuthManager,
        private val wordsManager: IWordsManager,
        private val keystoreSafeExecute: IKeyStoreSafeExecute,
        systemInfoManager: ISystemInfoManager) : GuestModule.IInteractor {

    var delegate: GuestModule.IInteractorDelegate? = null

    override val appVersion: String = systemInfoManager.appVersion

    override fun createWallet() {
        keystoreSafeExecute.safeExecute(
                action = Runnable { authManager.login(wordsManager.generateWords(), SyncMode.NEW) },
                onSuccess = Runnable { delegate?.didCreateWallet() },
                onFailure = Runnable { delegate?.didFailToCreateWallet() }
        )
    }

}
