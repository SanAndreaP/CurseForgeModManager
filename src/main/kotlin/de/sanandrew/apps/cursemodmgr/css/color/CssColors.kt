package de.sanandrew.apps.cursemodmgr.css.color

import de.sanandrew.apps.cursemodmgr.css.CssPacks
import de.sanandrew.apps.cursemodmgr.css.CssWindow
import javafx.scene.effect.BlurType
import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import tornadofx.*

abstract class CssColors: Stylesheet() {
    // main colors
    lateinit var mainColor: Color
    lateinit var windowHeaderBottomStart: Color
    lateinit var windowHeaderBottomEnd: Color
    lateinit var windowHeaderTopEnd: Color
    lateinit var windowHeaderTopStart: Color
    lateinit var buttonHoverBackground: Color
    var text: Color = Color.BLACK
    var windowBackground: Color = Color.WHITESMOKE
    var textSelected: Color = Color.WHITE
    var textSelectedBackground: Color = Color.STEELBLUE

    // loading screen
    lateinit var progressBorder: Color
    lateinit var progressBar: Color

    // pack list
    lateinit var packBackground: Color
    lateinit var packButtonPanel: Color

    val flatFocus: CssSelectionBlock
    val flat: CssSelectionBlock

    init {
        this.initColors()

        flatFocus = mixin {
            effect = DropShadow(2.0, mainColor)
            borderColor += box(mainColor)
        }
        flat = mixin {
            and(focused) {
                +flatFocus
            }
        }

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
                    backgroundColor += buttonHoverBackground
                    textFill = text
                }
                and(selected) {
                    backgroundColor += textSelectedBackground
                    textFill = textSelected
                }
            }
        }

        CssWindow.windowBg {
            effect = DropShadow(2.0, mainColor)
            borderColor += box(mainColor)
        }
        CssWindow.windowContent {
            backgroundColor += windowBackground
        }
        CssWindow.windowHeader {
            backgroundColor += LinearGradient(0.0, 0.0, 0.0, 1.0, true, CycleMethod.REPEAT,
                    Stop(0.0, windowHeaderTopStart),
                    Stop(0.4, windowHeaderTopEnd),
                    Stop(0.4, windowHeaderBottomStart),
                    Stop(1.0, windowHeaderBottomEnd))
            borderColor += box(mainColor)
            button {
                and(CssWindow.minBtn, CssWindow.maxBtn, CssWindow.resBtn) {
                    and(hover) {
                        backgroundColor += buttonHoverBackground
                        textFill = text
                    }
                }
            }
            label {
                textFill = Color.WHITE
                fontSize = 12.pt
                effect = DropShadow(BlurType.ONE_PASS_BOX, Color.BLACK, 2.0, 1.0, 0.0, 0.0)
            }
        }
        CssWindow.progressCircle {
            stroke = progressBorder
        }
        CssWindow.progressArc {
            fill = progressBar
        }


        CssPacks.packsArea {
            borderColor += box(mainColor)
        }
        CssPacks.packPane {
            backgroundColor += packBackground
            and(hover) {
                backgroundColor += buttonHoverBackground
            }
        }
        CssPacks.newPackPane {
            backgroundColor += packBackground
        }
        CssPacks.packPaneBottom {
            backgroundColor += packButtonPanel
        }
    }

    abstract fun initColors()
}