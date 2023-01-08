package mes.inc.aic.common.data.model

import kotlinx.serialization.Serializable
import mes.inc.aic.common.extensions.generateRandomNumber
import mes.inc.aic.common.extensions.generateUuid

@Serializable
data class Artwork(
    val localId: Long = 0,
    val serverId: String = generateUuid(),
    val title: String = "Artwork ${generateRandomNumber()}",
    val thumbnail: String? = null,
    val dateDisplay: String? = "Dark Ages",
    val artistId: String? = null,
    val categoryTitles: List<String> = listOf("Painting"),
    val styleTitle: String? = null,
    val updatedAt: String? = null,
    val origin: String? = "Art institute of Chicago",
    val searchString: String? = "$title $categoryTitles"
) {
    fun toReel() = Reel(
        title = title,
        thumbnail = thumbnail,
        type = Reel.ReelType.Artwork(this)
    )

    companion object {
        val mapper: (
            Long,
            String,
            String,
            String?,
            String?,
            String?,
            String,
            String?,
            String?,
            String?,
            String?,
        ) -> Artwork =
            { localId, serverId, title, thumbnail, dateDisplay, artistId,
              categoryTitles, styleTitle, updatedAt, origin, searchString ->
                Artwork(
                    localId = localId,
                    serverId = serverId,
                    title = title,
                    thumbnail = thumbnail,
                    dateDisplay = dateDisplay,
                    artistId = artistId,
                    categoryTitles = categoryTitles.replace(" ", "").split(",").map { it.trim() },
                    styleTitle = styleTitle,
                    updatedAt = updatedAt,
                    origin = origin,
                    searchString = searchString
                )
            }
    }
}

data class Reel(
    val title: String = "Top Reel ${generateRandomNumber()}",
    val description: String = "Explore our top selection item 🚀",
    val thumbnail: String? = null,
    val type: ReelType = ReelType.Artwork(Artwork(title = title, thumbnail = thumbnail))
) {
    sealed class ReelType {
        data class Artwork(val artwork: mes.inc.aic.common.data.model.Artwork) : ReelType()
    }
}
