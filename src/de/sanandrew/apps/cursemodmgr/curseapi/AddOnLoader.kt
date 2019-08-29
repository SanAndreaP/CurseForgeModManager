package de.sanandrew.apps.cursemodmgr.curseapi

import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.httpDownload
import com.google.gson.annotations.SerializedName
import de.sanandrew.apps.cursemodmgr.GsonInst
import de.sanandrew.apps.cursemodmgr.mcGameId
import java.io.File
import java.io.FileReader
import java.net.URL

object AddOnLoader {
    private final val minecraftId = 432

    data class Game(val id: Long, val name: String, val slug: String, val categorySections: Array<CategorySection>)
    data class CategorySection(val id: Long, val name: String, val packageType: Int, val path: String, val initialInclusionPattern: String,
                               val extraIncludePattern: String, val gameCategoryId: Long)
//    data class Dependency(val AddOnId: Long, val Type: Int)
//    data class Module(val Foldername: String, val Fingerprint: Long)
//    data class LatestFile(val Id: Long, val FileName: String, val FileNameOnDisk: String, val FileDate: String, val ReleaseType: Int, val FileStatus: Int,
//                          val DownloadURL: String, val IsAlternate: Boolean, val AlternateFileId: Long, val Dependencies: Array<Dependency>, val IsAvailable: Boolean,
//                          val Modules: Array<Module>, val PackageFingerprint: Long, val GameVersion: Array<String>)
//
//    data class Author(val Name: String, val Url: String)
//    data class Attachment(val Description: String, val IsDefault: Boolean, val ThumbnailUrl: String, val Title: String, val Url: String)
//    data class Category(val Id: Long, val Name: String, val URL: String)
//    data class CategorySection(val ID: Long, val GameID: Int, val Name: String, val PackageType: Int, val Path: String, val InitialInclusionPattern: String, val ExtraIncludePattern: String?)
//    data class AddOnFile(val GameVersion: String, val ProjectFileID: Long, val ProjectFileName: String, val FileType: Int)
//
//    data class AddOn(val Id: Long, val Name: String, val Authors: Array<Author>, val Attachments: Array<Attachment>, val WebSiteURL: String, val GameId: Int, val Summary: String,
//                     val DefaultFileId: Long, val CommentCount: Long, val DownloadCount: Double, val Rating: Int, val InstallCount: Long, val IconId: Long,
//                     val LatestFiles: Array<LatestFile>, val Categories: Array<Category>, val PrimaryAuthorName: String, val ExternalUrl: String?, val Status: Int, val Stage: Int,
//                     val DonationUrl: String?, val PrimaryCategoryId: Long, val PrimaryCategoryName: String, val PrimaryCategoryAvatarUrl: String, val Likes: Long,
//                     val CategorySection: CategorySection, val PackageType: Int, val AvatarUrl: String?, val GameVersionLatestFiles: Array<AddOnFile>, val IsFeatured: Int,
//                     val PopularityScore: Double, val GamePopularityRank: Long)
//
//    data class AddOnData(val timestamp: Long, @SerializedName("data") val addOns: Array<AddOn>)
//
//    val addonsUrl: String = "https://clientupdate-v6.cursecdn.com/feed/addons/$mcGameId/v10/complete.json.bz2"
//    @Volatile
//    var addOns: AddOnData? = null

    //TODO: reinstate loader
    fun load(dlProgressHandler: (Long, Long) -> Unit, finished: (Response) -> Unit) {
        val tmpFile = File.createTempFile("mcf_addons", "")
//        addonsUrl.httpDownload().destination({_, _ -> tmpFile})
//                .progress(dlProgressHandler)
//                .response { _, response, _ ->
//                    if( response.statusCode == 200 ) {
//                        addOns = GsonInst.fromJson(FileReader(tmpFile), AddOnData::class.java)
//                    }
                    //finished(response)
                    finished(Response(URL("file:///C:\\")))
//                }
    }
}