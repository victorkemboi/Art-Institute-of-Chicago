package mes.inc.aic.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import mes.inc.aic.common.data.model.Artwork
import mes.inc.aic.common.data.model.Reel
import mes.inc.aic.common.extensions.loadNetworkImage
import mes.inc.aic.common.constants.NETWORK_IMAGE
import mes.inc.aic.common.constants.NETWORK_IMAGE_LOADER

@Composable
fun ReelComponent(reel: Reel, modifier: Modifier = Modifier) {
    Box(modifier = modifier.background(Primary).heightIn(min = 300.dp)) {
        reel.thumbnail?.let { NetworkImage(it, description = reel.title) }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = Padding.Medium, bottom = Padding.Medium)
        ) {
            Text(text = reel.title, style = MaterialTheme.typography.h4)
            Text(text = reel.description, style = MaterialTheme.typography.subtitle1)
        }
    }
}

@Composable
fun ArtworkComponent(
    artwork: Artwork,
    modifier: Modifier = Modifier,
    thumbnailModifier: Modifier = Modifier.width(150.dp).height(150.dp)
) {
    Column(modifier = modifier) {
        Box(modifier = thumbnailModifier.background(Primary)) {
            artwork.thumbnail?.let { NetworkImage(it, description = artwork.title) }
        }
        Box(modifier = Modifier.padding(top = Padding.Small)) {
            Text(text = artwork.title, modifier = Modifier.align(Alignment.TopStart))
            artwork.dateDisplay?.let {
                Text(text = it, modifier = Modifier.align(Alignment.TopEnd))
            }
        }
    }
}

@Composable
fun NetworkImage(
    link: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    contentScale: ContentScale = ContentScale.FillBounds
) {
    var image by remember { mutableStateOf<ImageBitmap?>(null) }
    LaunchedEffect(link) {
        image = loadNetworkImage(link)
    }
    if (image != null) {
        image?.let {
            Image(
                bitmap = it,
                contentDescription = description,
                modifier = modifier.testTag(NETWORK_IMAGE),
                contentScale = contentScale
            )
        }
    } else {
        CircularProgressIndicator(progress = 1f, modifier = Modifier.testTag(NETWORK_IMAGE_LOADER))
    }
}