package de.sanandrew.apps.cursemodmgr.pack

import de.sanandrew.apps.cursemodmgr.GsonInst
import de.sanandrew.apps.cursemodmgr.curseapi.CFAPI
import de.sanandrew.apps.cursemodmgr.curseapi.Game
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Files

object MinecraftModpacks {
    data class PackImg(var width: Int, var height: Int, var data: String)
    data class Modpack(var title: String, var modLoader: Game.Modloader, var directory: String, var img: PackImg?)

    private val packs = ArrayList<Modpack>()

    fun loadPacks() {
        this.packs.clear()
        if(Files.exists(File("./packs.json").toPath())) {
            GsonInst.fromJson(FileReader("./packs.json"), Array<Modpack>::class.java).forEach { p -> addPack(p) }
        }
    }

    fun getPacks(): Set<Modpack> {
        return this.packs.toSet()
    }

    fun addPack(pack: Modpack) {
        pack.modLoader = CFAPI.Minecraft.modloader.first { ml -> ml.name == pack.modLoader.name }
        this.packs.add(pack)
    }

    fun removePack(pack: Modpack) {
        this.packs.remove(pack)
    }

    fun savePacks() {
        FileWriter("./packs.json", false).use {
            it.write(GsonInst.toJson(this.packs.toArray()))
        }
    }
}