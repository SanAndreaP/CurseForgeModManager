package de.sanandrew.apps.cursemodmgr.util

import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.image.Image
import javafx.scene.image.PixelFormat
import javafx.scene.image.WritableImage
import javafx.stage.Stage
import javafx.util.Callback
import javafx.util.StringConverter
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

fun windowFrame(stage: () -> Stage, title: String, op: WindowFrame.() -> Unit = {}): WindowFrame {
    return WindowFrame(stage, title).apply {
        op(this)
    }
}

fun <T> getConverter(result: (obj: T) -> String): StringConverter<T> {
    return object : StringConverter<T>() {
        override fun fromString(string: String?): T? {
            return null
        }

        override fun toString(obj: T): String {
            return result(obj)
        }
    }
}

fun <T> getCellCallback(result: (obj: T) -> String): Callback<ListView<T>, ListCell<T>> {
    return Callback {
        object : ListCell<T>() {
            override fun updateItem(item: T?, empty: Boolean) {
                super.updateItem(item, empty)
                if(empty || item == null) {
                    graphic = null
                } else {
                    text = result(item)
                }
            }
        }
    }
}

fun getImgFromBase64GZip(width: Int, height: Int, b64: String): Image {
    val gzb = Base64.getDecoder().decode(b64)
    ByteArrayInputStream(gzb).use { bis ->
        var bytes = ByteArray(0)
        GZIPInputStream(bis).use { gzip ->
            bytes = gzip.readBytes()
        }

        val writableImg = WritableImage(width, height)
        writableImg.pixelWriter.setPixels(0, 0, width, height, PixelFormat.getByteBgraInstance(), bytes, 0, 4 * width)
        return writableImg
    }
}

fun writeImgToBase64GZip(img: Image): Triple<Int, Int, String> {
    val pform = PixelFormat.getByteBgraInstance()
    val width = img.width.toInt()
    val height = img.height.toInt()
    val imgBytes = ByteArray(4 * width * height)
    img.pixelReader.getPixels(0, 0, width, height, pform, imgBytes, 0, 4 * width)
    ByteArrayOutputStream().use { bos ->
        GZIPOutputStream(bos).use { gzip ->
            gzip.write(imgBytes)
        }

        return Triple(width, height, Base64.getEncoder().encodeToString(bos.toByteArray()))
    }
}

