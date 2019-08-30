package de.sanandrew.apps.cursemodmgr.util

import de.sanandrew.apps.cursemodmgr.GsonInst
import javafx.stage.Stage
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Files

class Config {
    companion object {
        private var inst = Config()

        val instance: Config
            get() = inst

        fun saveConfig() {
            val cfg = GsonInst.toJson(inst)
            FileWriter("./config.json", false).use {
                it.write(cfg)
            }
        }

        fun loadConfig() {
            if( Files.exists(File("./config.json").toPath()) ) {
                inst = GsonInst.fromJson(FileReader("./config.json"), Config::class.java)
            }
        }
    }

    var isMaximized = StdValue(false)
    var windowSizeX = StdValue(800)
    var windowSizeY = StdValue(600)

    fun updateValues(stage: Stage? = null) {
        if( stage != null ) {
            this.isMaximized.value = stage.isMaximized
            this.windowSizeX.value = stage.width.toInt()
            this.windowSizeY.value = stage.height.toInt()
        }

        saveConfig()
    }

    class StdValue<T>(var value: T) {
        val default = this.value
    }
}