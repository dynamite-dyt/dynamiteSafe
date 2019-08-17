package io.horizontalsystems.dynamitewallet.modules.receive

import io.horizontalsystems.dynamitewallet.core.App
import io.horizontalsystems.dynamitewallet.modules.receive.viewitems.AddressItem
import io.horizontalsystems.dynamitewallet.modules.transactions.CoinCode
import io.horizontalsystems.dynamitewallet.viewHelpers.TextHelper

object ReceiveModule {

    interface IView {
        fun showAddress(address: AddressItem)
        fun showError(error: Int)
        fun showCopied()
    }

    interface IViewDelegate {
        fun viewDidLoad()
        fun onShareClick()
        fun onAddressClick()
    }

    interface IInteractor {
        fun getReceiveAddress()
        fun copyToClipboard(coinAddress: String)
    }

    interface IInteractorDelegate {
        fun didReceiveAddress(address: AddressItem)
        fun didFailToReceiveAddress(exception: Exception)
        fun didCopyToClipboard()
    }

    interface IRouter{
        fun shareAddress(address: String)
    }

    fun init(coinCode: CoinCode?, view: ReceiveViewModel, router: IRouter) {
        val interactor = ReceiveInteractor(coinCode, io.horizontalsystems.dynamitewallet.core.App.adapterManager, TextHelper)
        val presenter = ReceivePresenter(interactor, router)

        view.delegate = presenter
        presenter.view = view
        interactor.delegate = presenter
    }

}
