package io.horizontalsystems.dynamitewallet.modules.syncmodule

import android.content.Context
import io.horizontalsystems.dynamitewallet.core.App
import io.horizontalsystems.dynamitewallet.core.IKeyStoreSafeExecute
import io.horizontalsystems.dynamitewallet.entities.SyncMode

object SyncModeModule {

    interface IView {
        fun updateSyncMode(syncMode: SyncMode)
        fun showError(error: Int)
        fun showConfirmationDialog()
    }

    interface IViewDelegate {
        fun viewDidLoad()
        fun onNextClick()
        fun onFastSyncModeSelect()
        fun onSlowSyncModeSelect()
        fun didConfirm(words: List<String>)
    }

    interface IInteractor {
        fun getSyncMode(): SyncMode
        fun restore(words: List<String>, syncMode: SyncMode)
    }

    interface IInteractorDelegate {
        fun didRestore()
        fun didFailToRestore(exception: Exception)
    }

    interface IRouter {
        fun navigateToSetPin()
    }

    fun start(context: Context, words: List<String>) {
        SyncModeActivity.start(context, words)
    }

    fun init(view: SyncModeViewModel, router: IRouter, keystoreSafeExecute: IKeyStoreSafeExecute) {
        val interactor = SyncModeInteractor(io.horizontalsystems.dynamitewallet.core.App.localStorage, io.horizontalsystems.dynamitewallet.core.App.authManager, io.horizontalsystems.dynamitewallet.core.App.wordsManager, keystoreSafeExecute)
        val presenter = SyncModePresenter(interactor, router, State())

        view.delegate = presenter
        presenter.view = view
        interactor.delegate = presenter
    }

    class State{
        var syncMode: SyncMode? = null
    }
}
