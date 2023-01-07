package mes.inc.aic.common.data.cache

import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import mes.inc.aic.common.data.model.Artwork
import mes.inc.aic.common.database.ArtworkQueries

@OptIn(ExperimentalCoroutinesApi::class)
class ArtworkDaoTest {
    private val artworkQueries: ArtworkQueries = mockk(relaxed = true)
    private val artworkDao: ArtworkDao = ArtworkDao(artworkQueries)
    private val artworkCategories = listOf("Painting", "Sculpture", "Architecture")

    @Test
    fun insertingArtworkIsCalled() {
        val localId = 4L
        val title = "Art 101"
        val artwork = Artwork(
            localId = localId,
            title = title,
            categoryTitles = artworkCategories
        )
        artworkDao.insert(artwork)
        verify(exactly = 1) {
            artworkQueries.insertArtwork(
                localId = null,
                serverId = artwork.serverId,
                title = artwork.title,
                thumbnail = artwork.thumbnail,
                dateDisplay = artwork.dateDisplay,
                artistId = artwork.artistId,
                categoryTitles = "Painting,Sculpture,Architecture",
                styleTitle = artwork.styleTitle,
                updatedAt = artwork.updatedAt,
                origin = artwork.origin,
                searchString = artwork.searchString
            )
        }
    }

    @Test
    fun fetchingArtworksWithoutQueryIsCalled() = runTest {
        artworkDao.fetchArtworks(null).first()
        verify(exactly = 1) { artworkQueries.selectAll(mapper = Artwork.mapper) }
    }

    @Test
    fun fetchingArtworksWithQueryIsCalled() = runTest {
        val query = "Query"
        artworkDao.fetchArtworks(query).first()
        verify(exactly = 1) { artworkQueries.search(query = "%$query%", mapper = Artwork.mapper) }
    }
}