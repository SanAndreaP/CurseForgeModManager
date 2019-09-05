package de.sanandrew.apps.cursemodmgr.util

import de.sanandrew.apps.cursemodmgr.useResourceList
import java.io.InputStreamReader

object I18n
{
    const val STD_LANG = "en_us"

    private val languages = HashMap<String, HashMap<String, String>>()
    private var currLang = STD_LANG

    fun loadLangs() {
        InputStreamReader(javaClass.classLoader.getResourceAsStream("lang/$STD_LANG.lang")!!).use { r ->
            loadLang(this.STD_LANG, r)
        }

        useResourceList("lang/") {
            val res = javaClass.classLoader.getResourceAsStream(it)
            if( it.endsWith(".lang") && res != null ) {
                loadLang(it.substringBeforeLast('.').substringAfter("lang/"), res.reader())
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