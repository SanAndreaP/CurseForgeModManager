package de.sanandrew.apps.cursemodmgr.css.color

import de.sanandrew.apps.cursemodmgr.css.CssMain
import javafx.scene.paint.Color
import tornadofx.*

class CssLightFirebrick : CssMain() {
    companion object {
        @JvmStatic val MAIN_CLR = c("#B22222")

        @JvmStatic val WND_HEAD_TS = c("#D05A5A")
        @JvmStatic val WND_HEAD_TE = c("#C83C3C")
        @JvmStatic val WND_HEAD_BS = MAIN_CLR
        @JvmStatic val WND_HEAD_BE = c("#8E1B1B")

        @JvmStatic val BUTTON_BG = c("#C83C3C")
        @JvmStatic val BUTTON_HV_BG = c("#F7E8E8")
        @JvmStatic val BUTTON_TEXT = c("#FFF")

        @JvmStatic val PROGRESS_BAR = c("#7A1717")

        @JvmStatic val PACK_PANE_BG = c("#000", 0.1)
    }

    override val windowBorder: Color             get() = MAIN_CLR
    override val focusBorder: Color              get() = MAIN_CLR
    override val focusBorderShadow: Color        get() = MAIN_CLR
    override val windowHeaderTopStart: Color     get() = WND_HEAD_TS
    override val windowHeaderTopEnd: Color       get() = WND_HEAD_TE
    override val windowHeaderBottomStart: Color  get() = WND_HEAD_BS
    override val windowHeaderBottomEnd: Color    get() = WND_HEAD_BE
    override val buttonBackground: Color         get() = BUTTON_BG
    override val buttonText: Color               get() = BUTTON_TEXT
    override val buttonHoverBackground: Color    get() = BUTTON_HV_BG

    override val progressBorder: Color  get() = PROGRESS_BAR
    override val progressBar: Color     get() = MAIN_CLR

    override val packBackground: Color   get() = PACK_PANE_BG
    override val packButtonPanel: Color  get() = PACK_PANE_BG
}
