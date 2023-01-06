package mes.inc.aic.common.ui

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import mes.inc.aic.common.data.model.Artwork
import mes.inc.aic.common.data.model.Reel
import mes.inc.aic.common.data.repository.ArtworkRepository
import mes.inc.aic.common.extensions.getKoinInstance

@Composable
fun HomeScreenStateScope(
    refreshReels: Boolean = false,
    refreshArtwork: Boolean = false,
    currentState: HomeScreenState = HomeScreenState(),
    state: @Composable (State<HomeScreenState>) -> Unit = {}
) {
    val artworkRepository: ArtworkRepository = getKoinInstance()
    val mutableState = remember { mutableStateOf(currentState) }
    LaunchedEffect(refreshReels) {
        if (refreshReels) {
            artworkRepository.fetchArtworkReels().collectLatest { reels ->
                mutableState.value = mutableState.value.copy(reel = reels.firstOrNull())
            }
        }
    }
    LaunchedEffect(refreshArtwork) {
        if (refreshArtwork) {
            artworkRepository.fetchArtworks().collectLatest { artworks ->
                mutableState.value = mutableState.value.copy(artworks = artworks)
            }
        }
    }
    state(mutableState)
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var refreshArtwork by remember { mutableStateOf(true) }
    var refreshReels by remember { mutableStateOf(true) }

    fun refreshState() {
        refreshArtwork = true
        refreshReels = true
    }

    HomeScreenStateScope(
        refreshArtwork = refreshArtwork,
        refreshReels = refreshReels
    ) { state ->
        Column(modifier = modifier.scrollable(state = rememberScrollState(), orientation = Orientation.Vertical)) {
            state.value.reel?.let {
                Reel(reel = it, modifier = Modifier.fillMaxWidth())
            }
            LazyVerticalGrid(columns = GridCells.Adaptive(100.dp)) {
                itemsIndexed(items = state.value.artworks) { _, item ->
                    ArtworkComponent(
                        artwork = item, modifier = Modifier
                    )
                }
            }
            Button(modifier = Modifier.fillMaxWidth(1f), onClick = { refreshState() }) {
                Text("Refresh")
            }
        }
    }

    LaunchedEffect(refreshArtwork) {
        if (refreshArtwork) {
            refreshArtwork = false
        }
        if (refreshReels) {
            refreshArtwork = false
        }
    }
}

data class HomeScreenState(
    val reel: Reel? = null,
    val artworks: List<Artwork> = emptyList(),
)