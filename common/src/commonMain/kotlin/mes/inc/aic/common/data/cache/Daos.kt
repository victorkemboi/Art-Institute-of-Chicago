package mes.inc.aic.common.data.cache

import mes.inc.aic.database.ArtSpaceDatabase

internal class ArtworkAao(databaseDriverFactory: DatabaseDriverFactory) {
    private val artSpaceDatabase = ArtSpaceDatabase(databaseDriverFactory.createDriver())

    internal fun removeAllArtworks() {
        artSpaceDatabase.transaction {
            artSpaceDatabase.artworkQueries.removeAllArtworks()
        }
    }
}

internal class ArtistDao