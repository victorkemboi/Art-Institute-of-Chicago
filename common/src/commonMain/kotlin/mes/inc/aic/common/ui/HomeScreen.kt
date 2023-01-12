package mes.inc.aic.common.ui

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.time.Duration
import mes.inc.aic.common.constants.HOMES_SCREEN_ARTWORKS
import mes.inc.aic.common.constants.HOMES_SCREEN_REEL
import mes.inc.aic.common.data.model.Artwork
import mes.inc.aic.common.data.model.Reel
import mes.inc.aic.common.data.repository.ArtworkRepository
import mes.inc.aic.common.extensions.getKoinInstance

expect val refreshReelTime: Duration

@Composable
fun HomeScreenStateScope(
    refreshArtworks: Boolean = false,
    refreshReel: Boolean = true,
    query: String = "",
    artworkRepository: ArtworkRepository = getKoinInstance(),
    scope: @Composable (State<HomeScreenState>) -> Unit = {},
) {
    val state = remember { mutableStateOf(HomeScreenState()) }
    val artworks = artworkRepository.fetchArtworks(query).collectAsState(emptyList())

    LaunchedEffect(refreshReel) {
        if (refreshReel) {
            val reel = artworkRepository.fetchArtworkReels()
            state.value = state.value.copy(reel = reel)
        }
    }

    LaunchedEffect(artworks.value) {
        state.value = state.value.copy(artworks = artworks.value)
    }

    LaunchedEffect(query, refreshArtworks) {
        artworkRepository.syncArtworks(query.ifEmpty { null })
    }
    scope(state)
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var refreshArtworks by remember { mutableStateOf(true) }
    var refreshReel by remember { mutableStateOf(true) }
    var query by remember { mutableStateOf("") }
    var resetReelTimer by remember { mutableStateOf(false) }

    HomeScreenStateScope(
        query = query,
        refreshArtworks = refreshArtworks,
        refreshReel = refreshReel
    ) { state ->
        Box(modifier = modifier) {
            ReelRefreshTimer(
                modifier = Modifier.align(Alignment.TopCenter),
                onRefresh = { refreshReel = true },
                onResetTimer = { resetReelTimer = false },
                resetTimer = resetReelTimer,
            )
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

            IconButton(onClick = { resetReelTimer = true }, modifier = Modifier.size(60.dp)) {
                Icon(Icons.Filled.Refresh, null)
            }
        }
    }

    LaunchedEffect(refreshArtworks, refreshReel) {
        if (refreshArtworks) {
            refreshArtworks = false
        }
        if (refreshReel) {
            refreshReel = false
        }
    }
}

@Composable
fun ReelRefreshTimer(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
    onResetTimer: () -> Unit = {},
    resetTimer: Boolean = false,
) {
    Timer(
        action = onRefresh,
        onResetTimer = onResetTimer,
        resetTimer = resetTimer
    ) {
        LinearProgressIndicator(
            modifier = modifier
                .fillMaxWidth()
                .height(10.dp),
            progress = it,
        )
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
                    ReelComponent(
                        reel = it, modifier = Modifier
                            .fillMaxWidth()
                            .testTag(HOMES_SCREEN_REEL)
                    )
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

@Stable
data class HomeScreenState(
    val refresh: Boolean = false,
    val searchQuery: String? = null,
    val reel: Reel? = null,
    val artworks: List<Artwork> = emptyList(),
)