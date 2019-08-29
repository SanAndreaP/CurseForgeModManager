package de.sanandrew.apps.cursemodmgr.curseapi

data class Game(val id: Long, val name: String, val slug: String, val categorySections: Array<CategorySection>) {
    data class CategorySection(val id: Long, val name: String, val packageType: Int, val path: String, val initialInclusionPattern: String,
                               val extraIncludePattern: String, val gameCategoryId: Long)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Game

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}