package de.sanandrew.apps.cursemodmgr

import com.google.gson.GsonBuilder
import javafx.application.Application

val mcGameId: Int = 432

val categoriesUrl: String = "https://clientupdate-v6.cursecdn.com/feed/categories/v10/categories.json.bz2"

val GsonInst = GsonBuilder().serializeNulls().setPrettyPrinting().create()

fun main(args: Array<String>) {
    Config.loadConfig()

    Application.launch(MainApp::class.java, *args)
}