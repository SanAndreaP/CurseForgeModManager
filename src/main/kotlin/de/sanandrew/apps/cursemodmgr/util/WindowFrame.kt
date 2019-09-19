package de.sanandrew.apps.cursemodmgr.util

import de.sanandrew.apps.cursemodmgr.MainApp
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.Stage
import tornadofx.*

class WindowFrame(stage: Stage, title: String): VBox()
{
    private var canMinimize: Boolean = true
    fun setMinimizable(canMinimize: Boolean) {
        this.canMinimize = canMinimize
    }

    private var canResize: Boolean = true
    fun setResizable(canResize: Boolean) {
        this.canResize = canResize
    }

    private var canClose: Boolean = true
    fun setCloseable(canClose: Boolean) {
        this.canClose = canClose
    }

    private var onClosing: () -> Boolean = { true }
    fun onClosing(onClosing: () -> Boolean) {
        this.onClosing = onClosing
    }

    private var contentPadding: Insets = insets(0)
    fun setContentPadding(padding: Insets) {
        this.contentPadding = padding
    }

    val contentBox: HBox

//    operator fun plusAssign(child: UIComponent) {
//        this.contentBox += child
//    }

    private var initFinished = false
    override fun getChildren(): ObservableList<Node> {
        return if(initFinished) this.contentBox.children else super.getChildren()
    }

    init {
        var xOffsetWindow = 0.0
        var yOffsetWindow = 0.0

        val showRestored = SimpleObjectProperty(stage.isMaximized && canResize)
        val showMaximized = SimpleObjectProperty(!stage.isMaximized && canResize)
        val showMinimized = SimpleObjectProperty(canMinimize)
        val showClose = SimpleObjectProperty(canClose)

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
                        stage.isIconified = true
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
                        MainApp.restoreApp(stage)
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
                        MainApp.maximizeApp(stage)
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
                            MainApp.closeApp(stage)
                        }
                    }
                }
            }
            setOnMouseDragged { event ->
                if(dragCursor == Cursor.DEFAULT && !stage.isMaximized) {
                    stage.x = event.screenX + xOffsetWindow
                    stage.y = event.screenY + yOffsetWindow

                    Config.instance.updateValues(stage)
                }
            }
            setOnMouseClicked { event ->
                if(event.button == MouseButton.PRIMARY && event.clickCount == 2 && canResize) {
                    when {
                        stage.isMaximized -> MainApp.restoreApp(stage)
                        else -> MainApp.maximizeApp(stage)
                    }
                }
            }
        }
        this.contentBox = hbox {
            hgrow = Priority.ALWAYS
            vgrow = Priority.ALWAYS
            padding = contentPadding

            addClass("windowContent")
        }
        setOnMousePressed { event ->
            if(!stage.isMaximized) {
                xOffsetWindow = stage.x - event.screenX
                yOffsetWindow = stage.y - event.screenY
            }
        }
        addEventFilter(MouseEvent.MOUSE_DRAGGED) { event ->
            if(!stage.isMaximized && canResize) {
//                Platform.runLater {
                val curX = event.screenX + xOffsetWindow
                val curY = event.screenY + yOffsetWindow
                var newLoc: Pair<Double, Double> = stage.x to stage.y
                var newSize: Pair<Double, Double> = stage.width to stage.height
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
//                        val root = stage.scene.root
//                        stage.scene = null
//                        val newScene = Scene(root, newSize.first, newSize.second)
//                        stage.scene = newScene
                    stage.width = newSize.first
                    stage.height = newSize.second
                    stage.x = newLoc.first
                    stage.y = newLoc.second
                    stage.scene.reloadStylesheets()

                    Config.instance.updateValues(stage)

                    event.consume()
                }
//                }
            }
        }
        addEventFilter(MouseEvent.MOUSE_MOVED) { event ->
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

        this.initFinished = true
    }
}