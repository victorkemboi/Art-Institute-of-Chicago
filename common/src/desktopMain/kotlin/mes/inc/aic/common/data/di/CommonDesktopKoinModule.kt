package mes.inc.aic.common.data.di

import io.ktor.client.*
import mes.inc.aic.common.data.cache.DatabaseDriverFactory
import mes.inc.aic.database.ArtSpaceDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun commonModule(): Module = module {
    single { ArtSpaceDatabase(DatabaseDriverFactory().createDriver()) }
}

actual val httpClient: HttpClient
    get() = TODO("Not yet implemented")