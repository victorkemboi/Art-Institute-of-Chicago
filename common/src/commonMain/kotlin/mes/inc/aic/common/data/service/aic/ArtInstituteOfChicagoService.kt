package mes.inc.aic.common.data.service.aic

import co.touchlab.kermit.Logger
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import mes.inc.aic.common.data.cache.ArtworkDao
import mes.inc.aic.common.data.model.Artwork

class ArtInstituteOfChicagoService(
    private val client: HttpClient,
    private val artworkDao: ArtworkDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
//    init {
//        client.config {
//            defaultRequest {
//                host = "https://api.artic.edu/api/v1/"
//                url { protocol = URLProtocol.HTTPS }
//            }
//        }
//    }

    suspend fun fetchArtworks(query: String? = null, page: Int = 1) {
        with(dispatcher) {
            val urlString = "https://api.artic.edu/api/v1/" + if (query != null) {
                "artworks/search?q=$query&limit=100&page=$page"
            } else {
                "artworks?limit=100&page=$page"
            }
            Logger.i("Fetch artworks: url -> $urlString")
            val response = client.get(urlString)
            Logger.i("Response: $response")
            when (response.status) {
                HttpStatusCode.OK -> {
                    val data = response.body<BaseResponse<List<Artwork>>>()
                    Logger.i("Fetch artworks: data -> $data")
                    artworkDao.insert(data.data)
                    if (data.pagination.totalPages != page) {
                        Logger.i("Fetch artworks: current page $page,  -> fetching next")
                        fetchArtworks(query = query, page = page + 1)
                    }
                }

                else -> {
                    Logger.i("Fetch artworks failed.")
                    return@with
                }
            }
        }
        client.close()
    }
}