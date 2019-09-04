package de.sanandrew.apps.cursemodmgr

import de.sanandrew.apps.cursemodmgr.css.CssMain
import de.sanandrew.apps.cursemodmgr.css.CssPacks
import de.sanandrew.apps.cursemodmgr.css.CssWindow
import de.sanandrew.apps.cursemodmgr.css.color.CssLightFirebrick
import de.sanandrew.apps.cursemodmgr.css.color.CssLightSteelblue
import de.sanandrew.apps.cursemodmgr.util.Config
import de.sanandrew.apps.cursemodmgr.util.currStage
import javafx.application.Platform
import javafx.stage.Screen
import javafx.stage.Stage
import javafx.stage.StageStyle
import tornadofx.*
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass
import kotlin.system.exitProcess

class MainApp: tornadofx.App(Loader::class) {
    companion object {
        private var currStyle = Stylesheets.FIREBRICK_L

        fun getCurrStyle(): Stylesheets {
            return currStyle
        }

        fun setCurrStyle(name: String) {
            try {
                Stylesheets.valueOf(name).apply()
            } catch( e: IllegalArgumentException ) { }
        }

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
            stage.isMaximized = Config.instance.isMaximized.get()

            if( Config.instance.isMaximized.get() ) {
                maximizeApp(view)
            } else {
                stage.width = Config.instance.windowSizeX.get().toDouble()
                stage.height = Config.instance.windowSizeY.get().toDouble()

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
        reloadViewsOnFocus()
        reloadStylesheetsOnFocus()

        importStylesheet(CssMain::class)
        importStylesheet(CssWindow::class)
        importStylesheet(CssPacks::class)

        importStylesheet(currStyle.css)
    }

    override fun start(stage: Stage) {
        stage.initStyle(StageStyle.UNDECORATED)

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


