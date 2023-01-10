package mes.inc.aic.common.data.cache

import app.cash.turbine.test
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import mes.inc.aic.common.data.model.Artwork

class FakeArtworkDao : ArtworkDao {
    private val artworkState = MutableStateFlow((listOf<Artwork>()))
    override fun insert(artwork: Artwork) {
        artworkState.update { it.toMutableList().apply { add(artwork) } }
    }

    override fun insert(artworks: List<Artwork>) {
        artworks.forEach { insert(it) }
    }

    override fun fetchArtworks(query: String?): Flow<List<Artwork>> =
        if (query == null) {
            artworkState
        } else {
            artworkState.map { it.filter { artwork -> artwork.searchString?.contains(query) == true } }
        }

    override suspend fun recordExists(title: String): Boolean =
        artworkState.value.filter { it.title == title }.isNotEmpty()

    override fun removeAllArtworks() {
        artworkState.update { emptyList() }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class ArtworkDaoTest {
    private val artworkDao: ArtworkDao = FakeArtworkDao()
    private val artworkCategories = listOf("Painting", "Sculpture", "Architecture")

    @Test
    fun artworkInserted() = runTest {
        val localId = 4L
        val title = "Art 101"
        val artwork = Artwork(
            localId = localId,
            title = title,
            categoryTitles = artworkCategories
        )
        artworkDao.insert(artwork)
        assertEquals(1, artworkDao.fetchArtworks().first().size)
    }

    @Test
    fun artworkUpdatesAreEmitted() = runTest {
        artworkDao.fetchArtworks(null).test {
            assertEquals(0, awaitItem().size)
            artworkDao.insert(Artwork())
            assertEquals(1, awaitItem().size)
            awaitComplete()
        }
    }

    @Test
    fun fetchingArtworksWithQuery() = runTest {
        val query = "Query"
        artworkDao.insert(listOf(Artwork(title = query), Artwork(), Artwork()))
        val artworksSize = artworkDao.fetchArtworks(query).first().size
        assertEquals(1, artworksSize)
    }
}