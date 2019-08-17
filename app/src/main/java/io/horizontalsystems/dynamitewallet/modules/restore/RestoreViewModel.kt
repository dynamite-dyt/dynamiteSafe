package io.horizontalsystems.dynamitewallet.modules.restore

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.horizontalsystems.dynamitewallet.SingleLiveEvent

class RestoreViewModel : ViewModel(), RestoreModule.IView, RestoreModule.IRouter {

    lateinit var delegate: RestoreModule.IViewDelegate

    val errorLiveData = MutableLiveData<Int>()
    val navigateToSetSyncModeLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<List<String>>()

    fun init() {
        RestoreModule.init(this, this)
    }

    override fun showError(error: Int) {
        errorLiveData.value = error
    }

    override fun navigateToSetSyncMode(words: List<String>) {
        navigateToSetSyncModeLiveEvent.value = words
    }

}
