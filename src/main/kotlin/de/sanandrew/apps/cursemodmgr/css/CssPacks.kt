package de.sanandrew.apps.cursemodmgr.css

import tornadofx.*

class CssPacks : Stylesheet() {
    companion object {
        val packPane by cssclass("packPane")
        val packPaneBottom by cssclass("packPaneBottom")
        val thumbPane by cssclass("thumbPane")
    }

    init {
        packPane {
            thumbPane {
                padding = box(5.px)
            }
        }
    }
}