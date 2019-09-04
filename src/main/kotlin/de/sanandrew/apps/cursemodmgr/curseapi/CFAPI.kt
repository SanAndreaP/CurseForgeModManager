package de.sanandrew.apps.cursemodmgr.curseapi

import com.github.kittinunf.fuel.httpGet
import de.sanandrew.apps.cursemodmgr.GsonInst
import de.sanandrew.apps.cursemodmgr.Loader
import de.sanandrew.apps.cursemodmgr.MainApp
import de.sanandrew.apps.cursemodmgr.pack.Packs
import de.sanandrew.apps.cursemodmgr.util.I18n

object CFAPI {
    const val TOTAL_LOAD_PARTS = 1 + Game.TOTAL_LOAD_PARTS

    lateinit var Minecraft: Game

    private val minecraftId = 432L

    fun load(dlProgressHandler: (Long, Long, String) -> Unit, onFinish: () -> Unit) {
        "https://addons-ecs.forgesvc.net/api/v2/game"
                .httpGet(listOf(Pair("supportsAddons", true)))
                .responseProgress {rb, tb -> dlProgressHandler(rb, tb, I18n.translate("load.gamelist"))}
                .responseString {_, _, result ->
                    Loader.progressPartsDone++
                    val games = GsonInst.fromJson(result.get(), Array<Game>::class.java)
                    this.Minecraft = games.first { gm -> gm.id == this.minecraftId }
                    this.Minecraft.load(dlProgressHandler, onFinish)
                }
    }
}