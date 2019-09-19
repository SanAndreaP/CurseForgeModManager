package de.sanandrew.apps.cursemodmgr.css

import tornadofx.*

class CssPacks : Stylesheet() {
    companion object {
        val packsArea by cssclass("packsArea")
        val packPane by cssclass("packPane")
        val packPaneBottom by cssclass("packPaneBottom")
        val newPackPane by cssclass("newPackPane")
        val thumbPane by cssclass("thumbPane")
    }

    init {
        packsArea {
            borderWidth += box(1.px, 1.px, 0.px, 1.px)
        }

        packPane {
            thumbPane {
                padding = box(5.px)
            }
        }
    }
}