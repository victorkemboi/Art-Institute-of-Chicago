package android

import android.app.Application
import mes.inc.aic.common.data.di.initKoin

class ArtSpaceApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}