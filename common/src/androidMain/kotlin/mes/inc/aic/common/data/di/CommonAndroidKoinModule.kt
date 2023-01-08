package mes.inc.aic.common.data.di

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.observer.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import mes.inc.aic.common.data.cache.DatabaseDriverFactory
import mes.inc.aic.database.ArtSpaceDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun commonModule(): Module = module {
    single { ArtSpaceDatabase(DatabaseDriverFactory().createDriver()) }
}

actual val httpClient: HttpClient
    get() = HttpClient(OkHttp) {
        engine { config { followRedirects(true) } }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        install(ResponseObserver) {
            onResponse {
                co.touchlab.kermit.Logger.i("Response observer: $it")
            }
        }
    }