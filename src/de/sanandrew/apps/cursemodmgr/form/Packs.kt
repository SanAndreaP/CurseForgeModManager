package de.sanandrew.apps.cursemodmgr.form

import de.sanandrew.apps.cursemodmgr.MainApp
import de.sanandrew.apps.cursemodmgr.css.CssPacksList
import de.sanandrew.apps.cursemodmgr.css.color.CssLightFirebrick
import de.sanandrew.apps.cursemodmgr.css.color.CssLightSteelblue
import de.sanandrew.apps.cursemodmgr.cstWindowFrame
import de.sanandrew.apps.cursemodmgr.getImgFromBase64GZip
import de.sanandrew.apps.cursemodmgr.pack.Modpacks
import de.sanandrew.apps.cursemodmgr.pack.PackDialog
import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.OverrunStyle
import javafx.scene.control.ScrollPane
import javafx.scene.control.Tooltip
import javafx.scene.effect.BoxBlur
import javafx.scene.effect.Effect
import javafx.scene.image.Image
import javafx.scene.input.MouseButton
import javafx.scene.layout.Priority
import javafx.scene.text.Font
import javafx.stage.StageStyle
import tornadofx.*

class Packs: View("CurseForge Mod Manager") {
    private val wndEffect = SimpleObjectProperty<Effect>()
    private val mainPane = flowpane {
        hgap = 10.0
        vgap = 10.0

        padding = Insets(10.0)

        pane {
            addClass("newPackPane")
            setPrefSize(120.0, 180.0)
            hgrow = Priority.NEVER
            label("+")
            label("new pack")
            setOnMouseClicked { event ->
                if( event.button == MouseButton.PRIMARY ) {
                    val pack = openPackDialog()
                    if (pack != null) {
                        Modpacks.packs.add(pack)
                        Modpacks.savePacks()
                        addNewPackPane(pack)
                    }
                } else {
                    removeStylesheet(CssLightFirebrick::class)
                    importStylesheet(CssLightSteelblue::class)
                }
            }
        }
    }

    init {
        MainApp.initWindow(this)
        Modpacks.loadPacks()
        for( pack in Modpacks.packs ) {
            addNewPackPane(pack)
        }
    }

    override val root = cstWindowFrame(this, vbox {
        label("Modpacks") {
            font = Font.font(16.0)
        }
        scrollpane {
            vbarPolicy = ScrollPane.ScrollBarPolicy.AS_NEEDED
            isFitToWidth = true

            addClass("packsArea")
            add(mainPane)

            vboxConstraints {
                hgrow = Priority.ALWAYS
                vgrow = Priority.ALWAYS
            }
        }
        hgrow = Priority.ALWAYS
        vgrow = Priority.ALWAYS
        effectProperty().bind(wndEffect)
    }, true, true)

    private fun addNewPackPane(pack: Modpacks.Modpack) {
        this.mainPane.children.add(0,
            vbox {
                addClass("packPane")
                val packTitle = SimpleObjectProperty<String>(pack.title)
                val packImg = SimpleObjectProperty<Image?>(if(pack.img != null) getImgFromBase64GZip(pack.img!!.width, pack.img!!.height, pack.img!!.data) else null)

                setMinSize(120.0, 180.0)
                stackpane {
                    addClass("thumbPane")
                    imageview(packImg) {
                        setPrefSize(110.0, 110.0)
                        fitWidth = 110.0
                        fitHeight = 110.0
                    }
                }
                label(packTitle) {
                    style {
                        alignment = Pos.CENTER
                    }
                    maxWidth = 120.0
                    vgrow = Priority.ALWAYS
                }
                hbox {
                    alignment = Pos.CENTER
                    button("\ue067") {
                        addClass("icoFont")
                        setMinSize(25.0, 25.0)
                        setMaxSize(25.0, 25.0)
                        textOverrun = OverrunStyle.CLIP
                        padding = Insets(0.0, 0.0, 1.5, 0.0)
                        tooltip = Tooltip("Edit Pack")
                    }
                    button("\ue123") {
                        addClass("icoFont")
                        setMinSize(25.0, 25.0)
                        setMaxSize(25.0, 25.0)
                        textOverrun = OverrunStyle.CLIP
                        padding = Insets(0.0, 0.0, 1.5, 0.0)
                        tooltip = Tooltip("Delete Pack")
                    }
                    button("\ue0b0") {
                        addClass("icoFont")
                        setMinSize(25.0, 25.0)
                        setMaxSize(25.0, 25.0)
                        textOverrun = OverrunStyle.CLIP
                        padding = Insets(0.0, 0.0, 1.5, 0.0)
                        tooltip = Tooltip("Play Pack")
                    }
                    button("\ue114") {
                        addClass("icoFont")
                        setMinSize(25.0, 25.0)
                        setMaxSize(25.0, 25.0)
                        textOverrun = OverrunStyle.CLIP
                        padding = Insets(0.0, 0.0, 1.5, 0.0)
                        tooltip = Tooltip("Show/Edit Modlist")
                    }
                }
                setOnMouseClicked {
                    openPackDialog(pack)
                    Modpacks.savePacks()
                    packTitle.set(pack.title)
                    packImg.set(if(pack.img != null) getImgFromBase64GZip(pack.img!!.width, pack.img!!.height, pack.img!!.data) else null)
                }
            }
        )
    }

    private fun openPackDialog(pack: Modpacks.Modpack? = null): Modpacks.Modpack? {
        wndEffect.set(BoxBlur(5.0, 5.0, 3))

        val md = if(pack != null) PackDialog(pack) else PackDialog()
        md.openModal(StageStyle.UNDECORATED, block = true)

        wndEffect.set(BoxBlur(0.0, 0.0, 0))

        return md.pack
    }
}