package mes.inc.aic.common.data.service.aic

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import mes.inc.aic.common.data.cache.ArtworkDao
import mes.inc.aic.common.data.cache.FakeArtworkDao
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ArtInstituteOfChicagoServiceTest {
    private val dispatcher = StandardTestDispatcher()
    private val artworkDao: ArtworkDao = FakeArtworkDao()

    @BeforeTest
    fun setup() {
        Logger.setLogWriters(CommonWriter())
    }

    @Test
    fun onSuccessfulFetchingSaveArtworks() = runTest {
        val service = ArtInstituteOfChicagoService(
            client = HttpClient(MockEngine {
                respond(
                    content = ByteReadChannel(
                        """
                        {
                            "pagination": {
                                "total": 118760,
                                "limit": 2,
                                "offset": 0,
                                "total_pages": 59380,
                                "current_page": 1,
                                "next_url": "https://api.artic.edu/api/v1/artworks?page=2&limit=2"
                            },
                            "data": [
                                {
                                    "id": 7600,
                                    "api_model": "artworks",
                                    "api_link": "https://api.artic.edu/api/v1/artworks/76771",
                                    "is_boosted": false,
                                    "title": "Pan",
                                    "alt_titles": null,
                                    "image_id": "76771",
                                    "date_display": "Dark Ages",
                                    "artist_id": 1000,
                                    "category_titles":  ["Painting"],
                                    "style_title": null,
                                    "updated_at": null,
                                }
                            ],
                            "info": {
                                "license_text": "The data in this response is licensed under a Creative Commons Zero (CC0) 1.0 designation and the Terms and Conditions of artic.edu.",
                                "license_links": [
                                    "https://creativecommons.org/publicdomain/zero/1.0/",
                                    "https://www.artic.edu/terms"
                                ],
                                "version": "1.6"
                            },
                            "config": {
                                "iiif_url": "https://www.artic.edu/iiif/2",
                                "website_url": "https://www.artic.edu"
                            }
                        }
                    """.trimIndent()
                    ),
                    status = HttpStatusCode.OK
                )
            }),
            artworkDao = artworkDao,
            dispatcher = dispatcher
        )
        service.fetchArtworks()
        advanceUntilIdle()
        val noOfArtworks = artworkDao.fetchArtworks().first().size
        assertEquals(1, noOfArtworks)
    }

}