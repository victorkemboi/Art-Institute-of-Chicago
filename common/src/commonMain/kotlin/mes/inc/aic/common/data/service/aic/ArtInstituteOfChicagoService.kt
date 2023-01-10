package mes.inc.aic.common.data.service.aic

import co.touchlab.kermit.Logger
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import mes.inc.aic.common.data.cache.ArtworkDao
import mes.inc.aic.common.data.model.Artwork

class ArtInstituteOfChicagoService(
    private val client: HttpClient,
    private val artworkDao: ArtworkDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun fetchArtworks(query: String? = null, page: Int = 1) {
        val urlString = "https://api.artic.edu/api/v1/" + if (query != null) {
            "artworks/search?q=$query&limit=10&page=$page"
        } else {
            "artworks?limit=10&page=$page"
        }
        Logger.i("Fetching artworks")
        CoroutineScope(dispatcher).launch {
            kotlin.runCatching {
                client.get(urlString)
            }.onFailure { error ->
                Logger.i("Error from response: $error")
            }.onSuccess { response ->
                Logger.i("Response: $response")
                when (response.status) {
                    HttpStatusCode.OK -> {
                        val data = response.body<BaseResponse<List<ArtworkResponse>>>()
                        artworkDao.insert(data.data.mapNotNull { artworkResponse ->
                            Logger.i("Artwork: $artworkResponse")
                            artworkResponse.imageId?.let {
                                artworkResponse.toArtwork()
                            }
                        })
                        if (data.pagination.totalPages != page && page < 6) {
                            fetchArtworks(query = query, page = page + 1)
                        }
                    }

                    else -> {
                        Logger.i("Unexpected response: $response")
                    }
                }
            }
//            client.close()
        }
    }
}