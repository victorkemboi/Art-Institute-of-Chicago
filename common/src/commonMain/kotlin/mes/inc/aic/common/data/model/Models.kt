package mes.inc.aic.common.data.model

import mes.inc.aic.common.extensions.generateRandomNumber
import mes.inc.aic.common.extensions.generateUuid

data class Artwork(
    val localId: Int = 0,
    val serverId: String = generateUuid(),
    val title: String = "Artwork ${generateRandomNumber()}",
    val thumbnail: String? = null,
    val dateDisplay: String? = "Dark Ages",
    val artistId: String? = null,
    val categoryTitles: List<String> = listOf("Painting"),
    val styleTitle: String? = null,
    val updatedAt: String? = null,
    val origin: String? = "Art institute of Chicago",
) {
    fun toReel() = Reel(
        title = title,
        thumbnail = thumbnail,
        type = Reel.ReelType.Artwork(this)
    )
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
