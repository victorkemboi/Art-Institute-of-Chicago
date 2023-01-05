package mes.inc.aic.common.data.cache

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import mes.inc.aic.database.ArtSpaceDatabase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class DatabaseDriverFactory : KoinComponent {
    private val context: Context by inject()
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(ArtSpaceDatabase.Schema, context, "art-space.db")
    }
}