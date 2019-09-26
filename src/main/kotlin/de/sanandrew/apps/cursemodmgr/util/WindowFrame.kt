package de.sanandrew.apps.cursemodmgr.util

import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.Screen
import javafx.stage.Stage
import tornadofx.*

@Suppress("unused", "MemberVisibilityCanBePrivate")
class WindowFrame(val stage: () -> Stage, title: String): VBox()
{
    private val showRestored = SimpleObjectProperty(false)
    private val showMaximized = SimpleObjectProperty(true)
    private val showMinimized = SimpleObjectProperty(true)
    private val showClose = SimpleObjectProperty(true)

    var canMinimize: Boolean = true
    var canResize: Boolean = true
    var canClose: Boolean = true

    private var onClosing: () -> Boolean = { true }
    fun onClosing(onClosing: () -> Boolean) {
        this.onClosing = onClosing
    }

    private var onResizing: (newLocation: Pair<Double, Double>, newSize: Pair<Double, Double>) -> Boolean = { _, _ -> true }
    fun onResizing(onResizing: (newLocation: Pair<Double, Double>, newSize: Pair<Double, Double>) -> Boolean) {
        this.onResizing = onResizing
    }

    private var onResized: () -> Unit = { }
    fun onResized(onResized: () -> Unit) {
        this.onResized = onResized
    }

    private var prevX: Double? = null
    private var prevY: Double? = null
    private var prevWidth: Double? = null
    private var prevHeight: Double? = null
    fun maximize() {
        val s = this.stage()

        val screens = Screen.getScreensForRectangle(s.x, s.y, s.x, s.y)
        val bounds = screens.getOrElse(0) { Screen.getPrimary() }.visualBounds

        if( !this.onResizing(bounds.minX to bounds.maxX, bounds.width to bounds.height) ) {
            return
        }

        prevX = s.x
        prevY = s.y
        prevWidth = s.width
        prevHeight = s.height

        s.isMaximized = true

        s.x = bounds.minX
        s.y = bounds.minY
        s.width = bounds.width
        s.height = bounds.height

        this.onResized()
    }
    fun restore() {
        if( this.prevX == null || this.prevY == null || this.prevWidth == null || this.prevHeight == null ) {
            return
        }

        if( !this.onResizing(this.prevX!! to this.prevY!!, this.prevWidth!! to this.prevHeight!!) ) {
            return
        }

        val s = this.stage()

        s.isMaximized = false

        s.x = this.prevX!!
        s.y = this.prevY!!
        s.width = this.prevWidth!!
        s.height = this.prevHeight!!

        this.onResized()
    }
    fun minimize() {
        this.stage().isIconified = true
    }
    fun initSize(w: Double, h: Double) {
        val s = this.stage()

        s.width = w
        s.height = h

        this.centerOnScreen()
    }
    fun centerOnScreen() {
        val s = this.stage()

        val screens = Screen.getScreensForRectangle(s.x, s.y, s.x, s.y)
        val bounds = screens.getOrElse(0) { Screen.getPrimary() }.visualBounds

        s.x = (bounds.width - s.width) / 2.0
        s.y = (bounds.height - s.height) / 3.0
    }

    fun initialize() {
        val s = this.stage()

        this.showRestored.set(s.isMaximized && this.canResize)
        this.showMaximized.set(!s.isMaximized && this.canResize)
        this.showMinimized.set(this.canMinimize)
        this.showClose.set(this.canClose)
    }

    init {
        var xOffsetWindow = 0.0
        var yOffsetWindow = 0.0

        var dragCursor = Cursor.DEFAULT

        addClass("windowBg")

        alignment = Pos.TOP_CENTER

        hbox {
            addClass("windowHeader")
            minHeight = 30.0
            alignment = Pos.CENTER_RIGHT

            label(title) {
                paddingLeft = 10.0
            }
            region {
                hgrow = Priority.ALWAYS
            }
            button("\ue0bd") {
                addClass("minBtn")
                prefWidth = 30.0
                prefHeight = 30.0
                visibleWhen(showMinimized)
                managedProperty().bind(showMinimized)
                action {
                    if(dragCursor == Cursor.DEFAULT) {
                        stage().isIconified = true
                    }
                }
            }
            button("\ue10e") {
                addClass("resBtn")
                prefWidth = 30.0
                prefHeight = 30.0
                visibleWhen(showRestored)
                managedProperty().bind(showRestored)
                action {
                    if(dragCursor == Cursor.DEFAULT) {
                        restore()
                        showRestored.set(false)
                        showMaximized.set(true)
                    }
                }
            }
            button("\ue012") {
                addClass("maxBtn")
                prefWidth = 30.0
                prefHeight = 30.0
                visibleWhen(showMaximized)
                managedProperty().bind(showMaximized)
                action {
                    if(dragCursor == Cursor.DEFAULT) {
                        maximize()
                        showRestored.set(true)
                        showMaximized.set(false)
                    }
                }
            }
            button("\ue122") {
                addClass("closeBtn")
                prefWidth = 30.0
                prefHeight = 30.0
                visibleWhen(showClose)
                managedProperty().bind(showClose)
                action {
                    if(dragCursor == Cursor.DEFAULT) {
                        if(onClosing()) {
                            stage().close()
                        }
                    }
                }
            }
            setOnMouseDragged { event ->
                val s = stage()
                if(dragCursor == Cursor.DEFAULT && !s.isMaximized) {
                    s.x = event.screenX + xOffsetWindow
                    s.y = event.screenY + yOffsetWindow
                }
            }
            setOnMouseClicked { event ->
                if(event.button == MouseButton.PRIMARY && event.clickCount == 2 && canResize) {
                    when {
                        stage().isMaximized -> restore()
                        else -> maximize()
                    }
                }
            }
        }
        setOnMousePressed { event ->
            val s = stage()
            if(!s.isMaximized) {
                xOffsetWindow = s.x - event.screenX
                yOffsetWindow = s.y - event.screenY
            }
        }
        addEventFilter(MouseEvent.MOUSE_DRAGGED) { event ->
            val s = stage()
            if(!s.isMaximized && canResize) {
//                Platform.runLater {
                val curX = event.screenX + xOffsetWindow
                val curY = event.screenY + yOffsetWindow
                var newLoc: Pair<Double, Double> = s.x to s.y
                var newSize: Pair<Double, Double> = s.width to s.height
                var update = false
                if(dragCursor == Cursor.N_RESIZE || dragCursor == Cursor.NE_RESIZE || dragCursor == Cursor.NW_RESIZE) {
                    newSize = newSize.copy(second = newSize.second + newLoc.second - curY)
                    newLoc = newLoc.copy(second = curY)
                    update = true
                }
                if(dragCursor == Cursor.W_RESIZE || dragCursor == Cursor.NW_RESIZE || dragCursor == Cursor.SW_RESIZE) {
                    newSize = newSize.copy(first = newSize.first + newLoc.first - curX)
                    newLoc = newLoc.copy(first = curX)
//                        newSize = newSize.copy(first = stage.width + stage.x - curX)
                    update = true
                }
                if(dragCursor == Cursor.S_RESIZE || dragCursor == Cursor.SW_RESIZE || dragCursor == Cursor.SE_RESIZE) {
                    newSize = newSize.copy(second = event.screenY - newLoc.second)
                    update = true
                }
                if(dragCursor == Cursor.E_RESIZE || dragCursor == Cursor.NE_RESIZE || dragCursor == Cursor.SE_RESIZE) {
                    newSize = newSize.copy(first = event.screenX - newLoc.first)
                    update = true
                }

                if(update) {
                    if(!onResizing(newLoc, newSize)) {
                        return@addEventFilter
                    }

                    s.width = newSize.first
                    s.height = newSize.second
                    s.x = newLoc.first
                    s.y = newLoc.second
                    s.scene.reloadStylesheets()

                    onResized()

                    event.consume()
                }
            }
        }
        addEventFilter(MouseEvent.MOUSE_MOVED) { event ->
            val s = stage()
            val frame = 3.0
            when {
                s.isMaximized || !canResize -> s.scene.cursor = Cursor.DEFAULT
                event.x <= frame -> when {
                    event.y <= frame -> s.scene.cursor = Cursor.NW_RESIZE
                    event.y >= s.height - frame -> s.scene.cursor = Cursor.SW_RESIZE
                    else -> s.scene.cursor = Cursor.W_RESIZE
                }
                event.x >= s.width - frame -> when {
                    event.y <= frame -> s.scene.cursor = Cursor.NE_RESIZE
                    event.y >= s.height - frame -> s.scene.cursor = Cursor.SE_RESIZE
                    else -> s.scene.cursor = Cursor.E_RESIZE
                }
                event.y <= frame -> s.scene.cursor = Cursor.N_RESIZE
                event.y >= s.height - frame -> s.scene.cursor = Cursor.S_RESIZE
                else -> s.scene.cursor = Cursor.DEFAULT
            }
            dragCursor = s.scene.cursor
        }
    }
}