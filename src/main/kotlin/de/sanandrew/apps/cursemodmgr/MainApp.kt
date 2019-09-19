package de.sanandrew.apps.cursemodmgr

import de.sanandrew.apps.cursemodmgr.css.CssMain
import de.sanandrew.apps.cursemodmgr.css.CssPacks
import de.sanandrew.apps.cursemodmgr.css.CssWindow
import de.sanandrew.apps.cursemodmgr.css.color.CssLightFirebrick
import de.sanandrew.apps.cursemodmgr.css.color.CssLightSteelblue
import de.sanandrew.apps.cursemodmgr.util.Config
import javafx.application.Platform
import javafx.stage.Screen
import javafx.stage.Stage
import javafx.stage.StageStyle
import tornadofx.*
import kotlin.reflect.KClass
import kotlin.system.exitProcess

class MainApp : tornadofx.App(Loader::class) {
    companion object {
        private var currStyle = Stylesheets.FIREBRICK_L

        fun getCurrStyle(): Stylesheets {
            return currStyle
        }

        fun setCurrStyle(name: String) {
            try {
                Stylesheets.valueOf(name).apply()
            } catch(e: IllegalArgumentException) {
            }
        }

        fun closeApp(stage: Stage) {
            Config.instance.updateValues(stage)

            Platform.exit()
            exitProcess(0)
        }

        fun maximizeApp(stage: Stage) {
            stage.isMaximized = true

            val screens = Screen.getScreensForRectangle(stage.x, stage.y, stage.x, stage.y)
            val bounds = screens.getOrElse(0) { Screen.getPrimary() }.visualBounds

            stage.x = bounds.minX
            stage.y = bounds.minY
            stage.width = bounds.width
            stage.height = bounds.height

            Config.instance.updateValues(stage)
        }

        fun restoreApp(stage: Stage) {
            stage.isMaximized = false
            stage.width = Config.instance.windowSizeX.default.toDouble()
            stage.height = Config.instance.windowSizeY.default.toDouble()

            centerOnScreen(stage)

            Config.instance.updateValues(stage)
        }

        fun minimizeApp(stage: Stage) {
            stage.isIconified = true
        }

        fun initWindow(stage: Stage) {
            stage.isMaximized = Config.instance.isMaximized.get()

            if(Config.instance.isMaximized.get()) {
                maximizeApp(stage)
            } else {
                stage.width = Config.instance.windowSizeX.get().toDouble()
                stage.height = Config.instance.windowSizeY.get().toDouble()

                centerOnScreen(stage)
            }
        }

        private fun centerOnScreen(stage: Stage) {
            val screens = Screen.getScreensForRectangle(stage.x, stage.y, stage.x, stage.y)
            val bounds = screens.getOrElse(0) { Screen.getPrimary() }.visualBounds

            stage.x = (bounds.width - stage.width) / 2.0
            stage.y = (bounds.height - stage.height) / 3.0
        }
    }

    init {
//        reloadViewsOnFocus()
//        reloadStylesheetsOnFocus()

        importStylesheet(CssMain::class)
        importStylesheet(CssWindow::class)
        importStylesheet(CssPacks::class)

        importStylesheet(currStyle.css)
    }

    override fun start(stage: Stage) {
        stage.initStyle(StageStyle.TRANSPARENT)

        super.start(stage)
    }

    enum class Stylesheets(val styleName: String, val css: KClass<out Stylesheet>) {
        FIREBRICK_L("Firebrick Light", CssLightFirebrick::class),
        STEELBLUE_L("Steelblue Light", CssLightSteelblue::class);

        fun apply() {
            removeStylesheet(currStyle.css)
            importStylesheet(this.css)
            currStyle = this

            Config.instance.updateValues()
        }
    }
}


