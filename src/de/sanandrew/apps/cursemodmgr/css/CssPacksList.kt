package de.sanandrew.apps.cursemodmgr.css

import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import tornadofx.*

class CssPacksList: Stylesheet() {
    companion object {
        val packsArea by cssclass("packsArea")
        val packPane by cssclass("packPane")
        val newPackPane by cssclass("newPackPane")
        val thumbPane by cssclass("thumbPane")
    }

    init {
        packsArea {
            borderWidth += box(1.px, 1.px, 0.px, 1.px)
        }

        packPane {
            borderWidth += box(1.px)
            borderRadius += box(5.px)
            backgroundRadius += box(5.px)
            label {
                padding = box(3.px, 0.px, 0.px, 0.px)
                borderWidth += box(1.px, 0.px, 0.px, 0.px)
            }
            thumbPane {
                padding = box(5.px)
                backgroundRadius += box(5.px, 5.px, 0.px, 0.px)
            }
        }
    }
}