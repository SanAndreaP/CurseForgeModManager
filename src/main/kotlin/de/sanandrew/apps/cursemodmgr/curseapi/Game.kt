package de.sanandrew.apps.cursemodmgr.curseapi

import de.sanandrew.apps.cursemodmgr.GsonInst
import de.sanandrew.apps.cursemodmgr.Loader

data class Game(val id: Long,
                val name: String,
                val slug: String,
                val categorySections: Array<CategorySection>,
                var versions: Array<GameVersion> = arrayOf(),
                var modloader: Array<Modloader> = arrayOf()) {

    data class CategorySection(val id: Long,
                               val name: String,
                               val packageType: Int,
                               val path: String,
                               val initialInclusionPattern: String,
                               val extraIncludePattern: String,
                               val gameCategoryId: Long)

    data class GameVersion(val id: Long,
                           val gameVersionId: Long,
                           val gameVersionTypeId: Long,
                           val versionString: String,
                           val jarDownloadUrl: String,
                           val jsonDownloadUrl: String) {
        override fun equals(other: Any?): Boolean {
            if(other === this) return true
            if(other is String) return this.versionString == other
            if(other !is GameVersion) return false

            return this.versionString == other.versionString
        }

        override fun hashCode(): Int {
            return this.versionString.hashCode()
        }
    }

    data class Modloader(val name: String,
                         val gameVersion: String,
                         val latest: Boolean,
                         val recommended: Boolean) {
        fun getGameVersion(game: Game): GameVersion {
            return game.versions.first { ver -> ver.versionString == this.gameVersion }
        }
    }

    fun loadVersions(dlProgressHandler: (Long, Long, String) -> Unit, onFinish: () -> Unit) {
        CFAPI.loadFromAPI("$slug/version", dlProgressHandler, {
            this.versions = GsonInst.fromJson(it, Array<GameVersion>::class.java)
            Loader.progressPartsDone++
            this.loadModLoaders(dlProgressHandler, onFinish)
        })
    }

    private fun loadModLoaders(dlProgressHandler: (Long, Long, String) -> Unit, onFinish: () -> Unit) {
        CFAPI.loadFromAPI("$slug/modloader", dlProgressHandler, {
            this.modloader = GsonInst.fromJson(it, Array<Modloader>::class.java)
            Loader.progressPartsDone++
            onFinish()
        })
    }

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false

        other as Game

        if(id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    companion object {
        const val TOTAL_LOAD_PARTS = 2
    }
}