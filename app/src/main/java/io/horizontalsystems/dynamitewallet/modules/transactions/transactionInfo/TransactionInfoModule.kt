package io.horizontalsystems.dynamitewallet.modules.transactions.transactionInfo

import io.horizontalsystems.dynamitewallet.entities.Coin
import io.horizontalsystems.dynamitewallet.modules.transactions.CoinCode
import io.horizontalsystems.dynamitewallet.viewHelpers.TextHelper

object TransactionInfoModule {
    interface View {
        fun showCopied()
    }

    interface ViewDelegate {
        fun onCopy(value: String)
        fun openFullInfo(transactionHash: String, coin: Coin)
    }

    interface Interactor {
        fun onCopy(value: String)
    }

    interface InteractorDelegate

    interface Router {
        fun openFullInfo(transactionHash: String, coin: Coin)
    }

    fun init(view: TransactionInfoViewModel, router: Router) {
        val interactor = TransactionInfoInteractor(TextHelper)
        val presenter = TransactionInfoPresenter(interactor, router)

        view.delegate = presenter
        presenter.view = view
        interactor.delegate = presenter
    }

}
