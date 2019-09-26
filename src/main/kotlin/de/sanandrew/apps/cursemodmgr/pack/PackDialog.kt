package de.sanandrew.apps.cursemodmgr.pack

import de.sanandrew.apps.cursemodmgr.curseapi.CFAPI
import de.sanandrew.apps.cursemodmgr.curseapi.Game
import de.sanandrew.apps.cursemodmgr.util.*
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.transformation.FilteredList
import javafx.geometry.HPos
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.effect.BoxBlur
import javafx.scene.image.Image
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import javafx.stage.StageStyle
import tornadofx.*
import java.io.File
import java.util.concurrent.Callable

class PackDialog constructor() : Fragment("New Modpack") {
    companion object {
        fun openPackDialog(pack: MinecraftModpacks.Modpack? = null, parent: Node): MinecraftModpacks.Modpack? {
            parent.effect = BoxBlur(5.0, 5.0, 3)

            val md = if(pack != null) PackDialog(pack) else PackDialog()
            md.openModal(StageStyle.UNDECORATED, block = true)

            parent.effect = null

            return md.pack
        }
    }

    var pack: MinecraftModpacks.Modpack? = null

    private val stdImage = Image("/add_image.png")
    private val packTitle = SimpleObjectProperty<String>()
    private val packGameVer = SimpleObjectProperty<Game.GameVersion>()
    private val packLoader = SimpleObjectProperty<Game.Modloader>()
    private val packDir = SimpleObjectProperty<String>()
    private val packImg = SimpleObjectProperty<Image?>(null)
    private val packImgDef = Bindings.createObjectBinding<Image>(Callable {
        packImg.get() ?: stdImage
    }, packImg)

    private val filteredLoaders = FilteredList<Game.Modloader>(CFAPI.Minecraft.modloader.toList().observable())
    private val errMsg = SimpleObjectProperty<String?>()

    constructor(pack: MinecraftModpacks.Modpack) : this() {
        this.title = "Edit Modpack - " + pack.title
        this.pack = pack

        this.packTitle.set(pack.title)
        this.packGameVer.set(pack.modLoader.getGameVersion(CFAPI.Minecraft))
        this.packLoader.set(pack.modLoader)
        this.packDir.set(pack.directory)
        if(pack.img != null) {
            this.packImg.set(getImgFromBase64GZip(pack.img!!.width, pack.img!!.height, pack.img!!.data))
        }
    }

    private val frame = windowFrame({this.currentStage ?: this.primaryStage}, this.title) {
        onClosing {
            close()
            false
        }

        canResize = false
        canMinimize = false

        gridpane {
            paddingAll = 15.0
            row {
                stackpane {
                    pane {
                        setMaxSize(200.0, 200.0)
                        imageview(packImgDef) {
                            fitWidth = 200.0
                            fitHeight = 200.0
                            border = Border(BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii(0.0), BorderWidths(1.0)))
                        }
                        setOnMouseClicked {
                            val fc = FileChooser()
                            fc.extensionFilters += FileChooser.ExtensionFilter("Image-Files", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif", "*.tif")
                            val imgUrl = fc.showOpenDialog(currentWindow)
                            if(imgUrl != null) {
                                val img = Image(imgUrl.toURI().toString())
                                if(img.width > 400.0 || img.height > 400.0) {
                                    errMsg.set("Image must be within 400x400 in size!")
                                } else {
                                    errMsg.set(null)
                                    packImg.set(img)
                                }
                            }
                        }
                    }
                    gridpaneConstraints {
                        columnSpan = 3
                    }
                }
                rowConstraints += RowConstraints(210.0)
            }
            row {
                label("Title") {
                    columnConstraints += ColumnConstraints(120.0)
                }
                textfield(packTitle) {
                    gridpaneConstraints {
                        columnSpan = 2
                    }
                }
                rowConstraints += RowConstraints(30.0)
            }
            row {
                label("Game Version")
                combobox<Game.GameVersion>(packGameVer, CFAPI.Minecraft.versions.toList()) {
                    valueProperty().addListener { _, _, v ->
                        filteredLoaders.setPredicate { e -> v == e.getGameVersion(CFAPI.Minecraft) }
                    }

                    cellFactory = getCellCallback { obj -> obj.versionString }
                    converter = getConverter<Game.GameVersion?> { obj -> obj?.versionString ?: "" }

                    gridpaneConstraints {
                        columnSpan = 2
                    }
                }
                rowConstraints += RowConstraints(30.0)
            }
            row {
                label("Modloader")
                combobox<Game.Modloader>(packLoader, filteredLoaders) {
                    disableProperty().bind(packGameVer.isNull)

                    cellFactory = getCellCallback { obj -> obj.name }
                    converter = getConverter<Game.Modloader?> { obj -> obj?.name ?: "" }

                    gridpaneConstraints {
                        columnSpan = 2
                    }
                }
                rowConstraints += RowConstraints(30.0)
            }
            row {
                label("Profile Directory")
                textfield(packDir)
                button("Choose...") {
                    action {
                        val dc = javafx.stage.DirectoryChooser()
                        dc.title = "Choose your Profile directory..."
                        if(getStringPropValue(packDir) != "") {
                            dc.initialDirectory = File(packDir.value)
                        }
                        val res: File? = dc.showDialog(currentWindow)
                        if(res != null) {
                            packDir.set(res.absolutePath)
                        }
                    }
                }
                rowConstraints += RowConstraints(30.0)
            }
            row {
                label(errMsg) {
                    visibleWhen(errMsg.isNotNull)
                    style {
                        textFill = Color.FIREBRICK
                    }
                    gridpaneConstraints {
                        marginBottom = 5.0
                        columnSpan = 3
                        hAlignment = HPos.CENTER
                    }
                }
                rowConstraints += RowConstraints(30.0)
            }
            row {
                hbox {
                    button("Save") {
                        action {
                            onSavePack()
                        }
                    }
                    button("Cancel") {
                        action {
                            close()
                        }
                    }
                    gridpaneConstraints {
                        columnSpan = 3
                    }
                    alignment = Pos.TOP_CENTER
                    spacing = 10.0
                }
                rowConstraints += RowConstraints(30.0)
            }
        }
    }
    override val root = this.frame

    override fun onBeforeShow() {
        super.onBeforeShow()
        this.frame.initialize()
    }

    private fun getStringPropValue(sop: SimpleObjectProperty<String>): String {
        return sop.takeIf { sop.isNotNull.value }?.value ?: ""
    }

    private fun onSavePack() {
        if(packTitle.isNull.value || packTitle.value == "") {
            errMsg.set("You need to enter a name for the pack")
        } else if(packLoader.isNull.value) {
            errMsg.set("You need to select a game and modloader version")
        } else {
            if(pack != null) {
                pack!!.title = packTitle.value
                pack!!.modLoader = packLoader.value
                pack!!.directory = getStringPropValue(packDir)
                if(packImg.value != null) {
                    val (width, height, data) = writeImgToBase64GZip(packImg.value!!)
                    pack!!.img = MinecraftModpacks.PackImg(width, height, data)
                }
            } else {
                pack = MinecraftModpacks.Modpack(packTitle.value, packLoader.value, getStringPropValue(packDir), null)
            }
            close()
        }
    }
}