package de.sanandrew.apps.cursemodmgr.pack

import de.sanandrew.apps.cursemodmgr.cstWindowFrame
import de.sanandrew.apps.cursemodmgr.curseapi.ModLoaders
import de.sanandrew.apps.cursemodmgr.getCellCallback
import de.sanandrew.apps.cursemodmgr.getConverter
import de.sanandrew.apps.cursemodmgr.getImgFromBase64GZip
import de.sanandrew.apps.cursemodmgr.util.SemVer
import de.sanandrew.apps.cursemodmgr.writeImgToBase64GZip
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.transformation.FilteredList
import javafx.geometry.HPos
import javafx.geometry.Pos
import javafx.scene.image.Image
import javafx.scene.layout.Border
import javafx.scene.layout.BorderStroke
import javafx.scene.layout.BorderStrokeStyle
import javafx.scene.layout.BorderWidths
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.RowConstraints
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import tornadofx.*
import java.io.File

class PackDialog constructor(): Fragment("New Modpack") {
    var pack: Modpacks.Modpack? = null

    private val stdImage = Image("/add_image.png")
    private val packTitle = SimpleObjectProperty<String>()
    private val packGameVer = SimpleObjectProperty<SemVer>()
    private val packLoader = SimpleObjectProperty<ModLoaders.ModLoader>()
    private val packMcDir = SimpleObjectProperty<String>()
    private val packProfDir = SimpleObjectProperty<String>()
    private val packImg = SimpleObjectProperty<Image>(stdImage)

    private val filteredLoaders = FilteredList<ModLoaders.ModLoader>(ModLoaders.loaderList.loaders.observable())
    private val errMsg = SimpleObjectProperty<String?>()

    constructor(pack: Modpacks.Modpack): this() {
        this.title = "Edit Modpack - " + pack.title
        this.pack = pack

        this.packTitle.set(pack.title)
        this.packGameVer.set(SemVer.getMcVer(pack.modLoader.GameVersion))
        this.packLoader.set(pack.modLoader)
        this.packMcDir.set(pack.mcDirectory)
        this.packProfDir.set(pack.profileDirectory)
        if( pack.img != null ) {
            this.packImg.set(getImgFromBase64GZip(pack.img!!.width, pack.img!!.height, pack.img!!.data))
        }
    }

    override val root = cstWindowFrame(this, gridpane {
        row {
            stackpane {
                pane {
                    setMaxSize(200.0, 200.0)
                    imageview(packImg) {
                        fitWidth = 200.0
                        fitHeight = 200.0
                        border = Border(BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii(0.0), BorderWidths(1.0)))
                    }
                    setOnMouseClicked {
                        val fc = FileChooser()
                        fc.extensionFilters += FileChooser.ExtensionFilter("Image-Files", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif", "*.tif")
                        val imgUrl = fc.showOpenDialog(currentWindow)
                        if (imgUrl != null) {
                            val img = Image(imgUrl.toURI().toString())
                            if( img.width > 400.0 || img.height > 400.0 ) {
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
            combobox(packGameVer, ModLoaders.gameVersions) {
                valueProperty().addListener { _, _, v ->
                    filteredLoaders.setPredicate { e -> SemVer.getMcVer(e.GameVersion) == v }
                }

                cellFactory = getCellCallback { obj -> obj.getMcVerString() }
                converter = de.sanandrew.apps.cursemodmgr.getConverter<SemVer?> { obj -> obj?.getMcVerString() ?: "" }

                gridpaneConstraints {
                    columnSpan = 2
                }
            }
            rowConstraints += RowConstraints(30.0)
        }
        row {
            label("Modloader")
            combobox<ModLoaders.ModLoader>(packLoader, filteredLoaders) {
                disableProperty().bind(packGameVer.isNull)

                cellFactory = getCellCallback { obj -> obj.Name }
                converter = getConverter<ModLoaders.ModLoader?> { obj -> obj?.Name ?: "" }

                gridpaneConstraints {
                    columnSpan = 2
                }
            }
            rowConstraints += RowConstraints(30.0)
        }
        row {
            label("Minecraft Directory")
            textfield(packMcDir)
            button("Choose...") {
                action {
                    val dc = javafx.stage.DirectoryChooser()
                    dc.title = "Choose your Minecraft directory..."
                    if( getStringPropValue(packMcDir) != "" ) {
                        dc.initialDirectory = File(packMcDir.value)
                    }
                    packMcDir.set(dc.showDialog(currentWindow).absolutePath)
                }
            }
            rowConstraints += RowConstraints(30.0)
        }
        row {
            label("Profile Directory")
            textfield(packProfDir)
            button("Choose...") {
                action {
                    val dc = javafx.stage.DirectoryChooser()
                    dc.title = "Choose your Profile directory..."
                    if( getStringPropValue(packProfDir) != "" ) {
                        dc.initialDirectory = File(packProfDir.value)
                    } else if( getStringPropValue(packMcDir) != "" ) {
                        dc.initialDirectory = File(packMcDir.value)
                    }
                    packProfDir.set(dc.showDialog(currentWindow).absolutePath)
                }
            }
            rowConstraints += RowConstraints(30.0)
        }
        row {
            label(errMsg) {
                visibleProperty().bind(errMsg.isNotNull)
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
    }, cstCloseAction = { close() })

    private fun getStringPropValue(sop: SimpleObjectProperty<String>): String {
        return sop.takeIf { sop.isNotNull.value }?.value ?: ""
    }

    private fun onSavePack() {
        if( packTitle.isNull.value || packTitle.value == "" ) {
            errMsg.set("You need to enter a name for the pack")
        } else if( packLoader.isNull.value ) {
            errMsg.set("You need to select a game and modloader version")
        } else {
            if( pack != null ) {
                pack!!.title = packTitle.value
                pack!!.modLoader = packLoader.value
                pack!!.mcDirectory = getStringPropValue(packMcDir)
                pack!!.profileDirectory = getStringPropValue(packProfDir)
                if( packImg != stdImage ) {
                    val (width, height, data) = writeImgToBase64GZip(packImg.value)
                    pack!!.img = Modpacks.PackImg(width, height, data)
                }
            } else {
                pack = Modpacks.Modpack(packTitle.value, packLoader.value, getStringPropValue(packMcDir), getStringPropValue(packProfDir), null)
            }
            close()
        }
    }
}