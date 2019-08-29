package de.sanandrew.apps.cursemodmgr.pack

import de.sanandrew.apps.cursemodmgr.GsonInst
import de.sanandrew.apps.cursemodmgr.curseapi.ModLoaders
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Files

object Modpacks {
    data class PackImg(var width: Int, var height: Int, var data: String)
    data class Modpack(var title: String, var modLoader: ModLoaders.ModLoader, var mcDirectory: String, var profileDirectory: String, var img: PackImg?)

    val packs = ArrayList<Modpack>()

    fun loadPacks() {
        this.packs.clear()
        if( Files.exists(File("./packs.json").toPath()) ) {
            this.packs.addAll(GsonInst.fromJson(FileReader("./packs.json"), Array<Modpack>::class.java))
        }
    }

    fun savePacks() {
        FileWriter("./packs.json", false).use {
            it.write(GsonInst.toJson(this.packs.toArray()))
        }
    }
}