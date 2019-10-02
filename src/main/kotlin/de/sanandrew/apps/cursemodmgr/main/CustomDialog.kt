package de.sanandrew.apps.cursemodmgr.main

import de.sanandrew.apps.cursemodmgr.css.CssWindow
import de.sanandrew.apps.cursemodmgr.util.windowFrame
import javafx.scene.Node
import javafx.scene.control.ButtonType
import javafx.scene.effect.BoxBlur
import javafx.stage.StageStyle
import tornadofx.*
import java.util.*

class CustomDialog(title: String, private val text: String, private val buttonTypes: Array<out ButtonType>): Fragment(title)
{
    companion object {
        fun openDialog(title: String, text: String, parent: Node, vararg buttonTypes: ButtonType = arrayOf( ButtonType.OK )): Optional<ButtonType> {
            parent.effect = BoxBlur(5.0, 5.0, 3)

            val md = CustomDialog(title, text, buttonTypes)
            md.openModal(StageStyle.UNDECORATED, block = true)

            parent.effect = null

            return Optional.ofNullable(md.result)
        }
    }

    var result: ButtonType? = null
    private val frame = windowFrame({this.currentStage ?: this.primaryStage}, this.title) {
        canResize = false
        canMinimize = false

        gridpane {
            hgap = 5.0
            vgap = 5.0

            row {
                label(text) {
                    padding = insets(15.0)
                    maxWidth = 500.0
                    isWrapText = true
                }
            }
            row {
                flowpane {
                    addClass(CssWindow.buttonPanel)
                    for (btn in buttonTypes) {
                        button(btn.text) {
                            setOnMouseClicked {
                                this@CustomDialog.result = btn
                                this@CustomDialog.close()
                            }
                        }
                    }
                }
            }
        }
    }

    override val root = this.frame

    override fun onBeforeShow() {
        super.onBeforeShow()
        this.frame.initialize()
    }
}