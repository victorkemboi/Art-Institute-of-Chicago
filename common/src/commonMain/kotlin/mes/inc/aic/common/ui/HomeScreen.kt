package mes.inc.aic.common.ui

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import mes.inc.aic.common.constants.HOMES_SCREEN_ARTWORKS
import mes.inc.aic.common.constants.HOMES_SCREEN_REEL
import mes.inc.aic.common.constants.HOMES_SCREEN_REFRESH_BUTTON
import mes.inc.aic.common.data.model.Artwork
import mes.inc.aic.common.data.model.Reel
import mes.inc.aic.common.data.repository.ArtworkRepository
import mes.inc.aic.common.extensions.getKoinInstance

@Composable
fun HomeScreenStateScope(
    currentState: HomeScreenState = HomeScreenState(),
    artworkRepository: ArtworkRepository = getKoinInstance(),
    scope: @Composable (State<HomeScreenState>) -> Unit = {},
) {
    val state = remember { mutableStateOf(currentState) }
    val artworks = artworkRepository.fetchArtworks(currentState.searchQuery).collectAsState(emptyList())
    val reels = artworkRepository.fetchArtworkReels().collectAsState(emptyList())

    LaunchedEffect(artworks.value, reels.value) {
        state.value = state.value.copy(
            artworks = artworks.value,
            reel = reels.value.firstOrNull()
        )
    }
    LaunchedEffect(currentState.searchQuery, currentState.refresh) {
        artworkRepository.syncArtworks(currentState.searchQuery)
    }
    scope(state)
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var refresh by remember { mutableStateOf(true) }

    HomeScreenStateScope { state ->
        Column(modifier = modifier.scrollable(state = rememberScrollState(), orientation = Orientation.Vertical)) {
            state.value.reel?.let {
                ReelComponent(reel = it, modifier = Modifier.fillMaxWidth().testTag(HOMES_SCREEN_REEL))
            }
            LazyVerticalGrid(columns = GridCells.Adaptive(150.dp), modifier = Modifier.testTag(HOMES_SCREEN_ARTWORKS)) {
                items(state.value.artworks) { artwork ->
                    ArtworkComponent(
                        artwork = artwork, modifier = Modifier
                    )
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth(1f).testTag(HOMES_SCREEN_REFRESH_BUTTON),
                onClick = { refresh = true }) {
                Text("Refresh")
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