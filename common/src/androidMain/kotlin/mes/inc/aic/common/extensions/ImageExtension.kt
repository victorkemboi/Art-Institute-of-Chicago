package mes.inc.aic.common.extensions

import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import co.touchlab.kermit.Logger
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@Suppress("TooGenericExceptionCaught")
actual suspend fun loadNetworkImage(link: String, dispatcher: CoroutineDispatcher): ImageBitmap? =
    withContext(dispatcher) {
        val context: Context = getKoinInstance()
        return@withContext try {
            Glide.with(context).asBitmap().load(link).submit().get().asImageBitmap()
        } catch (e: Exception) {
            Logger.i("Error loading image: $e")
            null
        }
    }