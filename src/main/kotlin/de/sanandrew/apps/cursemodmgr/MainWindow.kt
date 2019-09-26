package de.sanandrew.apps.cursemodmgr

import de.sanandrew.apps.cursemodmgr.util.Config
import de.sanandrew.apps.cursemodmgr.util.I18n
import de.sanandrew.apps.cursemodmgr.util.windowFrame
import tornadofx.*

class MainWindow: View(I18n.translate("title"))
{
    override val root = windowFrame({this.primaryStage}, this.title) {
        this.initSize(Config.instance.windowSizeX.default.toDouble(), Config.instance.windowSizeY.default.toDouble())

        if(Config.instance.isMaximized.get()) {
            this.maximize()
        }

        onResized {
            Config.instance.updateValues(this.stage())
        }

        onClosing {
            MainApp.closeApp(stage())
            false
        }

        initialize()
    }

    private val loader: Loader by inject()
    init {
        root += loader
    }
}