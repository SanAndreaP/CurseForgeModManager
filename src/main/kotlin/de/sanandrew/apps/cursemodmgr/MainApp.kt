package de.sanandrew.apps.cursemodmgr

import de.sanandrew.apps.cursemodmgr.css.CssMain
import de.sanandrew.apps.cursemodmgr.css.CssPacksList
import de.sanandrew.apps.cursemodmgr.css.CssWindow
import de.sanandrew.apps.cursemodmgr.css.color.CssLightFirebrick
import de.sanandrew.apps.cursemodmgr.form.Loader
import javafx.application.Platform
import javafx.stage.Screen
import javafx.stage.Stage
import javafx.stage.StageStyle
import tornadofx.*
import kotlin.system.exitProcess

class MainApp: tornadofx.App(Loader::class) {
    companion object {
        fun closeApp(view: UIComponent) {
            Config.instance.updateValues(currStage(view))

            view.onUndock()
            Platform.exit()
            exitProcess(0)
        }

        fun maximizeApp(view: UIComponent) {
            val stage = currStage(view)
            stage.isMaximized = true

            val screens = Screen.getScreensForRectangle(stage.x, stage.y, stage.x, stage.y)
            val bounds = screens.getOrElse(0) { Screen.getPrimary() }.visualBounds

            stage.x = bounds.minX
            stage.y = bounds.minY
            stage.width = bounds.width
            stage.height = bounds.height

            Config.instance.updateValues(stage)
        }

        fun restoreApp(view: UIComponent) {
            val stage = currStage(view)
            stage.isMaximized = false
            stage.width = Config.instance.windowSizeX.default.toDouble()
            stage.height = Config.instance.windowSizeY.default.toDouble()

            centerOnScreen(view)

            Config.instance.updateValues(stage)
        }

        fun minimizeApp(view: UIComponent) {
            currStage(view).isIconified = true
        }

        fun initWindow(view: UIComponent) {
            val stage = currStage(view)
            stage.isMaximized = Config.instance.isMaximized.value

            if( Config.instance.isMaximized.value ) {
                maximizeApp(view)
            } else {
                stage.width = Config.instance.windowSizeX.value.toDouble()
                stage.height = Config.instance.windowSizeY.value.toDouble()

                centerOnScreen(view)
            }
        }

        private fun centerOnScreen(view: UIComponent) {
            val stage = currStage(view)

            val screens = Screen.getScreensForRectangle(stage.x, stage.y, stage.x, stage.y)
            val bounds = screens.getOrElse(0) { Screen.getPrimary() }.visualBounds

            stage.x = (bounds.width - stage.width) / 2.0
            stage.y = (bounds.height - stage.height) / 3.0
        }
    }

    init {
        importStylesheet(CssMain::class)
        importStylesheet(CssWindow::class)
        importStylesheet(CssPacksList::class)
        importStylesheet(CssLightFirebrick::class)
    }

    override fun start(stage: Stage) {
        stage.initStyle(StageStyle.UNDECORATED)

        super.start(stage)
    }
}


