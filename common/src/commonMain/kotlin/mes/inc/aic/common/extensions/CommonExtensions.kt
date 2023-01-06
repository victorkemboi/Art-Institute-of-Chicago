package mes.inc.aic.common.extensions

import java.util.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

inline fun <reified T> getKoinInstance(): T {
    return object : KoinComponent {
        val value: T by inject()
    }.value
}

fun generateUuid(): String = UUID.randomUUID().toString()

fun generateRandomNumber(min: Int = 0, max: Int = 1000): Int = (min..max).random()