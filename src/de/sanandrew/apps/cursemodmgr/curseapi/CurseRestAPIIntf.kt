package de.sanandrew.apps.cursemodmgr.curseapi

import com.github.kittinunf.fuel.httpPost
import de.sanandrew.apps.cursemodmgr.GsonInst
import java.io.File

object CurseRestAPIIntf {
    data class SessionData(val session: Session?, val status: String) {
        data class Session(val email_address: String, val session_id: String, val subscription_token: Int, val token: String, val user_id: Int, val username: String)
    }
    data class LoginData(val username: String, val password: String)

    val curseRestApiLnk: String = "https://curse-rest-proxy.azurewebsites.net/api"
    val sessionFile = File("./session_data.json")

    @Volatile
    lateinit var loginInfo: SessionData

    fun loadSession(): Boolean {
        if( sessionFile.exists() ) {
            loginInfo = GsonInst.fromJson(sessionFile.readText(Charsets.UTF_8), SessionData::class.java)
            return true
        }

        return false
    }

    fun createSession(usrName: String, pw: String, callback: () -> Unit) {
        curseRestApiLnk.plus("/authenticate").httpPost().header(Pair("Content-Type", "application/json"))
                            .body(GsonInst.toJson(LoginData(usrName, pw))).response { _, response, result ->
            if( response.statusCode == 200 ) {
                loginInfo = GsonInst.fromJson(String(result.component1()!!, Charsets.UTF_8), SessionData::class.java)
            } else {
                loginInfo = SessionData(null, response.responseMessage)
            }
            callback()
        }
    }

    fun saveSession() {
        sessionFile.writeText(GsonInst.toJson(loginInfo), Charsets.UTF_8)
    }
}