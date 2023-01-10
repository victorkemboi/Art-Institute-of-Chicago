package mes.inc.aic.common.data.cache

import co.touchlab.kermit.Logger
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import mes.inc.aic.common.data.model.Artwork
import mes.inc.aic.common.database.ArtworkQueries

interface ArtworkDao {
    fun insert(artwork: Artwork)
    fun insert(artworks: List<Artwork>)

    fun fetchArtworks(query: String? = null): Flow<List<Artwork>>
    fun removeAllArtworks()
}

class ArtworkDaoImpl(private val artworkQueries: ArtworkQueries) : ArtworkDao {

    override fun insert(artwork: Artwork) {
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

    override fun insert(artworks: List<Artwork>) {
        artworks.forEach {
            Logger.i("Response: saving artwork $it")
            insert(it)
        }
    }

    override fun fetchArtworks(query: String?): Flow<List<Artwork>> {
        return if (query != null) {
            artworkQueries.search(query = "%$query%", mapper = Artwork.mapper)
        } else {
            artworkQueries.selectAll(mapper = Artwork.mapper)
        }.asFlow().mapToList()
    }

    override fun removeAllArtworks() {
        artworkQueries.transaction {
            artworkQueries.removeAllArtworks()
        }
    }
}

internal class ArtistDao