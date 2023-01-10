package mes.inc.aic.common.data.service.aic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mes.inc.aic.common.data.model.Artwork
import mes.inc.aic.common.extensions.generateRandomNumber
import mes.inc.aic.common.extensions.generateUuid

sealed class NetworkResponse<out R> {
    data class Success<out T>(val data: T) : NetworkResponse<T>()

    data class ServerError(
        val code: Int? = null,
        val errorBody: Any? = null
    ) : NetworkResponse<Nothing>()

    object NetworkError : NetworkResponse<Nothing>()
}

@Serializable
data class BaseResponse<T>(
    val pagination: Pagination = Pagination(),
    val data: T,
    val info: Info = Info(),
    val config: Config = Config(),
)

@Serializable
data class Pagination(
    val total: Int = 0,
    val limit: Int = 0,
    val offset: Int = 0,
    @SerialName("total_pages")
    val totalPages: Int = 1,
    @SerialName("current_page")
    val currentPage: Int = 1,
    @SerialName("next_url")
    val nextUrl: String? = null,
)

@Serializable
data class Info(
    val licenseText: String = "",
    val licenseLinks: String = "",
    val version: String = ""
)

@Serializable
data class Config(
    @SerialName("iiif_url")
    val iiifUrl: String = "",
    val websiteUrl: String = ""
)

@Serializable
data class ArtworkResponse(
    val id: String = generateUuid(),
    val title: String = "Artwork ${generateRandomNumber()}",
    @SerialName("image_id")
    val imageId: String? = "",
    @SerialName("date_display")
    val dateDisplay: String? = "Unknown",
    @SerialName("artist_id")
    val artistId: Int? = null,
    @SerialName("category_titles")
    val categoryTitles: List<String> = listOf("Unknown"),
    @SerialName("style_title")
    val styleTitle: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null,
    val origin: String? = "Art institute of Chicago",
) {
    fun toArtwork() = Artwork(
        serverId = this.id,
        title = this.title,
        thumbnail = "https://www.artic.edu/iiif/2/${this.imageId}/full/843,/0/default.jpg",
        dateDisplay = dateDisplay,
        artistId = artistId.toString(),
        categoryTitles = categoryTitles,
        styleTitle = styleTitle,
        updatedAt = updatedAt,
        origin = origin
    )
}