package io.horizontalsystems.dynamitewallet.core.managers

import io.horizontalsystems.dynamitewallet.core.ICurrentDateProvider
import java.util.*

class CurrentDateProvider: ICurrentDateProvider {

    override val currentDate: Date
        get() = Date()

}
