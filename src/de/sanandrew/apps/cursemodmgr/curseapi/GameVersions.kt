package de.sanandrew.apps.cursemodmgr.curseapi

import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson

class GameVersions() {
    data class GameVersion(val id: Int, val gameVersionTypeID: Int, val name: String, val slug: String)

    var versions: Array<GameVersion> = arrayOf<GameVersion>()

    init {
        val (_, _, result) = "https://minecraft.curseforge.com/api/game/versions".httpGet(listOf(Pair("token", "6f561561-f635-46b4-bebb-b6edcd743f5f"))).responseString()
        this.versions = Gson().fromJson(result.component1(), Array<GameVersion>::class.java).filter { gv -> gv.gameVersionTypeID != 1 }.toTypedArray()
    }
}