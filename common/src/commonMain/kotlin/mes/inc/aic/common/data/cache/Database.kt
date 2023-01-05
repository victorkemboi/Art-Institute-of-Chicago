package mes.inc.aic.common.data.cache

import mes.inc.aic.database.ArtSpaceDatabase

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = ArtSpaceDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.artworkQueries

    internal fun clearDatabase() {
        database.transaction {
            database.artworkQueries.removeAllArtworks()
        }
    }
}

internal class ArtworkDao(database: ArtSpaceDatabase)