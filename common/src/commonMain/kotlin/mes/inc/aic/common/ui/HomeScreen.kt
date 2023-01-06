package mes.inc.aic.common.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mes.inc.aic.common.data.model.Artwork
import mes.inc.aic.common.data.model.Reel
import mes.inc.aic.common.data.repository.ArtworkRepository
import mes.inc.aic.common.extensions.getKoinInstance
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Composable
fun HomeScreen(modifier: Modifier = Modifier, stateHolder: HomeScreenStateHolder = getKoinInstance()) {
    val state = stateHolder.state.collectAsState()

    Column(modifier = modifier) {
        state.value.reel?.let {
            Reel(reel = Reel())
        }
        LazyVerticalGrid(columns = GridCells.Adaptive(100.dp)) {
            itemsIndexed(items = state.value.artworks) { _, item ->
                ArtworkComponent(
                    artwork = item,
                    modifier = Modifier
                )
            }
        }
    }
}

data class HomeScreenState(
    val reel: Reel? = null,
    val artworks: List<Artwork> = emptyList(),
)

// @Singleton
class HomeScreenStateHolder : KoinComponent {

    private val artworkRepository: ArtworkRepository by inject()
    private val coroutineScope: CoroutineScope = MainScope()
    private var artworksJob: Job? = null
    private var reelsJob: Job? = null

    init {
        fetchReels()
        fetchArtworks()
    }

    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    fun fetchArtworks() {
        artworksJob?.cancel()
        artworksJob = coroutineScope.launch {
            artworkRepository.fetchArtworks().collectLatest { artworks ->
                _state.update { it.copy(artworks = artworks) }
            }
        }
    }

    fun fetchReels() {
        reelsJob = coroutineScope.launch {
            artworkRepository.fetchArtworkReels().collectLatest { reels ->
                _state.update { it.copy(reel = reels.firstOrNull()) }
            }
        }
    }

    fun cancelJobs() {
        coroutineScope.cancel()
    }
}
