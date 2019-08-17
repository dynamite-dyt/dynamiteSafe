package io.horizontalsystems.dynamitewallet.modules.guest

import android.content.Context
import android.content.Intent
import io.horizontalsystems.dynamitewallet.core.App
import io.horizontalsystems.dynamitewallet.core.IKeyStoreSafeExecute

object GuestModule {

    interface IView {
        fun showError()
        fun setAppVersion(appVersion: String)
    }

    interface IViewDelegate {
        fun createWalletDidClick()
        fun restoreWalletDidClick()
        fun onViewDidLoad()
    }

    interface IInteractor {
        fun createWallet()
        val appVersion: String
    }

    interface IInteractorDelegate {
        fun didCreateWallet()
        fun didFailToCreateWallet()
    }

    interface IRouter {
        fun navigateToBackupRoutingToMain()
        fun navigateToRestore()
    }

    fun start(context: Context) {
        val intent = Intent(context, GuestActivity::class.java)
        context.startActivity(intent)
    }

    fun init(view: GuestViewModel, router: IRouter, keystoreSafeExecute: IKeyStoreSafeExecute) {
        val interactor = GuestInteractor(io.horizontalsystems.dynamitewallet.core.App.authManager, io.horizontalsystems.dynamitewallet.core.App.wordsManager, keystoreSafeExecute, io.horizontalsystems.dynamitewallet.core.App.systemInfoManager)
        val presenter = GuestPresenter(interactor, router)

        view.delegate = presenter
        presenter.view = view
        interactor.delegate = presenter
    }

}
