package mes.inc.aic.common.data.di

import mes.inc.aic.common.data.cache.ArtworkAao
import mes.inc.aic.common.data.repository.ArtworkRepository
import mes.inc.aic.common.data.repository.ArtworkRepositoryImpl
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun repositoryModule() = module {
    single<ArtworkRepository> { ArtworkRepositoryImpl() }
    single { ArtworkAao(get()) }
}

fun daoModule() = module {
    single { ArtworkAao(get()) }
}

expect fun platformModule(): Module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(repositoryModule(), daoModule(), platformModule())
    }