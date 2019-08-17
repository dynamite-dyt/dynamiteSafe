package io.horizontalsystems.dynamitewallet.modules.managecoins

import androidx.lifecycle.ViewModel
import io.horizontalsystems.dynamitewallet.SingleLiveEvent

class ManageCoinsViewModel : ViewModel(), ManageCoinsModule.IView, ManageCoinsModule.IRouter {

    val coinsLoadedLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Void>()
    val closeLiveDate = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Void>()

    lateinit var delegate: ManageCoinsModule.IViewDelegate


    fun init() {
        ManageCoinsModule.init(this, this)
        delegate.viewDidLoad()
    }

    override fun updateCoins() {
        coinsLoadedLiveEvent.call()
    }

    override fun close() {
        closeLiveDate.call()
    }

    override fun showFailedToSaveError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCleared() {
        delegate.onClear()
    }

}
