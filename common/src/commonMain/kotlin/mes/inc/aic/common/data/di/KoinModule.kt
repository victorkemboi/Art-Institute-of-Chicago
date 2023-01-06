package mes.inc.aic.common.data.di

import mes.inc.aic.common.data.cache.Database
import mes.inc.aic.common.data.repository.ArtworkRepository
import mes.inc.aic.common.data.repository.ArtworkRepositoryImpl
import mes.inc.aic.common.ui.HomeScreenStateHolder
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

// import org.koin.core.annotation.ComponentScan
// import org.koin.core.annotation.Module
//
// @Module
// @ComponentScan
// class CommonAppModule
fun commonModule() = module {
    single<ArtworkRepository> { ArtworkRepositoryImpl() }
    single { HomeScreenStateHolder() }
    single { Database(get()) }
}

expect fun platformModule(): Module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(commonModule(), platformModule())
    }