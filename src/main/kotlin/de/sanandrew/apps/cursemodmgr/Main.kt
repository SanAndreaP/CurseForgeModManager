package de.sanandrew.apps.cursemodmgr

import com.google.gson.GsonBuilder
import javafx.application.Application

val GsonInst = GsonBuilder().serializeNulls().setPrettyPrinting().create()

fun main(args: Array<String>) {
    Config.loadConfig()

    Application.launch(MainApp::class.java, *args)
}