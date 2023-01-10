package android

import android.app.Application
import mes.inc.aic.common.data.di.initKoin
import org.koin.android.ext.koin.androidContext

class ArtSpaceApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(enableNetworkLogs = true) {
            androidContext(this@ArtSpaceApp.applicationContext)
        }
    }
}