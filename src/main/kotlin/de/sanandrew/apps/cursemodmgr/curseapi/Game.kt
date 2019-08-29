package de.sanandrew.apps.cursemodmgr.curseapi

import com.github.kittinunf.fuel.httpGet
import de.sanandrew.apps.cursemodmgr.GsonInst

data class Game(val id: Long,
                val name: String,
                val slug: String,
                val categorySections: Array<CategorySection>,
                var versions: Array<GameVersion> = arrayOf())
{
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
                           val jsonDownloadUrl: String)

    public fun loadGameVersions(dlProgressHandler: (Long, Long, String) -> Unit, onFinish: () -> Unit) {
        "https://addons-ecs.forgesvc.net/api/v2/$slug/version"
                .httpGet()
                .responseProgress {rb, tb -> dlProgressHandler(rb, tb, "$name Versions")}
                .responseString {_, _, result ->
                    this.versions = GsonInst.fromJson(result.get(), Array<GameVersion>::class.java)
                    onFinish()
                }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Game

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}