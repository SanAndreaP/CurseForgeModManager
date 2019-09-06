package de.sanandrew.apps.cursemodmgr.pack

import de.sanandrew.apps.cursemodmgr.MainApp
import de.sanandrew.apps.cursemodmgr.util.I18n
import de.sanandrew.apps.cursemodmgr.util.cstWindowFrame
import de.sanandrew.apps.cursemodmgr.util.getImgFromBase64GZip
import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.OverrunStyle
import javafx.scene.control.ScrollPane
import javafx.scene.control.Tooltip
import javafx.scene.effect.BoxBlur
import javafx.scene.effect.Effect
import javafx.scene.input.MouseButton
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.stage.StageStyle
import tornadofx.*

class Packs: View(I18n.translate("title")) {
    private val wndEffect = SimpleObjectProperty<Effect>()
    private val mainPane = flowpane {
        hgap = 10.0
        vgap = 10.0

        paddingAll = 10.0

        pane {
            addClass("newPackPane")
            setPrefSize(120.0, 180.0)
            hgrow = Priority.NEVER
            vbox {
                label("+") {
                    style {
                        fontSize = Dimension(40.0, Dimension.LinearUnits.pt)
                    }
                    alignment = Pos.CENTER
                    prefWidthProperty().bind((parent as VBox).widthProperty())
                }
                label("new pack") {
                    alignment = Pos.CENTER
                    prefWidthProperty().bind((parent as VBox).widthProperty())
                }
                alignment = Pos.CENTER
                prefWidthProperty().bind((parent as Pane).widthProperty())
                prefHeightProperty().bind((parent as Pane).heightProperty())
            }
            setOnMouseClicked { event ->
                if( event.button == MouseButton.PRIMARY ) {
                    val pack = openPackDialog()
                    if (pack != null) {
                        MinecraftModpacks.addPack(pack)
                        MinecraftModpacks.savePacks()
                        addNewPackPane(pack)
                    }
                }
            }
        }
    }

    init {
        MainApp.initWindow(this)
        MinecraftModpacks.loadPacks()
        MinecraftModpacks.getPacks().forEach(this::addNewPackPane)
    }

    override val root = cstWindowFrame(this, vbox {
        label(I18n.translate("packs")) {
            style {
                fontSize = Dimension(15.0, Dimension.LinearUnits.pt)
            }
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
    }, hasMinBtn = true, hasMaxRstBtn = true)

    private fun addNewPackPane(pack: MinecraftModpacks.Modpack) {
        this.mainPane.children.add(0,
            vbox {
                addClass("packPane")
                val packTitle = SimpleObjectProperty<String>(pack.title)
                val packImg = SimpleObjectProperty(if(pack.img != null) getImgFromBase64GZip(pack.img!!.width, pack.img!!.height, pack.img!!.data) else null)

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

                        setOnMouseClicked {
                            openPackDialog(pack)
                            MinecraftModpacks.savePacks()
                            packTitle.set(pack.title)
                            packImg.set(if(pack.img != null) getImgFromBase64GZip(pack.img!!.width, pack.img!!.height, pack.img!!.data) else null)
                        }
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
            }
        )
    }

    private fun openPackDialog(pack: MinecraftModpacks.Modpack? = null): MinecraftModpacks.Modpack? {
        wndEffect.set(BoxBlur(5.0, 5.0, 3))

        val md = if(pack != null) PackDialog(pack) else PackDialog()
        md.openModal(StageStyle.UNDECORATED, block = true)

        wndEffect.set(BoxBlur(0.0, 0.0, 0))

        return md.pack
    }
}