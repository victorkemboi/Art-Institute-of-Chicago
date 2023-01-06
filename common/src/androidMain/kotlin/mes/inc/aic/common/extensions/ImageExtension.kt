package mes.inc.aic.common.extensions

import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

actual suspend fun loadNetworkImage(link: String, dispatcher: CoroutineDispatcher): ImageBitmap =
    withContext(dispatcher) {
        val context: Context = getKoinInstance()
        return@withContext Glide.with(context).asBitmap().load(link).submit().get().asImageBitmap()
    }