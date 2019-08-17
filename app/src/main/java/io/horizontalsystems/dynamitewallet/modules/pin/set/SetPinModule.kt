package io.horizontalsystems.dynamitewallet.modules.pin.set

import io.horizontalsystems.dynamitewallet.core.App
import io.horizontalsystems.dynamitewallet.core.IKeyStoreSafeExecute
import io.horizontalsystems.dynamitewallet.modules.pin.PinInteractor
import io.horizontalsystems.dynamitewallet.modules.pin.PinViewModel

object SetPinModule {

    interface ISetPinRouter {
        fun navigateToMain()
    }

    fun init(view: PinViewModel, router: ISetPinRouter, keystoreSafeExecute: IKeyStoreSafeExecute) {

        val interactor = PinInteractor(io.horizontalsystems.dynamitewallet.core.App.pinManager, io.horizontalsystems.dynamitewallet.core.App.authManager, keystoreSafeExecute)
        val presenter = SetPinPresenter(interactor, router)

        view.delegate = presenter
        presenter.view = view
        interactor.delegate = presenter
    }

}
