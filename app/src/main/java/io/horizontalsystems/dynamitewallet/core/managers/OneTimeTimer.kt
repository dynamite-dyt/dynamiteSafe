package io.horizontalsystems.dynamitewallet.core.managers

import io.horizontalsystems.dynamitewallet.core.IOneTimerDelegate
import java.util.*

class OneTimeTimer {

    var delegate: IOneTimerDelegate? = null
    private var timer: Timer? = null

    fun schedule(time: Date) {

        if(timer == null) {
            timer = Timer()
        }

        timer?.schedule(object : TimerTask() {
            override fun run() {
                delegate?.onFire()
            }
        }, time)

    }
}
