package de.sanandrew.apps.cursemodmgr.css

import javafx.scene.effect.BlurType
import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import tornadofx.*

@Suppress("unused", "MemberVisibilityCanBePrivate")
abstract class CssMain : Stylesheet() {
    // window colors
    abstract val windowBorder: Color
    abstract val windowHeaderBottomStart: Color
    abstract val windowHeaderBottomEnd: Color
    abstract val windowHeaderTopEnd: Color
    abstract val windowHeaderTopStart: Color
    open val windowBackground: Color = Color.WHITESMOKE
    open val text: Color = Color.BLACK
    open val textSelected: Color = Color.WHITE
    open val textSelectedBackground: Color = Color.STEELBLUE
    open val errorText: Color = Color.FIREBRICK
    open val tabBorder: Color = Color.GAINSBORO
    open val tabHeader: Color = Color.WHITE
    open val tabInactive: Color = Color.GAINSBORO
    // button colors
    open val buttonText: Color = Color.BLACK
    open val buttonTextHover: Color = Color.BLACK
    open val buttonBackground: Color = Color.DARKGRAY
    open val buttonHoverBackground: Color = Color.LIGHTGRAY
    open val buttonBorder: Color = Color.GRAY
    // input colors
    open val inputText: Color = Color.BLACK
    open val inputBorder: Color = Color.GRAY
    open val inputBackground: Color = Color.WHITE
    abstract val focusBorder: Color
    abstract val focusBorderShadow: Color
    // loading screen
    abstract val progressBorder: Color
    abstract val progressBar: Color
    // pack list
    abstract val packBackground: Color
    abstract val packButtonPanel: Color

    init {
        // mixins
        val flatInput = mixin {
            +CssDef.flat
            textFill = inputText
            borderColor = multi(box(inputBorder))
            backgroundColor = multi(inputBackground)

            and(focused) {
                borderColor = multi(box(focusBorder))
                effect = javafx.scene.effect.InnerShadow(5.0, focusBorderShadow)
            }
        }

        // classes
        CssDef.errorText {
            textFill = errorText
        }

        // elements
        button {
            +CssDef.flat
            backgroundColor = multi(buttonBackground)
            textFill = buttonText
            borderColor = multi(box(buttonBorder))
            and(hover) {
                backgroundColor = multi(buttonHoverBackground)
                textFill = buttonTextHover
            }
        }
        label {
            +CssDef.flatFrameless
        }
        textField {
            +flatInput
        }
        checkBox {
            +CssDef.flatFrameless
            box {
                +flatInput
            }
        }
        comboBox {
            +flatInput
        }
        comboBoxPopup {
            cell {
                and(hover) {
                    backgroundColor = multi(buttonHoverBackground)
                    textFill = text
                }
                and(selected) {
                    backgroundColor = multi(textSelectedBackground)
                    textFill = textSelected
                }
            }
        }
        imageView {
            +CssDef.flat
        }
        scrollPane {
            +CssDef.flatFrameless
        }
        tabPane {
            +CssDef.flatFrameless
            tabHeaderArea {
                tabHeaderBackground {
                    backgroundColor = multi(tabHeader)
                    borderColor = multi(box(tabBorder))
                    borderWidth = multi(box(0.px, 0.px, 1.px, 0.px))
                }
                tab {
                    borderRadius = multi(box(0.px))
                    backgroundRadius = multi(box(0.px))
                    backgroundInsets = multi(box(0.px))
                    borderColor = multi(box(tabBorder))
                    borderWidth = multi(box(1.px))
                    backgroundColor = multi(tabInactive)
                    focusColor = Color.TRANSPARENT
                    faintFocusColor = Color.TRANSPARENT
                    and(selected) {
                        borderColor = multi(box(tabBorder, tabBorder, windowBackground, tabBorder))
                        backgroundColor = multi(windowBackground)
                    }
                    and(hover) {
                        backgroundColor = multi(buttonHoverBackground)
                    }
                }
            }
        }

        CssWindow.windowBg {
            borderColor = multi(box(windowBorder))
        }
        CssWindow.windowContent {
            backgroundColor = multi(windowBackground)
        }
        CssWindow.windowHeader {
            backgroundColor = multi(LinearGradient(0.0, 0.0, 0.0, 1.0, true, CycleMethod.REPEAT,
                                                   Stop(0.0, windowHeaderTopStart),
                                                   Stop(0.4, windowHeaderTopEnd),
                                                   Stop(0.4, windowHeaderBottomStart),
                                                   Stop(1.0, windowHeaderBottomEnd)))
            borderColor = multi(box(windowBorder))
            button {
                and(CssWindow.minBtn, CssWindow.maxBtn, CssWindow.resBtn) {
                    and(hover) {
                        backgroundColor = multi(buttonHoverBackground)
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

        CssPacks.packPane {
            backgroundColor = multi(packBackground)
            and(hover) {
                backgroundColor = multi(buttonHoverBackground)
            }
        }
        CssPacks.packPaneBottom {
            backgroundColor = multi(packButtonPanel)
        }
    }
}