package io.horizontalsystems.dynamitewallet.core.managers

import android.app.KeyguardManager
import android.content.Context
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import io.horizontalsystems.dynamitewallet.BuildConfig
import io.horizontalsystems.dynamitewallet.core.App
import io.horizontalsystems.dynamitewallet.core.ISystemInfoManager
import io.horizontalsystems.dynamitewallet.entities.BiometryType

class SystemInfoManager: ISystemInfoManager {
    override var appVersion: String = BuildConfig.VERSION_NAME

    override var biometryType: BiometryType = BiometryType.NONE
        get() {
            return when {
                phoneHasFingerprintSensor() -> BiometryType.FINGER
                else -> BiometryType.NONE
            }
        }

    override fun phoneHasFingerprintSensor(): Boolean {
        val fingerprintManager = FingerprintManagerCompat.from(io.horizontalsystems.dynamitewallet.core.App.instance)
        return fingerprintManager.isHardwareDetected
    }

    override fun touchSensorCanBeUsed(): Boolean {
        val fingerprintManager = FingerprintManagerCompat.from(io.horizontalsystems.dynamitewallet.core.App.instance)
        return when {
            fingerprintManager.isHardwareDetected -> {
                val keyguardManager = io.horizontalsystems.dynamitewallet.core.App.instance.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                keyguardManager.isKeyguardSecure && fingerprintManager.hasEnrolledFingerprints()
            }
            else -> false
        }
    }
}
