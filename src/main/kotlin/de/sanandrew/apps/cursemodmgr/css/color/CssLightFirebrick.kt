package de.sanandrew.apps.cursemodmgr.css.color

import javafx.scene.paint.Color

class CssLightFirebrick : CssColors() {
    override fun initColors() {
        this.windowBorder = Color.rgb(178, 34, 34)
        this.windowHeaderBottomStart = this.windowBorder
        this.focusBorder = this.windowBorder
        this.focusBorderShadow = this.windowBorder
        this.windowHeaderBottomEnd = Color.rgb(142, 27, 27)
        this.windowHeaderTopEnd = Color.rgb(200, 60, 60)
        this.windowHeaderTopStart = Color.rgb(208, 90, 90)
        this.buttonBackground = Color.rgb(200, 60, 60)
        this.buttonHoverBackground = Color.rgb(247, 232, 232)

        this.progressBorder = this.windowBorder.deriveColor(0.0, 0.4, 1.0, 1.0)
        this.progressBar = this.windowBorder

        this.packBackground = Color.gray(0.0, 0.1)
        this.packButtonPanel = Color.gray(0.0, 0.1)
    }
}
