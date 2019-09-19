package de.sanandrew.apps.cursemodmgr.util

import de.sanandrew.apps.cursemodmgr.GsonInst
import de.sanandrew.apps.cursemodmgr.MainApp
import javafx.stage.Stage
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Files
import java.util.Locale
import kotlin.reflect.full.declaredMemberProperties

class Config {
    companion object {
        private val inst = Config()

        val instance: Config
            get() = inst

        fun saveConfig() {
            val cfg = GsonInst.toJson(this.inst)
            FileWriter("./config.json", false).use {
                it.write(cfg)
            }
        }

        fun loadConfig() {
            if(Files.exists(File("./config.json").toPath())) {
                val loaded = GsonInst.fromJson(FileReader("./config.json"), Config::class.java)
                this.inst.updateConfig(loaded)

                I18n.setCurrentLang(this.inst.lang.get())
                MainApp.setCurrStyle(this.inst.style.get().toUpperCase(Locale.ROOT))
            }
        }
    }

    var isMaximized = StdValue(false)
    var windowSizeX = StdValue(800)
    var windowSizeY = StdValue(600)
    var lang = StdValue(I18n.STD_LANG)
    var style = StdValue(MainApp.Stylesheets.FIREBRICK_L.name.toLowerCase(Locale.ROOT))

    fun updateValues(stage: Stage? = null) {
        stage?.let {
            this.isMaximized.set(it.isMaximized)
            this.windowSizeX.set(it.width.toInt())
            this.windowSizeY.set(it.height.toInt())
        }

        this.lang.set(I18n.getCurrentLang())
        this.style.set(MainApp.getCurrStyle().name.toLowerCase(Locale.ROOT))

        saveConfig()
    }

    @Suppress("UNCHECKED_CAST")
    private fun updateConfig(newCfg: Config) {
        this::class.declaredMemberProperties
                .mapNotNull {
                    val g = it.getter.call(this)
                    if(g is StdValue<*>) it.name to g as StdValue<Any> else null
                }
                .forEach {
                    val mo = newCfg::class.declaredMemberProperties.find { moi -> moi.name == it.first }!!.getter.call(newCfg) as StdValue<Any>
                    it.second.set(mo.getValue())
                }
    }

    data class StdValue<T : Any>(private var value: T?, val default: T) {
        constructor(value: T) : this(value, value)

        fun get(): T {
            return this.value ?: this.default
        }

        fun getValue(): T? {
            return this.value
        }

        fun set(value: T?) {
            this.value = value
        }
    }
}