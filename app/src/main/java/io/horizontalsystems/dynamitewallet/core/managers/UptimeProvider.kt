package io.horizontalsystems.dynamitewallet.core.managers

import android.os.SystemClock
import io.horizontalsystems.dynamitewallet.core.IUptimeProvider

class UptimeProvider: IUptimeProvider {

    override val uptime: Long
        get() = SystemClock.elapsedRealtime()

}
