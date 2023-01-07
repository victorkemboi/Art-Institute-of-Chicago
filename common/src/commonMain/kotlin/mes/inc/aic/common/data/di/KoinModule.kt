package mes.inc.aic.common.data.di

import mes.inc.aic.common.data.cache.ArtworkDao
import mes.inc.aic.common.data.repository.ArtworkRepository
import mes.inc.aic.common.data.repository.ArtworkRepositoryImpl
import mes.inc.aic.database.ArtSpaceDatabase
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun repositoryModule() = module {
    single<ArtworkRepository> { ArtworkRepositoryImpl() }
}

fun databaseModule() = module {
    single { ArtworkDao(get<ArtSpaceDatabase>().artworkQueries) }
}

expect fun commonModule(): Module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(repositoryModule(), databaseModule(), commonModule())
    }