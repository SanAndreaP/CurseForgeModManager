package de.sanandrew.apps.cursemodmgr.css.color

import de.sanandrew.apps.cursemodmgr.css.CssMain
import de.sanandrew.apps.cursemodmgr.css.CssPacksList
import de.sanandrew.apps.cursemodmgr.css.CssWindow
import de.sanandrew.apps.cursemodmgr.newColor
import javafx.scene.effect.BlurType
import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import tornadofx.*

class CssLightFirebrick: Stylesheet() {
    companion object {
        val firebrick = newColor(178, 34, 34) // 0
        val darkFirebrick = newColor(142, 27, 27) // s2
        val lightFirebrick = newColor(208, 122, 122) // t4
        val brightFirebrick = newColor(224, 166, 166) // t6
        val whiteFirebrick = newColor(247, 232, 232) // t9

        val flatFocus = mixin {
            effect = DropShadow(2.0, firebrick)
            borderColor += box(firebrick)
        }
        val flat = mixin {
            and(focused) {
                +flatFocus
            }
        }
    }

    init {
        button {
            +flat
        }
        textField {
            +flat
        }
        comboBox {
            +flat
        }
        checkBox {
            and(focused) {
                box {
                    +flatFocus
                }
            }
        }
        comboBoxPopup {
            cell {
                and(hover) {
                    backgroundColor += whiteFirebrick
                    textFill = Color.BLACK
                }
                and(selected) {
                    backgroundColor += firebrick
                    textFill = Color.WHITE
                }
            }
        }

        CssWindow.windowBg {
            effect = DropShadow(2.0, firebrick)
            borderColor += tornadofx.box(firebrick)
        }
        CssWindow.windowContent {
            backgroundColor += Color.WHITESMOKE
        }
        CssWindow.windowHeader {
            backgroundColor += LinearGradient(0.0, 0.0, 0.0, 1.0, true, CycleMethod.REPEAT,
                    Stop(0.0, brightFirebrick),
                    Stop(0.4, lightFirebrick),
                    Stop(0.4, firebrick),
                    Stop(1.0, darkFirebrick))
            borderColor += tornadofx.box(firebrick)
            button {
                and(CssWindow.minBtn, CssWindow.maxBtn, CssWindow.resBtn) {
                    and(hover) {
                        backgroundColor += whiteFirebrick
                        textFill = Color.BLACK
                    }
                }
            }
            label {
                textFill = Color.WHITE
                effect = DropShadow(BlurType.ONE_PASS_BOX, Color.BLACK, 2.0, 1.0, 0.0, 0.0)
            }
        }


        CssPacksList.packsArea {
            borderColor += tornadofx.box(firebrick)
        }
        CssPacksList.packPane {
            backgroundColor += whiteFirebrick
            effect = DropShadow(5.0, firebrick)
            borderColor += tornadofx.box(firebrick)
            label {
                borderColor += box(firebrick)
            }
        }
        CssPacksList.newPackPane {
            backgroundColor += Color.LIGHTGRAY
            effect = DropShadow(5.0, Color.GRAY)
        }
    }
}