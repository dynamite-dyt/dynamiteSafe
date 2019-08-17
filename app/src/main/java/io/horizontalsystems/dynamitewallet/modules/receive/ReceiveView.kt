package io.horizontalsystems.dynamitewallet.modules.receive

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import io.horizontalsystems.dynamitewallet.R
import io.horizontalsystems.dynamitewallet.core.reObserve
import io.horizontalsystems.dynamitewallet.modules.receive.viewitems.AddressItem
import io.horizontalsystems.dynamitewallet.viewHelpers.HudHelper
import io.horizontalsystems.dynamitewallet.viewHelpers.TextHelper
import kotlinx.android.synthetic.main.view_bottom_sheet_receive.view.*

class ReceiveView : ConstraintLayout {

    private lateinit var viewModel: ReceiveViewModel
    private lateinit var lifecycleOwner: LifecycleOwner
    private var listener: Listener? = null

    interface Listener {
        fun closeReceiveDialog()
        fun shareReceiveAddress(address: String)
        fun expandReceiveDialog()
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun bind(viewModel: ReceiveViewModel, lifecycleOwner: LifecycleOwner, listener: Listener) {
        inflate(context, R.layout.view_bottom_sheet_receive, this)
        this.viewModel = viewModel
        this.listener = listener
        this.lifecycleOwner = lifecycleOwner

        btnShare.setOnClickListener { viewModel.delegate.onShareClick() }
        receiveAddressView.setOnClickListener { viewModel.delegate.onAddressClick() }
        setObservers()
    }

    private val showAddressObserver = Observer<AddressItem?> { address ->
        address?.let {
            receiveCoinIcon.bind(it.coin)
            receiveTxtTitle.text = context.getString(R.string.Deposit_Title, it.coin.title)
            receiveAddressView.bind(it.address)
            imgQrCode.setImageBitmap(TextHelper.getQrCodeBitmapFromAddress(it.address))

            listener?.expandReceiveDialog()
        }
    }

    private val showErrorObserver = Observer<Int?> { error ->
        error?.let { HudHelper.showErrorMessage(it) }
        listener?.closeReceiveDialog()
    }

    private val showCopiedObserver = Observer<Unit> { HudHelper.showSuccessMessage(R.string.Hud_Text_Copied, 500) }

    private val shareAddressObserver = Observer<String?> { address ->
        address?.let {
            listener?.shareReceiveAddress(it)
        }
    }

    private fun setObservers() {
        viewModel.showAddressLiveData.reObserve(lifecycleOwner, showAddressObserver)
        viewModel.showErrorLiveData.reObserve(lifecycleOwner, showErrorObserver)
        viewModel.showCopiedLiveEvent.reObserve(lifecycleOwner, showCopiedObserver)
        viewModel.shareAddressLiveEvent.reObserve(lifecycleOwner, shareAddressObserver)
    }

}
