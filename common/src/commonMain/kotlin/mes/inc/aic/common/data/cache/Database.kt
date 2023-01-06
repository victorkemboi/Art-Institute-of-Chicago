package mes.inc.aic.common.data.cache

import mes.inc.aic.database.ArtSpaceDatabase

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val artSpaceDatabase = ArtSpaceDatabase(databaseDriverFactory.createDriver())

    internal fun clearDatabase() {
        artSpaceDatabase.transaction {
            artSpaceDatabase.artworkQueries.removeAllArtworks()
        }
    }
}

internal class ArtworkDao(database: ArtSpaceDatabase)