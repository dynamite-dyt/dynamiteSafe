package io.horizontalsystems.dynamitewallet.modules.managecoins

import android.content.Context
import android.content.Intent
import io.horizontalsystems.dynamitewallet.core.App
import io.horizontalsystems.dynamitewallet.entities.Coin

object ManageCoinsModule {

    class ManageCoinsPresenterState {
        var allCoins = listOf<Coin>()
            set(value) {
                field = value
                setDisabledCoins()
            }

        var enabledCoins = mutableListOf<Coin>()
            set(value) {
                field = value
                setDisabledCoins()
            }

        var disabledCoins = listOf<Coin>()

        fun enable(coin: Coin) {
            enabledCoins.add(coin)
            setDisabledCoins()
        }

        fun disable(coin: Coin) {
            enabledCoins.remove(coin)
            setDisabledCoins()
        }

        fun move(coin: Coin, index: Int) {
            enabledCoins.remove(coin)
            enabledCoins.add(index, coin)
        }

        private fun setDisabledCoins() {
            disabledCoins = allCoins.filter { !enabledCoins.contains(it) }
        }
    }

    interface IView {
        fun updateCoins()
        fun showFailedToSaveError()
    }

    interface IViewDelegate {
        fun viewDidLoad()
        fun enableCoin(position: Int)
        fun disableCoin(position: Int)
        fun saveChanges()
        fun moveCoin(fromIndex: Int, toIndex: Int)
        fun onClear()

        fun enabledItemForIndex(position: Int): Coin
        fun disabledItemForIndex(position: Int): Coin
        val enabledCoinsCount: Int
        val disabledCoinsCount: Int
    }

    interface IInteractor {
        fun loadCoins()
        fun saveEnabledCoins(coins: List<Coin>)
        fun clear()
    }

    interface IInteractorDelegate {
        fun didLoadEnabledCoins(enabledCoins: List<Coin>)
        fun didLoadAllCoins(allCoins: List<Coin>)
        fun didSaveChanges()
        fun didFailedToSave()
    }

    interface IRouter {
        fun close()
    }


    fun init(view: ManageCoinsViewModel, router: IRouter) {
        val interactor = ManageCoinsInteractor(io.horizontalsystems.dynamitewallet.core.App.coinManager, io.horizontalsystems.dynamitewallet.core.App.enabledCoinsStorage)
        val presenter = ManageCoinsPresenter(interactor, router, ManageCoinsPresenterState())

        view.delegate = presenter
        presenter.view = view
        interactor.delegate = presenter
    }

    fun start(context: Context) {
        val intent = Intent(context, ManageCoinsActivity::class.java)
        context.startActivity(intent)
    }

}
