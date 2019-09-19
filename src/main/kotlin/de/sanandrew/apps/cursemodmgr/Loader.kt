package de.sanandrew.apps.cursemodmgr

import de.sanandrew.apps.cursemodmgr.curseapi.CFAPI
import de.sanandrew.apps.cursemodmgr.pack.Packs
import de.sanandrew.apps.cursemodmgr.util.I18n
import de.sanandrew.apps.cursemodmgr.util.windowFrame
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

class Loader : View(I18n.translate("title")) {
    private val progressLbl = SimpleObjectProperty<String>(I18n.translate("load.loading", "..."))
    private val progressPerc = SimpleObjectProperty<Double>(0.0)
    private val progressDetails = SimpleObjectProperty<String>("")

    companion object {
        var progressPartsDone = 0
        var progressPartsTotal = 0
    }

    init {
        MainApp.initWindow(this.primaryStage)

        this.primaryStage.onCloseRequest = EventHandler {
            MainApp.closeApp(this.primaryStage)
        }
    }

    override val root = windowFrame(this.primaryStage, this.title) {
        vbox {
            label(this@Loader.progressLbl) {
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
                    lengthProperty().bind(this@Loader.progressPerc)
                    radiusX = 20.0
                    radiusY = 20.0
                    type = ArcType.ROUND
                    thread {
                        this@Loader.progressLbl.set(I18n.translate("load.loading", "..."))
                        Thread.sleep(500L)
                        Platform.runLater {
                            progressPartsTotal = CFAPI.TOTAL_LOAD_PARTS
                            CFAPI.load(::setProgress) {
                                progressPartsDone++
                                Platform.runLater { replaceWith(Packs()) }
                            }
                        }
                    }
                }
                alignment = Pos.CENTER
            }
            label(this@Loader.progressDetails)
            vboxConstraints {
                maxHeight = Double.MAX_VALUE
                hgrow = Priority.ALWAYS
                maxWidth = Double.MAX_VALUE
                vgrow = Priority.ALWAYS
            }
        }
    }

    private fun setProgress(rb: Long, tb: Long, lbl: String) {
        Platform.runLater {
            val partAngle = -360.0 / progressPartsTotal.toDouble()
            this.progressLbl.set(I18n.translate("load.loading", lbl))
            if(tb > 0) {
                this.progressPerc.set(progressPartsDone.toDouble() * partAngle + rb.toDouble() / tb.toDouble() * partAngle)
            } else {
                this.progressPerc.set(progressPartsDone.toDouble() * partAngle)
            }
            this.progressDetails.set(if(tb > 0) String.format("%s / %s", rb.getStagedByteVal(), tb.getStagedByteVal()) else rb.getStagedByteVal())
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