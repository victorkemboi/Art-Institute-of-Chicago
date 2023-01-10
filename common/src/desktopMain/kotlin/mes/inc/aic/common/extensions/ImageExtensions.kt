package mes.inc.aic.common.extensions

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import co.touchlab.kermit.Logger
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.net.HttpURLConnection
import java.net.URL
import javax.imageio.ImageIO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.jetbrains.skia.Image

actual suspend fun loadNetworkImage(link: String, dispatcher: CoroutineDispatcher): ImageBitmap? =
    withContext(dispatcher) {
        try {
            val url = URL(link)
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            val inputStream = connection.inputStream
            val bufferedImage = ImageIO.read(inputStream)

            val stream = ByteArrayOutputStream()
            ImageIO.write(bufferedImage, "png", stream)
            val byteArray = stream.toByteArray()

            return@withContext Image.makeFromEncoded(byteArray).toComposeImageBitmap()
        } catch (e: FileNotFoundException) {
            Logger.i("Error loading image: $e")
            null
        }
    }