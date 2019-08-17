package io.horizontalsystems.dynamitewallet.entities

import java.util.*

sealed class LockoutState {
    data class Unlocked(val attemptsLeft: Int?) : LockoutState()
    data class Locked(val until: Date) : LockoutState()
}
