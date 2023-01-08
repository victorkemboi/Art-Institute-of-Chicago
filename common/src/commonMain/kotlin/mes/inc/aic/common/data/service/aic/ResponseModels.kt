package mes.inc.aic.common.data.service.aic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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