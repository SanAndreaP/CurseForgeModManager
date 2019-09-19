package de.sanandrew.apps.cursemodmgr.util

data class SemVer(val major: Int, val api: Int, val minor: Int, val revision: Int, val build: Int) : Comparable<SemVer> {
    companion object {
        fun getMcVer(gameVer: String): SemVer {
            val nbr = gameVer.split('.')
            if(nbr.size > 1) {
                if(nbr.firstOrNull { e -> e.toIntOrNull() == null } == null) {
                    return SemVer(nbr[0].toInt(), 0, nbr[1].toInt(), if(nbr.size > 2) nbr[2].toIntOrNull() ?: 0 else 0, 0)
                } else {
                    throw IllegalArgumentException(String.format("Cannot parse version %s to Minecraft version. One variable is not a number.", gameVer))
                }
            } else {
                throw IllegalArgumentException(String.format("Cannot parse version %s to Minecraft version. There must be at least 2 variables.", gameVer))
            }
        }
    }

    override fun compareTo(other: SemVer): Int {
        var cmp = this.major.compareTo(other.major)
        if(cmp != 0) {
            return cmp
        }

        cmp = this.api.compareTo(other.api)
        if(cmp != 0) {
            return cmp
        }

        cmp = this.minor.compareTo(other.minor)
        if(cmp != 0) {
            return cmp
        }

        cmp = this.revision.compareTo(other.revision)
        if(cmp != 0) {
            return cmp
        }

        return this.build.compareTo(other.build)
    }

    fun getMcVerString(): String {
        if(this.revision > 0) {
            return String.format("%d.%d.%d", this.major, this.minor, this.revision)
        } else {
            return String.format("%d.%d", this.major, this.minor)
        }
    }
}