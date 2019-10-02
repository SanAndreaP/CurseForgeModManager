package de.sanandrew.apps.cursemodmgr.css

import javafx.geometry.Pos
import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import tornadofx.*

class CssWindow : Stylesheet() {
    companion object {
        val windowBg by cssclass("windowBg")
        val windowContent by cssclass("windowContent")
        val windowHeader by cssclass("windowHeader")
        val closeBtn by cssclass("closeBtn")
        val minBtn by cssclass("minBtn")
        val resBtn by cssclass("resBtn")
        val maxBtn by cssclass("maxBtn")
        val progressCircle by cssclass("progressCircle")
        val progressArc by cssclass("progressArc")
        val buttonPanel by cssclass("buttonPanel")
    }

    init {
        windowBg {
            snapToPixel = false
        }
        windowHeader {
            borderWidth = multi(box(0.px, 0.px, 1.px, 0.px))
            button {
                +CssDef.flat
                +CssDef.icoFontM
                effect = DropShadow(0.0, Color.TRANSPARENT)
                borderWidth = multi(box(0.px))
                backgroundColor = multi(Color.TRANSPARENT)
                textFill = Color.WHITE
                and(closeBtn) {
                    and(hover) {
                        backgroundColor = multi(Color.RED)
                    }
                }
            }
        }
        buttonPanel {
            hgap = 15.px
            vgap = 15.px
            padding = box(15.px)
            backgroundColor = multi(Color.WHITE)
            borderColor = multi(box(Color.GAINSBORO))
            borderWidth = multi(box(3.px, 0.px, 0.px, 0.px))

            alignment = Pos.CENTER

            button {
                padding = box(5.px, 15.px)
                minWidth = 65.px
            }
        }
    }
}