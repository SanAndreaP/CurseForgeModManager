package de.sanandrew.apps.cursemodmgr.css

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
    }
    init {
        windowBg {
            backgroundInsets += box(0.px)
            snapToPixel = false
        }
        windowHeader {
            borderWidth += box(0.px, 0.px, 1.px, 0.px)
            button {
                +CssMain.flat
                CssMain.fontTypicons?.let { fontFamily = it.family }
                fontSize = 12.pt
                effect = DropShadow(0.0, Color.TRANSPARENT)
                borderWidth += box(0.px)
                backgroundColor += Color.TRANSPARENT
                textFill = Color.WHITE
                and(closeBtn) {
                    and(hover) {
                        backgroundColor += Color.RED
                    }
                }
            }
        }
    }
}