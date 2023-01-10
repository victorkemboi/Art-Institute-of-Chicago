package mes.inc.aic.common.extensions

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

expect suspend fun loadNetworkImage(link: String, dispatcher: CoroutineDispatcher = Dispatchers.IO): ImageBitmap?