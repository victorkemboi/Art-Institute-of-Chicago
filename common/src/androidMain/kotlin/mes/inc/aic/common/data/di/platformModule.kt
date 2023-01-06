package mes.inc.aic.common.data.di

import mes.inc.aic.common.data.cache.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single { DatabaseDriverFactory() }
}