package mes.inc.aic.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mes.inc.aic.common.data.model.Artwork
import mes.inc.aic.common.data.model.Reel

@Composable
fun Reel(reel: Reel, modifier: Modifier = Modifier) {
    Box(modifier = modifier.background(Primary).heightIn(min = 300.dp)) {
        reel.thumbnail?.let {
//            Image(painter = painterResource(it), contentDescription = reel.title)
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
    thumbnail: Modifier = Modifier.width(100.dp).height(100.dp)
) {
    Column(modifier = modifier) {
        Box(modifier = thumbnail.background(Primary)) {
            artwork.thumbnail?.let {
//                Image(painter = painterResource(it), contentDescription = artwork.title)
            }
        }
        Box(modifier = Modifier.padding(top = Padding.Small)) {
            Text(text = artwork.title, modifier = Modifier.align(Alignment.TopStart))
            artwork.dateDisplay?.let {
                Text(text = it, modifier = Modifier.align(Alignment.TopEnd))
            }
        }
    }
}