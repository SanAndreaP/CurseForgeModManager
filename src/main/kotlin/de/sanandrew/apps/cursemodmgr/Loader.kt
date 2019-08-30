package de.sanandrew.apps.cursemodmgr

import de.sanandrew.apps.cursemodmgr.util.cstWindowFrame
import de.sanandrew.apps.cursemodmgr.curseapi.*
import de.sanandrew.apps.cursemodmgr.pack.Packs
import javafx.application.Platform
import javafx.beans.property.SimpleObjectProperty
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.scene.shape.ArcType
import javafx.scene.text.Font
import tornadofx.*
import kotlin.concurrent.thread

class Loader : View("CurseForge Mod Manager") {
    private val progressLbl = SimpleObjectProperty<String>("Loading...")
    private val progressPerc = SimpleObjectProperty<Double>(0.0)
    private val progressDetails = SimpleObjectProperty<String>("")

    init {
        MainApp.initWindow(this)

        this.primaryStage.onCloseRequest = EventHandler {
            MainApp.closeApp(this)
        }
    }

    override val root = cstWindowFrame(this, vbox {
        label(progressLbl) {
            font = Font.font(16.0)
        }
        group {
            circle {
                addClass("progressCircle")
                radius = 22.0
                fill = Color.WHITE
                strokeWidth = 2.0
            }
            arc {
                addClass("progressArc")
                startAngle = 90.0
                lengthProperty().bind(progressPerc)
                radiusX = 20.0
                radiusY = 20.0
                type = ArcType.ROUND
                thread {
                    progressLbl.set("Loading...")
                    Thread.sleep(500L)
                    Platform.runLater {
                        CFAPI.load(::setProgress) { Platform.runLater { replaceWith(Packs()) } }
                    }
                }
            }
            alignment = Pos.CENTER
        }
        label(progressDetails)
        vboxConstraints {
            maxHeight = Double.MAX_VALUE
            hgrow = Priority.ALWAYS
            maxWidth = Double.MAX_VALUE
            vgrow = Priority.ALWAYS
        }
    }, hasMinBtn = true, hasMaxRstBtn = true)

    private fun setProgress(rb: Long, tb: Long, lbl: String) {
        Platform.runLater {
            this.progressLbl.set("Loading $lbl...")
            this.progressPerc.set(-rb.toDouble() / tb.toDouble() * 360.0)
            this.progressDetails.set(if( tb > 0 ) String.format("%s / %s", rb.getStagedByteVal(), tb.getStagedByteVal()) else rb.getStagedByteVal())
        }
    }

    private fun Long.getStagedByteVal(): String {
        val kb = 1024
        val mb = kb * kb
        val gb = mb * kb
        return when {
            this >= gb -> String.format("%.2f GB", this / gb.toDouble())
            this >= mb -> String.format("%.2f MB", this / mb.toDouble())
            this >= kb -> String.format("%.2f kB", this / kb.toDouble())
            else -> String.format("%.2f B", this.toDouble())
        }
    }
}