package de.sanandrew.apps.cursemodmgr

import com.google.gson.GsonBuilder
import de.sanandrew.apps.cursemodmgr.util.Config
import de.sanandrew.apps.cursemodmgr.util.I18n
import javafx.application.Application
import tornadofx.*
import java.lang.reflect.Modifier

val GsonInst = GsonBuilder().serializeNulls().setPrettyPrinting().create()

fun main(args: Array<String>) {
    I18n.loadLangs()
    Config.loadConfig()

    Application.launch(MainApp::class.java, *args)
}