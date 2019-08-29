package de.sanandrew.apps.cursemodmgr.form

import de.sanandrew.apps.cursemodmgr.MainApp
import de.sanandrew.apps.cursemodmgr.cstWindowFrame
import de.sanandrew.apps.cursemodmgr.curseapi.AddOnLoader
import de.sanandrew.apps.cursemodmgr.curseapi.CurseRestAPIIntf
import de.sanandrew.apps.cursemodmgr.curseapi.ModLoaders
import javafx.application.Platform
import javafx.beans.property.SimpleObjectProperty
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.scene.shape.ArcType
import javafx.scene.text.Font
import javafx.stage.StageStyle
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

        if(CurseRestAPIIntf.loadSession()) {

        } else {
            val login = Login()
            login.openModal(StageStyle.UNDECORATED, block = true)
            if( !login.loginOkay ) {
                MainApp.closeApp(this)
            }
        }
    }

    override val root = cstWindowFrame(this, vbox {
        label(progressLbl) {
            font = Font.font(16.0)
        }
        group {
            circle {
                radius = 22.0
                fill = Color.WHITE
                stroke = Color.FIREBRICK.desaturate()
                strokeWidth = 2.0
            }
            arc {
                startAngle = 90.0
                lengthProperty().bind(progressPerc)
                radiusX = 20.0
                radiusY = 20.0
                type = ArcType.ROUND
                fill = Color.FIREBRICK
                thread {
                    progressLbl.set("Loading...")
                    Thread.sleep(500L)
                    Platform.runLater { loadModLoaders() }
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
    }, true, true)

    private fun loadModLoaders() {
        this.progressLbl.set("Loading Modloader list...")
        this.progressPerc.set(0.0)
        this.progressDetails.set("0 B / 0 B")
        ModLoaders.load({ readBytes, totalBytes ->
            Platform.runLater {
                this.progressPerc.set(-readBytes.toDouble() / totalBytes.toDouble() * 360.0)
                this.progressDetails.set(String.format("%s / %s", readBytes.getStagedByteVal(), totalBytes.getStagedByteVal()))
            }
        }, {
            Platform.runLater { loadAddons() }
        })
    }

    private fun loadAddons() {
        this.progressLbl.set("Loading AddOn list...")
        this.progressPerc.set(0.0)
        this.progressDetails.set("0 B / 0 B")
        AddOnLoader.load({ readBytes, totalBytes ->
            Platform.runLater {
                this.progressPerc.set(-readBytes.toDouble() / totalBytes.toDouble() * 360.0)
                this.progressDetails.set(String.format("%s / %s", readBytes.getStagedByteVal(), totalBytes.getStagedByteVal()))
            }
        }, {
            Platform.runLater {
                this.progressLbl.set("Done")
                this.progressDetails.set("")
            }
            Thread.sleep(500L)
            Platform.runLater { replaceWith(Packs::class) }
        })
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