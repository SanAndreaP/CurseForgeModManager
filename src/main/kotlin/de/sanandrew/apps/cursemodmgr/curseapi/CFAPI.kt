package de.sanandrew.apps.cursemodmgr.curseapi

import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.httpDownload
import com.github.kittinunf.fuel.httpGet

object CFAPI {
    public lateinit var Minecraft: Game

    private val minecraftId = 432

    public fun load(dlProgressHandler: (Long, Long) -> Unit) {
        "https://addons-ecs.forgesvc.net/api/v2/game"
                .httpGet(listOf(Pair("supportsAddons", true)))
                .responseProgress(dlProgressHandler)
                .responseString {_, _, result ->
                    println(result.get())
                }
    }
}