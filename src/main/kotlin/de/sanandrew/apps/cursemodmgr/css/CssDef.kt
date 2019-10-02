package de.sanandrew.apps.cursemodmgr.css

import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.text.Font
import tornadofx.*

class CssDef: Stylesheet() {
    companion object {
        val icoFont by cssclass("icoFont")
        val errorText by cssclass("errorText")

        val fontJosefinR: Font? = loadFont("/fonts/JosefinSans-Regular.ttf", 10.0)
        val fontJosefinB: Font? = loadFont("/fonts/JosefinSans-Bold.ttf", 10.0)
        val fontJosefinI: Font? = loadFont("/fonts/JosefinSans-Italic.ttf", 10.0)
        val fontTypicons: Font? = loadFont("/fonts/typicons.ttf", 10.0)

        // mixins
        val flat = mixin {
            fontJosefinR?.let { font = it }
            borderColor += box(c("#808080"))
            backgroundInsets += box(0.px)
            backgroundRadius += box(0.px)
            and(focused) {
                effect = DropShadow(0.0, Color.TRANSPARENT)
            }
        }
        val flatFrameless = mixin {
            +flat
            borderWidth += box(0.px)
            and(focused) {
                effect = DropShadow(0.0, Color.TRANSPARENT)
            }
        }
        val icoFontM = mixin {
            fontTypicons?.let { fontFamily = it.family }
            fontSize = 12.pt
        }
    }

    init {
        icoFont {
            +icoFontM
        }
        errorText { }
    }
}