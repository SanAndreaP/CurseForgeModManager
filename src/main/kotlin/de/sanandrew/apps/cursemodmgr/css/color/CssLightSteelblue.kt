package de.sanandrew.apps.cursemodmgr.css.color

import de.sanandrew.apps.cursemodmgr.css.CssPacks
import de.sanandrew.apps.cursemodmgr.css.CssWindow
import de.sanandrew.apps.cursemodmgr.util.newColor
import javafx.scene.effect.BlurType
import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import tornadofx.*

class CssLightSteelblue : Stylesheet() {
    companion object {
        val steelblue = newColor(70, 130, 180)
        val darkSteelblue = newColor(56, 104, 144)
        val lightSteelblue = newColor(144, 180, 210)
        val brightSteelblue = newColor(181, 205, 225)
        val whiteSteelblue = newColor(236, 242, 247)

        val flatFocus = mixin {
            effect = DropShadow(2.0, steelblue)
            borderColor += box(steelblue)
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
                    backgroundColor += whiteSteelblue
                    textFill = Color.BLACK
                }
                and(selected) {
                    backgroundColor += steelblue
                    textFill = Color.WHITE
                }
            }
        }

        CssWindow.windowBg {
            effect = DropShadow(2.0, steelblue)
            borderColor += box(steelblue)
        }
        CssWindow.windowContent {
            backgroundColor += Color.WHITESMOKE
        }
        CssWindow.windowHeader {
            backgroundColor += LinearGradient(0.0, 0.0, 0.0, 1.0, true, CycleMethod.REPEAT,
                    Stop(0.0, brightSteelblue),
                    Stop(0.4, lightSteelblue),
                    Stop(0.4, steelblue),
                    Stop(1.0, darkSteelblue))
            borderColor += box(steelblue)
            button {
                and(CssWindow.minBtn, CssWindow.maxBtn, CssWindow.resBtn) {
                    and(hover) {
                        backgroundColor += whiteSteelblue
                        textFill = Color.BLACK
                    }
                }
            }
            label {
                textFill = Color.WHITE
                effect = DropShadow(BlurType.ONE_PASS_BOX, Color.BLACK, 2.0, 1.0, 0.0, 0.0)
            }
        }
        CssWindow.progressCircle {
            stroke = steelblue.desaturate()
        }
        CssWindow.progressArc {
            fill = steelblue
        }


        CssPacks.packsArea {
            borderColor += box(steelblue)
        }
        CssPacks.packPane {
            backgroundColor += whiteSteelblue
            effect = DropShadow(5.0, steelblue)
            borderColor += box(steelblue)
            label {
                borderColor += box(steelblue)
            }
        }
        CssPacks.newPackPane {
            backgroundColor += Color.LIGHTGRAY
            effect = DropShadow(5.0, Color.GRAY)
        }
    }
}