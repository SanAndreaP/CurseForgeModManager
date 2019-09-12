package de.sanandrew.apps.cursemodmgr.css.color

import javafx.scene.paint.Color

class CssLightFirebrick: CssColors() {
    override fun initColors() {
        this.mainColor = Color.rgb(178, 34, 34)
        this.windowHeaderBottomStart = this.mainColor
        this.windowHeaderBottomEnd = Color.rgb(142, 27, 27)
        this.windowHeaderTopEnd = Color.rgb(200, 60, 60)
        this.windowHeaderTopStart = Color.rgb(208, 90, 90)
        this.buttonHoverBackground = Color.rgb(247, 232, 232)

        this.progressBorder = this.mainColor.deriveColor(0.0, 0.4, 1.0, 1.0)
        this.progressBar = this.mainColor

        this.packBackground = Color.gray(0.0, 0.1)
        this.packButtonPanel = Color.gray(0.0, 0.1)
    }
}