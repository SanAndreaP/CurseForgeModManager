package de.sanandrew.apps.cursemodmgr

import de.sanandrew.apps.cursemodmgr.css.CssMain
import de.sanandrew.apps.cursemodmgr.css.CssPacks
import de.sanandrew.apps.cursemodmgr.css.CssWindow
import de.sanandrew.apps.cursemodmgr.css.color.CssLightFirebrick
import de.sanandrew.apps.cursemodmgr.css.color.CssLightSteelblue
import de.sanandrew.apps.cursemodmgr.util.Config
import javafx.application.Platform
import javafx.stage.Stage
import javafx.stage.StageStyle
import tornadofx.*
import kotlin.reflect.KClass
import kotlin.system.exitProcess

class MainApp : tornadofx.App(MainWindow::class) {
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
    }

    init {
        importStylesheet(CssMain::class)
        importStylesheet(CssWindow::class)
        importStylesheet(CssPacks::class)

        importStylesheet(currStyle.css)
    }

    override fun start(stage: Stage) {
        stage.initStyle(StageStyle.TRANSPARENT)

        super.start(stage)
    }

    @Suppress("unused")
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


