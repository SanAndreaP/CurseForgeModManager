package de.sanandrew.apps.cursemodmgr

import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.image.Image
import javafx.scene.image.PixelFormat
import javafx.scene.image.WritableImage
import javafx.scene.input.MouseButton
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.util.Callback
import javafx.util.StringConverter
import tornadofx.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.Base64
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

fun javafx.event.EventTarget.cstWindowFrame(view: UIComponent, content: Node, hasMinBtn: Boolean = false, hasMaxRstBtn: Boolean = false,
                                            cstCloseAction: (() -> Unit)? = null,
                                            canResize: Boolean = true,
                                            contentPadding: Insets = Insets(5.0, 15.0, 0.0, 15.0)): javafx.scene.Parent
{
    return vbox {
        var xOffsetWindow = 0.0
        var yOffsetWindow = 0.0

        val showRestored = SimpleObjectProperty(currStage(view).isMaximized && hasMaxRstBtn)
        val showMaximized = SimpleObjectProperty(!currStage(view).isMaximized && hasMaxRstBtn)
        val showMinimized = SimpleObjectProperty(hasMinBtn)

        var dragCursor = Cursor.DEFAULT

        addClass("windowBg")

        alignment = Pos.TOP_CENTER

        hbox {
            addClass("windowHeader")

            minHeight = 30.0
            alignment = Pos.CENTER_RIGHT

            label(view.titleProperty) {
                maxWidth = Double.MAX_VALUE
                hgrow = Priority.ALWAYS
                paddingLeft = 10.0
            }
            button("\ue0bd") {
                addClass("minBtn")
                prefWidth = 30.0
                prefHeight = 30.0
                visibleWhen(showMinimized)
                managedProperty().bind(showMinimized)
                action {
                    MainApp.minimizeApp(view)
                }
            }
            button("\ue10e") {
                addClass("resBtn")
                prefWidth = 30.0
                prefHeight = 30.0
                visibleWhen(showRestored)
                managedProperty().bind(showRestored)
                action {
                    MainApp.restoreApp(view)
                    showRestored.set(false)
                    showMaximized.set(true)
                }
            }
            button("\ue012") {
                addClass("maxBtn")
                prefWidth = 30.0
                prefHeight = 30.0
                visibleWhen(showMaximized)
                managedProperty().bind(showMaximized)
                action {
                    MainApp.maximizeApp(view)
                    showRestored.set(true)
                    showMaximized.set(false)
                }
            }
            button("\ue122") {
                addClass("closeBtn")
                prefWidth = 30.0
                prefHeight = 30.0
                action {
                    if( cstCloseAction == null ) {
                        MainApp.closeApp(view)
                    } else {
                        cstCloseAction()
                    }
                }
            }

            setOnMouseDragged { event ->
                val stage = currStage(view)
                if( dragCursor == Cursor.DEFAULT && !stage.isMaximized ) {
                    stage.x = event.screenX + xOffsetWindow
                    stage.y = event.screenY + yOffsetWindow
                    Config.instance.updateValues(view.currentStage)
                }
            }
            setOnMouseClicked { event ->
                if( event.button == MouseButton.PRIMARY && event.clickCount == 2 && canResize ) {
                    when {
                        currStage(view).isMaximized -> MainApp.restoreApp(view)
                        else -> MainApp.maximizeApp(view)
                    }
                }
            }
        }
        hbox {
            vgrow = Priority.ALWAYS
            padding = contentPadding

            addClass("windowContent")

            add(content)
        }
        setOnMousePressed { event ->
            val stage = currStage(view)
            if( !stage.isMaximized ) {
                xOffsetWindow = stage.x - event.screenX
                yOffsetWindow = stage.y - event.screenY
            }
        }
        setOnMouseDragged { event ->
            val stage = currStage(view)
            if( !stage.isMaximized && canResize ) {
                val curX = event.screenX + xOffsetWindow
                val curY = event.screenY + yOffsetWindow
                if( dragCursor == Cursor.N_RESIZE || dragCursor == Cursor.NE_RESIZE || dragCursor == Cursor.NW_RESIZE ) {
                    val dY = stage.y - curY
                    stage.y = curY
                    stage.height += dY
                }
                if( dragCursor == Cursor.W_RESIZE || dragCursor == Cursor.NW_RESIZE || dragCursor == Cursor.SW_RESIZE ) {
                    val dX = stage.x - curX
                    stage.x = curX
                    stage.width += dX
                }
                if( dragCursor == Cursor.S_RESIZE || dragCursor == Cursor.SW_RESIZE || dragCursor == Cursor.SE_RESIZE ) {
                    stage.height = event.screenY - stage.y
                }
                if( dragCursor == Cursor.E_RESIZE || dragCursor == Cursor.NE_RESIZE || dragCursor == Cursor.SE_RESIZE ) {
                    stage.width = event.screenX - stage.x
                }

                Config.instance.updateValues(view.currentStage)
            }
        }
        setOnMouseMoved { event ->
            val stage = currStage(view)
            val frame = 3.0
            when {
                stage.isMaximized || !canResize -> stage.scene.cursor = Cursor.DEFAULT
                event.x <= frame -> when {
                    event.y <= frame -> stage.scene.cursor = Cursor.NW_RESIZE
                    event.y >= stage.height - frame -> stage.scene.cursor = Cursor.SW_RESIZE
                    else -> stage.scene.cursor = Cursor.W_RESIZE
                }
                event.x >= stage.width - frame -> when {
                    event.y <= frame -> stage.scene.cursor = Cursor.NE_RESIZE
                    event.y >= stage.height - frame -> stage.scene.cursor = Cursor.SE_RESIZE
                    else -> stage.scene.cursor = Cursor.E_RESIZE
                }
                event.y <= frame -> stage.scene.cursor = Cursor.N_RESIZE
                event.y >= stage.height - frame -> stage.scene.cursor = Cursor.S_RESIZE
                else -> stage.scene.cursor = Cursor.DEFAULT
            }
            dragCursor = stage.scene.cursor
        }
    }
}

fun currStage(view: UIComponent): Stage {
    @Suppress("SENSELESS_COMPARISON")
    return view.modalStage ?: if(view.root != null) view.root.scene?.window as Stage else null ?: view.primaryStage
}

fun <T> getConverter(result: (obj: T) -> String): StringConverter<T> {
    return object: StringConverter<T>() {
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
                if( empty || item == null ) {
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

fun newColor(red: Int, green: Int, blue: Int, opacity: Double = 1.0): Color {
    return Color(red.toDouble() / 255.0, green.toDouble() / 255.0, blue.toDouble() / 255.0, opacity)
}