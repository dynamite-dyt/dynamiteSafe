package io.horizontalsystems.dynamitewallet.modules.pin.edit

import io.horizontalsystems.dynamitewallet.core.App
import io.horizontalsystems.dynamitewallet.core.IKeyStoreSafeExecute
import io.horizontalsystems.dynamitewallet.modules.pin.PinInteractor
import io.horizontalsystems.dynamitewallet.modules.pin.PinViewModel

object EditPinModule {

    interface IEditPinRouter {
        fun dismiss()
    }

    fun init(view: PinViewModel, router: IEditPinRouter, keystoreSafeExecute: IKeyStoreSafeExecute) {

        val interactor = PinInteractor(io.horizontalsystems.dynamitewallet.core.App.pinManager, io.horizontalsystems.dynamitewallet.core.App.authManager, keystoreSafeExecute)
        val presenter = EditPinPresenter(interactor, router)

        view.delegate = presenter
        presenter.view = view
        interactor.delegate = presenter
    }

}
