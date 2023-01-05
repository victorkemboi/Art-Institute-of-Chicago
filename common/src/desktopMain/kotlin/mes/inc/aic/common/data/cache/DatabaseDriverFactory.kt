package mes.inc.aic.common.data.cache

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import mes.inc.aic.database.ArtSpaceDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        ArtSpaceDatabase.Schema.create(driver)
        return driver
    }
}