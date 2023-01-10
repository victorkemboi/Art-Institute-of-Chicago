package mes.inc.aic.common.data.service.aic

import co.touchlab.kermit.Logger
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import mes.inc.aic.common.data.cache.ArtworkDao

class ArtInstituteOfChicagoService(
    private val client: HttpClient,
    private val artworkDao: ArtworkDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun fetchArtworks(query: String? = null, page: Int = 1) {
        val urlString = "https://api.artic.edu/api/v1/" + if (query != null) {
            Logger.i("Query: $query")
            "artworks/search?q=$query&limit=10&page=$page"
        } else {
            "artworks?limit=10&page=$page"
        }
        CoroutineScope(dispatcher).launch {
            kotlin.runCatching {
                client.get(urlString)
            }.onFailure { error ->
                Logger.i("Error from response: $error")
            }.onSuccess { response ->
                when (response.status) {
                    HttpStatusCode.OK -> {
                        val data = response.body<BaseResponse<List<ArtworkResponse>>>()
                        data.data.forEach { artworkResponse ->
                            if (query != null) {
                                artworkResponse.id.toIntOrNull()?.let {
                                    fetchArtworkById(id = it)
                                }
                            } else {
                                artworkResponse.imageId?.let {
                                    val artwork = artworkResponse.toArtwork()
                                    artworkDao.insert(artwork)
                                }
                            }
                        }
                        if (data.pagination.totalPages != page && page < 20) {
                            fetchArtworks(query = query, page = page + 1)
                        }
                    }

                    else -> {
                        Logger.i("Unexpected response: $response")
                    }
                }
            }
        }
    }

    fun fetchArtworkById(id: Int) {
        val url = "https://api.artic.edu/api/v1/artworks/$id"
        CoroutineScope(dispatcher).launch {
            kotlin.runCatching {
                client.get(url)
            }.onFailure { error ->
                Logger.i("Error from response: $error")
            }.onSuccess { response ->
                when (response.status) {
                    HttpStatusCode.OK -> {
                        val artworkResponse = response.body<BaseResponse<ArtworkResponse>>().data
                        artworkResponse.imageId?.let {
                            val artwork = artworkResponse.toArtwork()
                            artworkDao.insert(artwork)
                        }
                    }

                    else -> {
                        Logger.i("Unexpected response: $response")
                    }
                }
            }
        }
    }
}