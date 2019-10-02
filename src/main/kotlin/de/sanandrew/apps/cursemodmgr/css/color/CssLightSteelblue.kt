package de.sanandrew.apps.cursemodmgr.css.color

import de.sanandrew.apps.cursemodmgr.css.CssMain
import javafx.scene.paint.Color

class CssLightSteelblue : CssMain() {
    override val windowBorder: Color             get() = Color.rgb(70, 130, 180)
    override val windowHeaderBottomStart: Color  get() = this.windowBorder
    override val focusBorder: Color              get() = this.windowBorder
    override val focusBorderShadow: Color        get() = this.windowBorder
    override val windowHeaderBottomEnd: Color    get() = Color.rgb(56, 104, 144)
    override val windowHeaderTopEnd: Color       get() = Color.rgb(181, 205, 225)
    override val windowHeaderTopStart: Color     get() = Color.rgb(144, 180, 210)
    override val buttonBackground: Color         get() = Color.rgb(60, 60, 200)
    override val buttonHoverBackground: Color    get() = Color.rgb(236, 242, 247)

    override val progressBorder: Color  get() = this.windowBorder.deriveColor(0.0, 0.4, 1.0, 1.0)
    override val progressBar: Color     get() = this.windowBorder

    override val packBackground: Color   get() = Color.gray(0.0, 0.1)
    override val packButtonPanel: Color  get() = Color.gray(0.0, 0.1)
}