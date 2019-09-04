package de.sanandrew.apps.cursemodmgr.util

import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Paths

object I18n
{
    const val STD_LANG = "en_us"

    private val languages = HashMap<String, HashMap<String, String>>()
    private var currLang = STD_LANG

    fun loadLangs() {
        InputStreamReader(javaClass.classLoader.getResourceAsStream("lang/$STD_LANG.lang")!!).use { r ->
            loadLang(this.STD_LANG, r)
        }

        javaClass.classLoader.getResourceAsStream("lang/")?.bufferedReader()?.useLines {
            it.forEach { file ->
                val res = javaClass.classLoader.getResourceAsStream("lang/$file")
                if( file.endsWith(".lang") && res != null ) {
                    loadLang(file.substringBeforeLast('.'), res.reader())
                }
            }
        }
    }

    private fun loadLang(lang: String, reader: InputStreamReader) {
        val kvMap = this.languages.computeIfAbsent(lang) { HashMap() }
        reader.readLines().forEach { l ->
            val kv = l.split(Regex("="), 2)
            if( kv.size == 2 ) {
                kvMap[kv[0]] = kv[1]
            }
        }
    }

    fun translate(key: String, vararg formatObj: Any?): String {
        if( this.languages[currLang]?.containsKey(key) == true ) {
            return this.languages[currLang]!![key]!!.format(*formatObj)
        } else if( this.languages[this.STD_LANG]?.containsKey(key) == true ) {
            return this.languages[this.STD_LANG]!![key]!!.format(*formatObj)
        }

        return key
    }

    fun setCurrentLang(lang: String?) {
        if( lang != null && this.languages.containsKey(lang) ) {
            this.currLang = lang
        }
    }

    fun getCurrentLang(): String {
        return this.currLang
    }
}