package mes.inc.aic.common.ui

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.time.Duration
import kotlinx.coroutines.delay
import mes.inc.aic.common.constants.HOMES_SCREEN_ARTWORKS
import mes.inc.aic.common.constants.HOMES_SCREEN_REEL
import mes.inc.aic.common.data.model.Artwork
import mes.inc.aic.common.data.model.Reel
import mes.inc.aic.common.data.repository.ArtworkRepository
import mes.inc.aic.common.extensions.getKoinInstance

expect val refreshReelTime: Duration

@Composable
fun HomeScreenStateScope(
    refresh: Boolean = false,
    query: String = "",
    artworkRepository: ArtworkRepository = getKoinInstance(),
    scope: @Composable (State<HomeScreenState>) -> Unit = {},
) {
    val state = remember { mutableStateOf(HomeScreenState()) }
    val artworks = artworkRepository.fetchArtworks(query).collectAsState(emptyList())

    LaunchedEffect(Unit) {
        while (true) {
            val reel = artworkRepository.fetchArtworkReels()
            state.value = state.value.copy(reel = reel)
            delay(refreshReelTime)
        }
    }

    LaunchedEffect(artworks.value) {
        state.value = state.value.copy(artworks = artworks.value)
    }

    LaunchedEffect(query, refresh) {
        artworkRepository.syncArtworks(query.ifEmpty { null })
    }
    scope(state)
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var refresh by remember { mutableStateOf(true) }
    var query by remember { mutableStateOf("") }
    HomeScreenStateScope(
        query = query,
        refresh = refresh
    ) { state ->
        Column(
            modifier = modifier.scrollable(
                state = rememberScrollState(), orientation = Orientation.Vertical
            ).padding(start = Padding.Normal, end = Padding.Normal),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Search(
                query = query,
                onQueryChanged = { query = it },
                modifier = Modifier.fillMaxWidth().padding(top = Padding.Small)
            )
            HomeScreenContent(
                reel = state.value.reel,
                showReel = query.isEmpty(),
                artworks = state.value.artworks
            )
        }
    }

    LaunchedEffect(refresh) {
        if (refresh) {
            refresh = false
        }
    }
}

@Composable
fun HomeScreenContent(
    reel: Reel? = null,
    showReel: Boolean = false,
    artworks: List<Artwork> = emptyList()
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = Modifier.testTag(HOMES_SCREEN_ARTWORKS).padding(top = Padding.Medium),
        contentPadding = PaddingValues(horizontal = Padding.Medium),
    ) {
        if (showReel) {
            item(span = { GridItemSpan(currentLineSpan = Int.MAX_VALUE) }) {
                reel?.let {
                    ReelComponent(reel = it, modifier = Modifier.fillMaxWidth().testTag(HOMES_SCREEN_REEL))
                }
            }
        }
        items(artworks) { artwork ->
            ArtworkComponent(
                artwork = artwork, modifier = Modifier.padding(top = Padding.Medium)
            )
        }
        if (artworks.isEmpty()) {
            item {
                Text(
                    text = "No artworks available.",
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(textAlign = TextAlign.Center)
                )
            }
        }
    }
}

data class HomeScreenState(
    val refresh: Boolean = false,
    val searchQuery: String? = null,
    val reel: Reel? = null,
    val artworks: List<Artwork> = emptyList(),
)