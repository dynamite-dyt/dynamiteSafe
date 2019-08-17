package io.horizontalsystems.dynamitewallet.ui.extensions

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import io.horizontalsystems.dynamitewallet.R
import io.horizontalsystems.dynamitewallet.entities.Coin
import io.horizontalsystems.dynamitewallet.viewHelpers.LayoutHelper
import io.horizontalsystems.dynamitewallet.viewHelpers.TextHelper
import kotlinx.android.synthetic.main.view_coin_icon.view.*

class CoinIconView : ConstraintLayout {

    init {
        inflate(context, R.layout.view_coin_icon, this)
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun bind(coin: Coin) {
        if(coin.code == "DYT"){
            dynamicCoinIcon.setImageResource(R.drawable.dyt_logo)
        }else{
            dynamicCoinIcon.setImageResource(LayoutHelper.getCoinDrawableResource(
                    TextHelper.getCleanCoinCode(coin.code)
            ))
        }

    }

}
