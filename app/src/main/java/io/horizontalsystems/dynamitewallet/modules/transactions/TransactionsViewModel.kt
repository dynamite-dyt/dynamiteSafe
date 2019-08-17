package io.horizontalsystems.dynamitewallet.modules.transactions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import io.horizontalsystems.dynamitewallet.SingleLiveEvent
import io.horizontalsystems.dynamitewallet.entities.Coin
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class TransactionsViewModel : ViewModel(), TransactionsModule.IView, TransactionsModule.IRouter {

    lateinit var delegate: TransactionsModule.IViewDelegate

    val filterItems = MutableLiveData<List<Coin?>>()
    val transactionViewItemLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<TransactionViewItem>()
    val reloadChangeEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<DiffUtil.DiffResult>()
    val reloadLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Unit>()
    val reloadItemsLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<List<Int>>()
    val addItemsLiveEvent = io.horizontalsystems.dynamitewallet.SingleLiveEvent<Pair<Int, Int>>()

    private var flushSubject = PublishSubject.create<Unit>()
    private var indexesToUpdate = mutableListOf<Int>()
    private val disposables = CompositeDisposable()

    fun init() {
        TransactionsModule.initModule(this, this)
        delegate.viewDidLoad()

        flushSubject
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { unit -> reloadWithBuffer() }
                .subscribe()?.let { disposables.add(it) }
    }

    override fun showFilters(filters: List<Coin?>) {
        filterItems.postValue(filters)
    }

    override fun reload() {
        reloadLiveEvent.postValue(Unit)
    }

    override fun reloadChange(diff: DiffUtil.DiffResult) {
        reloadChangeEvent.postValue(diff)
    }

    override fun reloadItems(updatedIndexes: List<Int>) {
        indexesToUpdate.addAll(updatedIndexes)
        indexesToUpdate = indexesToUpdate.distinct().toMutableList()
        flushSubject.onNext(Unit)
    }

    private fun reloadWithBuffer() {
        reloadItemsLiveEvent.value = indexesToUpdate
        indexesToUpdate.clear()
    }

    override fun addItems(fromIndex: Int, count: Int) {
        if (fromIndex == 0) {
            reload()
        } else {
            addItemsLiveEvent.postValue(Pair(fromIndex, count))
        }
    }

    override fun openTransactionInfo(transactionViewItem: TransactionViewItem) {
        transactionViewItemLiveEvent.value = transactionViewItem
    }

    override fun onCleared() {
        delegate.onClear()
    }
}
