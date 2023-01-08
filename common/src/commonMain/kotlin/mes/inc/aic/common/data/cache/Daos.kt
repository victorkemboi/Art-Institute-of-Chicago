package mes.inc.aic.common.data.cache

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import mes.inc.aic.common.data.model.Artwork
import mes.inc.aic.common.database.ArtworkQueries

class ArtworkDao(private val artworkQueries: ArtworkQueries) {

    fun insert(artwork: Artwork) {
        artworkQueries.insertArtwork(
            localId = null,
            serverId = artwork.serverId,
            title = artwork.title,
            thumbnail = artwork.thumbnail,
            dateDisplay = artwork.dateDisplay,
            artistId = artwork.artistId,
            categoryTitles = artwork.categoryTitles.joinToString(separator = ","),
            styleTitle = artwork.styleTitle,
            updatedAt = artwork.updatedAt,
            origin = artwork.origin,
            searchString = artwork.searchString
        )
    }

    fun insert(artworks: List<Artwork>) {
        artworks.forEach {
            insert(it)
        }
    }

    internal fun fetchArtworks(query: String? = null): Flow<List<Artwork>> {
        return if (query != null) {
            artworkQueries.search(query = "%$query%", mapper = Artwork.mapper)
        } else {
            artworkQueries.selectAll(mapper = Artwork.mapper)
        }.asFlow().mapToList()
    }

    internal fun removeAllArtworks() {
        artworkQueries.transaction {
            artworkQueries.removeAllArtworks()
        }
    }
}

internal class ArtistDao