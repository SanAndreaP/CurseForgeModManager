package de.sanandrew.apps.cursemodmgr.css.color

import javafx.scene.paint.Color

class CssLightSteelblue : CssColors() {
    override fun initColors() {
        this.mainColor = Color.rgb(70, 130, 180)
        this.windowHeaderBottomStart = this.mainColor
        this.windowHeaderBottomEnd = Color.rgb(56, 104, 144)
        this.windowHeaderTopEnd = Color.rgb(181, 205, 225)
        this.windowHeaderTopStart = Color.rgb(144, 180, 210)
        this.buttonHoverBackground = Color.rgb(236, 242, 247)
        this.progressBorder = this.mainColor.deriveColor(0.0, 0.4, 1.0, 1.0)
        this.progressBar = this.mainColor
    }
}