package de.sanandrew.apps.cursemodmgr.curseapi

import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.httpDownload
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import de.sanandrew.apps.cursemodmgr.mcGameId
import de.sanandrew.apps.cursemodmgr.util.SemVer
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream
import java.io.File
import java.io.FileInputStream
import java.nio.charset.Charset

object ModLoaders {
    val modloadersUrl: String = "https://clientupdate-v6.cursecdn.com/feed/modloaders/$mcGameId/v10/modloaders.json.bz2"

    data class ModLoader(val Name: String, val GameVersion: String)
    data class ModLoaderList(val timestamp: Long, @SerializedName("data") var loaders: List<ModLoader>)

    lateinit var loaderList: ModLoaderList
    var gameVersions: List<SemVer> = ArrayList()

    fun load(dlProgressHandler: (Long, Long) -> Unit, finished: (Response) -> Unit) {
        val tmpFile = File.createTempFile("mcf_modloaders", "")
        modloadersUrl.httpDownload().destination({_, _ -> tmpFile})
                .progress(dlProgressHandler)
                .response { _, response, _ ->
                    if( response.statusCode == 200 ) {
                        BZip2CompressorInputStream(FileInputStream(tmpFile)).use {
                            loaderList = Gson().fromJson(it.readBytes().toString(Charset.defaultCharset()), ModLoaderList::class.java)
                        }
                        loaderList.loaders.forEach { loader ->
                            val ver = SemVer.getMcVer(loader.GameVersion)
                            if( !gameVersions.contains(ver) ) {
                                gameVersions += ver
                            }
                        }

                        gameVersions = gameVersions.sortedDescending()
                        loaderList.loaders = loaderList.loaders.sortedWith(compareByDescending(ModLoader::Name))
                    }
                    finished(response)
        }
    }
}