package mes.inc.aic.common.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import mes.inc.aic.common.data.model.Artwork
import mes.inc.aic.common.data.model.Reel
// import org.koin.core.annotation.Single

interface ArtworkRepository {
    suspend fun syncArtworks()
    fun fetchArtworks(query: String? = null): Flow<List<Artwork>>
    fun fetchArtworkReels(): Flow<List<Reel>>
}

// @Single
class ArtworkRepositoryImpl : ArtworkRepository {
    override suspend fun syncArtworks() {
        TODO("Not yet implemented")
    }

    override fun fetchArtworks(query: String?): Flow<List<Artwork>> = flowOf(
        listOf(Artwork())
    )

    override fun fetchArtworkReels(): Flow<List<Reel>> = fetchArtworks().map { artworks ->
        artworks.map { it.toReel() }
    }
}