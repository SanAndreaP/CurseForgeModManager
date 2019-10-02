package de.sanandrew.apps.cursemodmgr.pack

import de.sanandrew.apps.cursemodmgr.css.CssDef
import de.sanandrew.apps.cursemodmgr.css.CssPacks
import de.sanandrew.apps.cursemodmgr.main.CustomDialog
import de.sanandrew.apps.cursemodmgr.util.getImgFromBase64GZip
import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.ButtonType
import javafx.scene.control.OverrunStyle
import javafx.scene.control.Tooltip
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import tornadofx.*

class PackPane(private val pack: MinecraftModpacks.Modpack, parent: Node) : Region() {
    init {
        vbox {
            addClass(CssPacks.packPane)
            val packTitle = SimpleObjectProperty<String>(pack.title)
            val packImg = SimpleObjectProperty(if(pack.img != null) getImgFromBase64GZip(pack.img!!.width, pack.img!!.height, pack.img!!.data) else null)

            minWidth = 120.0
            maxWidth = 120.0
            prefWidth = 120.0

            stackpane {
                addClass(CssPacks.thumbPane)
                imageview(packImg) {
                    setPrefSize(110.0, 110.0)
                    fitWidth = 110.0
                    fitHeight = 110.0
                }
            }
            vbox {
                addClass(CssPacks.packPaneBottom)
                hbox {
                    style {
                        alignment = Pos.CENTER
                        borderWidth += box(10.0.px, 0.0.px, 10.0.px, 0.0.px)
                        borderColor += box(Color.TRANSPARENT)
                    }
                    button("\ue067") {
                        addClass(CssDef.icoFont)
                        setMinSize(25.0, 25.0)
                        setMaxSize(25.0, 25.0)
                        textOverrun = OverrunStyle.CLIP
                        padding = Insets(0.0, 0.0, 1.5, 0.0)
                        tooltip = Tooltip("Edit Pack")

                        setOnMouseClicked {
                            PackDialog.openDialog(pack, parent)
                            MinecraftModpacks.savePacks()
                            packTitle.set(pack.title)
                            packImg.set(if(pack.img != null) getImgFromBase64GZip(pack.img!!.width, pack.img!!.height, pack.img!!.data) else null)
                        }
                    }
                    button("\ue123") {
                        addClass(CssDef.icoFont)
                        setMinSize(25.0, 25.0)
                        setMaxSize(25.0, 25.0)
                        textOverrun = OverrunStyle.CLIP
                        padding = Insets(0.0, 0.0, 1.5, 0.0)
                        tooltip = Tooltip("Delete Pack")

                        setOnMouseClicked {
                            val res = CustomDialog.openDialog("Delete Modpack", "Do you want to delete this modpack?", parent, ButtonType.YES, ButtonType.NO)
                            if( res.orElse(ButtonType.NO) == ButtonType.YES ) {
                                MinecraftModpacks.removePack(pack)
                                MinecraftModpacks.savePacks()
                                this@PackPane.removeFromParent()
                            }
                        }
                    }
                    button("\ue0b0") {
                        addClass(CssDef.icoFont)
                        setMinSize(25.0, 25.0)
                        setMaxSize(25.0, 25.0)
                        textOverrun = OverrunStyle.CLIP
                        padding = Insets(0.0, 0.0, 1.5, 0.0)
                        tooltip = Tooltip("Play Pack")
                    }
                    button("\ue114") {
                        addClass(CssDef.icoFont)
                        setMinSize(25.0, 25.0)
                        setMaxSize(25.0, 25.0)
                        textOverrun = OverrunStyle.CLIP
                        padding = Insets(0.0, 0.0, 1.5, 0.0)
                        tooltip = Tooltip("Show/Edit Modlist")
                    }
                }
                label(packTitle) {
                    style {
                        alignment = Pos.CENTER
                        borderWidth += box(0.px, 5.0.px, 5.0.px, 5.0.px)
                        borderColor += box(Color.TRANSPARENT)
                    }
                    onHover {
                        isWrapText = it
                    }
                    prefWidthProperty().bind((parent as VBox).widthProperty())
                }
            }
        }
    }
}