package de.sanandrew.apps.cursemodmgr.form

import com.sun.glass.ui.Application
import de.sanandrew.apps.cursemodmgr.MainApp.Companion.centerOnScreen
import de.sanandrew.apps.cursemodmgr.curseapi.CurseRestAPIIntf
import de.sanandrew.apps.cursemodmgr.cstWindowFrame
import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.HPos
import javafx.geometry.Pos
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.RowConstraints
import javafx.scene.paint.Color
import tornadofx.*

class Login : Fragment("CurseForge Mod Manager - Login") {
    private val usrName = SimpleObjectProperty<String>()
    private val passwd = SimpleObjectProperty<String>()
    private val saveSession = SimpleObjectProperty<Boolean>()

    private val isLoggingIn = SimpleObjectProperty<Boolean>(false)
    private val errMessage = SimpleObjectProperty<String>("")

    var loginOkay = false

    init {
        centerOnScreen(this)
    }

    override val root = cstWindowFrame(this,
            gridpane {
                row {
                    imageview("logo.png").gridpaneConstraints {
                        columnSpan = 2
                        hAlignment = HPos.CENTER
                    }
                    rowConstraints += RowConstraints(120.0)
                }
                row {
                    label("Please login with your CurseForge credentials").gridpaneConstraints {
                        columnSpan = 2
                        hAlignment = HPos.CENTER
                    }
                    rowConstraints += RowConstraints(30.0)
                }
                row {
                    label("Username") {
                        columnConstraints += ColumnConstraints(65.0)
                    }
                    textfield(usrName).disableProperty().bind(this@Login.isLoggingIn)
                    rowConstraints += RowConstraints(30.0)
                }
                row {
                    label("Password")
                    passwordfield(passwd).disableProperty().bind(this@Login.isLoggingIn)
                    rowConstraints += RowConstraints(30.0)
                }
                row {
                    checkbox("Remember me", saveSession) {
                        disableProperty().bind(this@Login.isLoggingIn)
                        gridpaneConstraints {
                            columnSpan = 2
                            hAlignment = HPos.CENTER
                        }
                    }
                    rowConstraints += RowConstraints(30.0)
                }
                row {
                    hbox {
                        pane {
                            button("Login") {
                                minWidth = 100.0
                                maxWidth = 100.0
                                action {
                                    tryLogin()
                                }
                                disableProperty().bind(this@Login.isLoggingIn)
                            }
                        }
                        pane {
                            button("Cancel") {
                                minWidth = 100.0
                                maxWidth = 100.0
                                action {
                                    close()
                                }
                                disableProperty().bind(this@Login.isLoggingIn)
                            }
                        }
                        gridpaneConstraints {
                            columnSpan = 2
                        }
                        alignment = Pos.TOP_CENTER
                        spacing = 10.0
                    }
                    rowConstraints += RowConstraints(30.0)
                }
                row {
                    label(errMessage) {
                        style {
                            textFill = Color.FIREBRICK
                        }
                        gridpaneConstraints {
                            columnSpan = 2
                            hAlignment = HPos.CENTER
                        }
                    }
                    rowConstraints += RowConstraints(25.0)
                }
            }, cstCloseAction = { this.close() }, canResize = false
    )

    private fun tryLogin() {
        if( this.usrName.isNull.value ) {
            this.errMessage.set("Username must be entered!")
            return
        }
        if( this.passwd.isNull.value ) {
            this.errMessage.set("Password must be entered!")
            return
        }
        this.isLoggingIn.set(true)
        CurseRestAPIIntf.createSession(this.usrName.get(), this.passwd.get(), {
            if( CurseRestAPIIntf.loginInfo.status == "Success" ) {
                if (this.saveSession.isNotNull.value && this.saveSession.get()) {
                    CurseRestAPIIntf.saveSession()
                }
                Application.invokeLater({
                    this.loginOkay = true
                    this.close()
                })
            } else {
                Application.invokeLater({
                    this.errMessage.set("Error during login: " + CurseRestAPIIntf.loginInfo.status)
                    this.isLoggingIn.set(false)
                })
            }
        })
    }
}