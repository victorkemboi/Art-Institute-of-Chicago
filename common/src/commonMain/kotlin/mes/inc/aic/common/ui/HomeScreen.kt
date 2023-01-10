package mes.inc.aic.common.ui

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import mes.inc.aic.common.constants.HOMES_SCREEN_ARTWORKS
import mes.inc.aic.common.constants.HOMES_SCREEN_REEL
import mes.inc.aic.common.data.model.Artwork
import mes.inc.aic.common.data.model.Reel
import mes.inc.aic.common.data.repository.ArtworkRepository
import mes.inc.aic.common.extensions.getKoinInstance
@Composable
fun HomeScreenStateScope(
    refresh: Boolean = false,
    query: String = "",
    artworkRepository: ArtworkRepository = getKoinInstance(),
    scope: @Composable (State<HomeScreenState>) -> Unit = {},
) {
    val state = remember { mutableStateOf(HomeScreenState()) }
    val artworks = artworkRepository.fetchArtworks(query).collectAsState(emptyList())
    val reels = artworkRepository.fetchArtworkReels().collectAsState(emptyList())

    LaunchedEffect(artworks.value, reels.value) {
        state.value = state.value.copy(
            artworks = artworks.value, reel = reels.value.firstOrNull()
        )
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
        Box(modifier = modifier) {
            Column(
                modifier = Modifier.scrollable(
                    state = rememberScrollState(), orientation = Orientation.Vertical
                ).padding(start = Padding.Normal, end = Padding.Normal),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                state.value.reel?.let {
                    ReelComponent(reel = it, modifier = Modifier.fillMaxWidth().testTag(HOMES_SCREEN_REEL))
                }
                Search(
                    query = query,
                    onQueryChanged = { query = it },
                    modifier = Modifier.fillMaxWidth().padding(top = Padding.Small)
                )
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(150.dp),
                    modifier = Modifier.testTag(HOMES_SCREEN_ARTWORKS).padding(top = Padding.Medium)
                ) {
                    items(state.value.artworks) { artwork ->
                        ArtworkComponent(
                            artwork = artwork, modifier = Modifier.padding(top = Padding.Small)
                        )
                    }
                }
            }

        }
    }

    LaunchedEffect(refresh) {
        if (refresh) {
            refresh = false
        }
    }
}

data class HomeScreenState(
    val refresh: Boolean = false,
    val searchQuery: String? = null,
    val reel: Reel? = null,
    val artworks: List<Artwork> = emptyList(),
)