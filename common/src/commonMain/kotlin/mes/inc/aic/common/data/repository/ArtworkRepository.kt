package mes.inc.aic.common.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import mes.inc.aic.common.data.model.Artwork
import mes.inc.aic.common.data.model.Reel

interface ArtworkRepository {
    suspend fun syncArtworks()
    fun fetchArtworks(query: String? = null): Flow<List<Artwork>>
    fun fetchArtworkReels(): Flow<List<Reel>>
}

class ArtworkRepositoryImpl : ArtworkRepository {
    override suspend fun syncArtworks() {
        TODO("Not yet implemented")
    }

    override fun fetchArtworks(query: String?): Flow<List<Artwork>> = flowOf(emptyList())

    override fun fetchArtworkReels(): Flow<List<Reel>> = flowOf(emptyList())
}