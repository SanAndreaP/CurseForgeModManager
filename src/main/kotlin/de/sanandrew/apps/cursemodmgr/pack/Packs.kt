package de.sanandrew.apps.cursemodmgr.pack

import de.sanandrew.apps.cursemodmgr.MainApp
import de.sanandrew.apps.cursemodmgr.util.I18n
import de.sanandrew.apps.cursemodmgr.util.windowFrame
import javafx.application.Platform
import javafx.scene.control.ScrollPane
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.Priority
import tornadofx.*

class Packs : View(I18n.translate("title")) {
    private val installedPacksPane = flowpane {
        hgap = 10.0
        vgap = 10.0

        paddingAll = 10.0
    }

    init {
        Platform.runLater {
            MinecraftModpacks.loadPacks()
            MinecraftModpacks.getPacks().forEach(this::addNewPackPane)
        }
    }

    private val main = vbox {
        val mainInner = this

        hgrow = Priority.ALWAYS
        vgrow = Priority.ALWAYS

        tabpane {
            hgrow = Priority.ALWAYS
            vgrow = Priority.ALWAYS
            tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE

            lateinit var currTab: Tab

            tab(I18n.translate("packs.installed")) {
                scrollpane {
                    addClass("packsArea")

                    vbarPolicy = ScrollPane.ScrollBarPolicy.AS_NEEDED
                    isFitToWidth = true

                    add(installedPacksPane)

                    vboxConstraints {
                        hgrow = Priority.ALWAYS
                        vgrow = Priority.ALWAYS
                    }
                }
                setOnSelectionChanged { currTab = this }
                currTab = this
            }
            tab(I18n.translate("packs.search")) {
                setOnSelectionChanged { currTab = this }
            }
            tab(I18n.translate("packs.new")) {
                setOnSelectionChanged {
                    currTab.select()
                    val pack = PackDialog.openPackDialog(parent = mainInner)
                    if(pack != null) {
                        MinecraftModpacks.addPack(pack)
                        MinecraftModpacks.savePacks()
                        addNewPackPane(pack)
                    }
                }
            }
        }
    }
    override val root = main

    private fun addNewPackPane(pack: MinecraftModpacks.Modpack) {
        this.installedPacksPane.children.add(0, PackPane(pack, this.main))
    }
}