package de.sanandrew.apps.cursemodmgr.curseapi

import com.github.kittinunf.fuel.httpGet
import de.sanandrew.apps.cursemodmgr.GsonInst
import de.sanandrew.apps.cursemodmgr.main.Loader
import de.sanandrew.apps.cursemodmgr.log
import de.sanandrew.apps.cursemodmgr.util.I18n
import javafx.application.Platform
import javafx.scene.control.Alert
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.nio.file.attribute.FileTime
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import kotlin.system.exitProcess

typealias FuelParameters = List<Pair<String, Any?>>
typealias ProgressHandler = (Long, Long, String) -> Unit

object CFAPI {
    const val TOTAL_LOAD_PARTS = 1 + Game.TOTAL_LOAD_PARTS

    lateinit var Minecraft: Game

    @JvmStatic
    private val MINECRAFT_ID = 432L

    fun load(dlProgressHandler: ProgressHandler, onFinish: () -> Unit) {
        loadFromAPI("game", dlProgressHandler, {
            val games = GsonInst.fromJson(it, Array<Game>::class.java)
            this.Minecraft = games.first { game -> game.id == MINECRAFT_ID }
            Loader.progressPartsDone++
            this.Minecraft.loadVersions(dlProgressHandler, onFinish)
        }, listOf(Pair("supportsAddons", true)))
    }

    fun loadFromAPI(name: String, dlProgressHandler: ProgressHandler, loader: (json: String) -> Unit, parameters: FuelParameters? = null) {
        log.info { "load $name..." }
        val tlName = name.replace("/", ".").replace("\\", ".")
        dlProgressHandler(0, -1, I18n.translate("load.$tlName"))
        val cache = this.getCache(name)
        if(cache.first == null) {
            "https://addons-ecs.forgesvc.net/api/v2/$name"
                    .httpGet(parameters)
                    .responseProgress { rb, tb -> dlProgressHandler(rb, tb, I18n.translate("load.$tlName")) }
                    .responseString { _, _, result ->
                        val rs = result.get()
                        Platform.runLater {
                            saveCache(cache.second, name, rs)
                            loader(rs)
                        }
                    }
        } else {
            loader(cache.first!!)
        }
    }

    private fun getCache(name: String): Pair<String?, Instant> {
        val fileName = name.replace("/", ".").replace("\\", ".")
        val pth = Paths.get("./cache/$fileName.json")

        log.debug { "grabbing timestamp for $name..." }
        val ts = "https://addons-ecs.forgesvc.net/api/v2/$name/timestamp".httpGet().responseString().third.component1()?.replace("Z\"", "")?.replace("\"", "")
        log.debug { "grabbed timestamp for $name: $ts" }
        if(ts != null) {
            val timestp = LocalDateTime.parse(ts).toInstant(OffsetDateTime.now().offset)
            return if(Files.exists(pth)) {
                val currTS = Files.getLastModifiedTime(pth).toInstant()
                if(currTS < timestp) {
                    println("Cache for $name outdated: $currTS -> $timestp")
                    null to timestp
                } else {
                    log.debug { "loading cache file for $name..." }
                    val ret = Files.readAllLines(pth).joinToString("\n") to currTS
                    log.debug { "loaded cache file for $name." }
                    ret
                }
            } else {
                null to timestp
            }
        } else if(Files.exists(pth)) {
            return Files.readAllLines(pth).joinToString("\n") to Files.getLastModifiedTime(pth).toInstant()
        }

        log.error { "Cannot fetch $name and no cache is available! Exiting app..." }
        Alert(Alert.AlertType.ERROR, I18n.translate("cfapi.err.cache.$fileName"))
        exitProcess(-1)
    }

    private fun saveCache(timestp: Instant, name: String, content: String) {
        val fileName = name.replace("/", ".").replace("\\", ".")
        val pth = Paths.get("./cache/$fileName.json")

        Files.createDirectories(pth.parent)
        Files.write(pth, content.toByteArray(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)
        Files.setLastModifiedTime(pth, FileTime.from(timestp))
    }
}