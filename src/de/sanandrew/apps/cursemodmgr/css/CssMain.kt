package de.sanandrew.apps.cursemodmgr.css

import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.text.Font
import tornadofx.*

class CssMain : Stylesheet() {
    companion object {
        val icoFont by cssclass("icoFont")

        val fontJosefinR: Font? = CssMain::class.java.getResourceAsStream("/fonts/JosefinSans-Regular.ttf").use { Font.loadFont(it, 10.0) }
        val fontJosefinB: Font? = CssMain::class.java.getResourceAsStream("/fonts/JosefinSans-Bold.ttf").use { Font.loadFont(it, 10.0) }
        val fontJosefinI: Font? = CssMain::class.java.getResourceAsStream("/fonts/JosefinSans-Italic.ttf").use { Font.loadFont(it, 10.0) }
        val fontTypicons: Font? = CssMain::class.java.getResourceAsStream("/fonts/typicons.ttf").use { Font.loadFont(it, 10.0) }

        val flat = mixin {
            fontJosefinR?.let { fontFamily = it.family }
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
        imageView {
            +flat
        }
        checkBox {
            +flatFrameless
            box {
                +flat
            }
        }
        scrollPane {
            +flatFrameless
        }
        label {
            +flatFrameless
        }
        icoFont {
            fontTypicons?.let { fontFamily = it.family }
            fontSize = 12.pt
        }
    }
}