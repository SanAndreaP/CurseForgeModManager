package de.sanandrew.apps.cursemodmgr

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import de.sanandrew.apps.cursemodmgr.util.Config
import de.sanandrew.apps.cursemodmgr.util.I18n
import javafx.application.Application
import mu.KotlinLogging
import java.net.JarURLConnection

val GsonInst: Gson = GsonBuilder().serializeNulls().setPrettyPrinting().create()
val log = KotlinLogging.logger { }

fun main(args: Array<String>) {
    I18n.loadLangs()
    Config.loadConfig()

    Application.launch(MainApp::class.java, *args)
}

fun useResourceList(path: String, onElem: (elem: String) -> Unit) {
    MainApp::class.java.classLoader.getResource(path)?.let { resource ->
        if( resource.toString().startsWith("jar:") ) {
            val conn = resource.openConnection() as JarURLConnection
            conn.jarFile.use { jarFile -> jarFile.entries().iterator().forEach { if( it.name.startsWith(path) ) onElem(it.name) } }
        } else {
            MainApp::class.java.classLoader.getResourceAsStream(path)?.bufferedReader()?.useLines { it.forEach { file -> onElem(path + file) } }
        }
    }
}