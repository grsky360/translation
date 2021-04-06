package ilio.translation.utils

import java.awt.Image
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException

val clipboard = Clipboard

object Clipboard {
    private val clipboard: java.awt.datatransfer.Clipboard by lazy { Toolkit.getDefaultToolkit().systemClipboard }

    fun writeTextClipboard(value: String) {
        val transferable = StringSelection(value)
        clipboard.setContents(transferable, null)
    }

    fun writeImageClipboard(value: Image) {
        val transferable = ImageTransferable(value)
        clipboard.setContents(transferable, null)
    }

    fun readTextClipboard(): String? {
        val transferable = clipboard.getContents(null)
        if (transferable == null || !transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return null
        }
        return try {
            val result = transferable.getTransferData(DataFlavor.stringFlavor)
            if (result is String) result else null
        } catch (e: Exception) {
            null
        }
    }

    fun readImageClipboard(): Image? {
        val transferable = clipboard.getContents(null)
        if (transferable == null || !transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            return null
        }
        return try {
            val result = transferable.getTransferData(DataFlavor.imageFlavor)
            if (result is Image) result else null
        } catch (e: java.lang.Exception) {
            null
        }
    }
}

internal class ImageTransferable(private val image: Image) : Transferable {
    override fun getTransferDataFlavors(): Array<DataFlavor> = arrayOf(DataFlavor.imageFlavor)

    override fun isDataFlavorSupported(flavor: DataFlavor?): Boolean = flavor == DataFlavor.imageFlavor

    override fun getTransferData(flavor: DataFlavor?): Any {
        if (!isDataFlavorSupported(flavor)) {
            throw UnsupportedFlavorException(flavor)
        }
        return image
    }

}
