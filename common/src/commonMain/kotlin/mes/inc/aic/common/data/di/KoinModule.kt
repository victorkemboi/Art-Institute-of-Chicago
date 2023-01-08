package mes.inc.aic.common.data.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.*
import io.ktor.network.tls.CIOCipherSuites
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import mes.inc.aic.common.data.cache.ArtworkDao
import mes.inc.aic.common.data.repository.ArtworkRepository
import mes.inc.aic.common.data.repository.ArtworkRepositoryImpl
import mes.inc.aic.common.data.service.aic.ArtInstituteOfChicagoService
import mes.inc.aic.database.ArtSpaceDatabase
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun repositoryModule() = module {
    single<ArtworkRepository> { ArtworkRepositoryImpl(get(), get()) }
}

fun serviceModule() = module {
    single { ArtInstituteOfChicagoService(get(), get()) }
}

fun databaseModule() = module {
    single { ArtworkDao(get<ArtSpaceDatabase>().artworkQueries) }
}

fun networkModule(enableNetworkLogs: Boolean = false) = module {
    val client = HttpClient(CIO) {
        engine {
            maxConnectionsCount = 1000
            endpoint {
                maxConnectionsPerRoute = 100
                pipelineMaxSize = 20
                keepAliveTime = 5000
                connectTimeout = 5000
                connectAttempts = 5
            }
            https { cipherSuites = CIOCipherSuites.SupportedSuites }
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        if (enableNetworkLogs) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }

        install(ResponseObserver) {
            onResponse {
                co.touchlab.kermit.Logger.i("Response observer: $it")
            }
        }
    }
    single { httpClient }
}

expect val httpClient: HttpClient

expect fun commonModule(): Module

fun initKoin(enableNetworkLogs: Boolean = false, appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(
        repositoryModule(),
        databaseModule(),
        commonModule(),
        networkModule(enableNetworkLogs = enableNetworkLogs),
        serviceModule()
    )
}