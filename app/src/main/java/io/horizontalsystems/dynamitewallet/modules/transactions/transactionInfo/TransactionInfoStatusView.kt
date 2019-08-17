package io.horizontalsystems.dynamitewallet.modules.transactions.transactionInfo

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import io.horizontalsystems.dynamitewallet.R
import io.horizontalsystems.dynamitewallet.core.App
import io.horizontalsystems.dynamitewallet.modules.transactions.TransactionStatus
import io.horizontalsystems.dynamitewallet.viewHelpers.LayoutHelper
import kotlinx.android.synthetic.main.view_transaction_info_status.view.*


open class TransactionInfoStatusView : ConstraintLayout {

    constructor(context: Context) : super(context) {
        initializeViews()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initializeViews()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initializeViews()
    }

    private fun initializeViews() {
        inflate(context, R.layout.view_transaction_info_status, this)
    }

    fun bind(transactionStatus: TransactionStatus) {
        progressBarWrapper.visibility = View.GONE
        statusIcon.visibility = View.GONE
        statusPendingText.visibility = View.GONE
        when (transactionStatus) {
            is TransactionStatus.Completed -> {
                statusIcon.setImageDrawable(LayoutHelper.d(R.drawable.surface1, io.horizontalsystems.dynamitewallet.core.App.instance))
                statusIcon.visibility = View.VISIBLE
                statusPendingText.visibility = View.VISIBLE
                statusPendingText.text = context.getString(R.string.TransactionInfo_Status_Confirmed)
            }
            is TransactionStatus.Processing -> {
                progressBarWrapper.visibility = View.VISIBLE
                fillProgress(transactionStatus)
            }
            else -> {
                statusIcon.setImageDrawable(LayoutHelper.d(R.drawable.ic_pending_grey, io.horizontalsystems.dynamitewallet.core.App.instance))
                statusIcon.visibility = View.VISIBLE
                statusPendingText.visibility = View.VISIBLE
                statusPendingText.text = context.getString(R.string.TransactionInfo_Status_Pending)
            }
        }
        invalidate()
    }

    private fun fillProgress(transactionStatus: TransactionStatus.Processing) {
        val greyBar = R.drawable.status_progress_bar_grey
        val greenBar = R.drawable.status_progress_bar_green

        progressBar1.setImageResource(if (transactionStatus.progress >= 1) greenBar else greyBar)
        progressBar2.setImageResource(if (transactionStatus.progress >= 2) greenBar else greyBar)
        progressBar3.setImageResource(if (transactionStatus.progress >= 3) greenBar else greyBar)
        progressBar4.setImageResource(if (transactionStatus.progress >= 4) greenBar else greyBar)
        progressBar5.setImageResource(if (transactionStatus.progress >= 5) greenBar else greyBar)
        progressBar6.setImageResource(if (transactionStatus.progress >= 6) greenBar else greyBar)
    }

}
