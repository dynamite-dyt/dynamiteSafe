package io.horizontalsystems.dynamitewallet.viewHelpers

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import io.horizontalsystems.dynamitewallet.core.App
import io.horizontalsystems.dynamitewallet.core.IClipboardManager


object TextHelper : IClipboardManager {

    override val hasPrimaryClip: Boolean
        get() = clipboard?.hasPrimaryClip() ?: false

    override fun copyText(text: String) {
        copyTextToClipboard(io.horizontalsystems.dynamitewallet.core.App.instance, text)
    }

    override fun getCopiedText(): String {
        return clipboard?.primaryClip?.itemCount?.let { count ->
            if (count > 0) {
                clipboard?.primaryClip?.getItemAt(0)?.text?.toString()
            } else {
                null
            }
        } ?: ""
    }

    fun getQrCodeBitmapFromAddress(address: String): Bitmap? {
        val multiFormatWriter = MultiFormatWriter()
        return try {
            val imageSize = LayoutHelper.dp(150F, io.horizontalsystems.dynamitewallet.core.App.instance)
            val bitMatrix = multiFormatWriter.encode(address, BarcodeFormat.QR_CODE, imageSize, imageSize, hashMapOf(EncodeHintType.MARGIN to 0))
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.createBitmap(bitMatrix)
            bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    fun getCleanCoinCode(coin: String): String {
        var cleanedCoin = coin.removeSuffix("t")
        cleanedCoin = cleanedCoin.removeSuffix("r")
        return cleanedCoin
    }

    private val clipboard: ClipboardManager?
        get() = io.horizontalsystems.dynamitewallet.core.App.instance.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager

    private fun copyTextToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText("text", text)
        clipboard?.primaryClip = clip
    }

}
