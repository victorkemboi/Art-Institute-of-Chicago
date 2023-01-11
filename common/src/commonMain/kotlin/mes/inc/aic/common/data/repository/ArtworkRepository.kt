package mes.inc.aic.common.data.repository

import kotlinx.coroutines.flow.*
import mes.inc.aic.common.data.cache.ArtworkDao
import mes.inc.aic.common.data.model.Artwork
import mes.inc.aic.common.data.model.Reel
import mes.inc.aic.common.data.service.aic.ArtInstituteOfChicagoService

interface ArtworkRepository {
    fun syncArtworks(query: String? = null)
    fun fetchArtworks(query: String? = null): Flow<List<Artwork>>
    suspend fun fetchArtworkReels(): Reel?
}

class ArtworkRepositoryImpl(
    private val artworkDao: ArtworkDao,
    private val artInstituteOfChicagoService: ArtInstituteOfChicagoService,
) : ArtworkRepository {
    override fun syncArtworks(query: String?) {
        artInstituteOfChicagoService.fetchArtworks(query = query)
    }

    override fun fetchArtworks(query: String?): Flow<List<Artwork>> {
        return artworkDao.fetchArtworks(query)
    }

    override suspend fun fetchArtworkReels(): Reel? {
        return artworkDao.fetchArtworks()
            .map {
                if (it.isNotEmpty()) {
                    val random = it.indices.random()
                    it[random].toReel()
                } else {
                    null
                }
            }
            .firstOrNull()
    }
}