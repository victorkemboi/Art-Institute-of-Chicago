package mes.inc.aic.common.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import mes.inc.aic.common.constants.NETWORK_IMAGE
import mes.inc.aic.common.constants.NETWORK_IMAGE_LOADER
import mes.inc.aic.common.data.model.Artwork
import mes.inc.aic.common.data.model.Reel
import mes.inc.aic.common.extensions.loadNetworkImage

@Composable
fun ReelComponent(reel: Reel, modifier: Modifier = Modifier) {
    Box(modifier = modifier.background(Primary).heightIn(min = 300.dp).height(IntrinsicSize.Max)) {
        reel.thumbnail?.let {
            NetworkImage(
                link = it,
                description = reel.title,
                modifier = Modifier.fillMaxWidth().fillMaxHeight()
            )
        }
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
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = thumbnailModifier.background(Primary).height(IntrinsicSize.Max)) {
            artwork.thumbnail?.let {
                NetworkImage(
                    link = it,
                    description = artwork.title,
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
        Column(modifier = Modifier.padding(top = Padding.Small)) {
            Text(text = artwork.title)
            artwork.dateDisplay?.let {
                Text(text = it)
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

@Composable
fun Search(query: String = "", onQueryChanged: (String) -> Unit, modifier: Modifier = Modifier) {
    val focusManager = LocalFocusManager.current
    Card(
        elevation = 4.dp,
        modifier = modifier.padding(all = 10.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.onPrimary)
    ) {
        TextField(
            query, onValueChange = onQueryChanged,
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            placeholder = { Text("Search...") },
            singleLine = true,
            trailingIcon = {
                if (query.isNotEmpty()) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "close",
                        modifier = Modifier.size(ButtonDefaults.IconSize).clickable {
                            onQueryChanged("")
                            focusManager.clearFocus()
                        }
                    )
                }
            }
        )
    }
}