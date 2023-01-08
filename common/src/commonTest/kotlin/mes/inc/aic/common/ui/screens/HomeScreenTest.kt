package mes.inc.aic.common.ui.screens

import androidx.compose.ui.test.junit4.createComposeRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.flowOf
import mes.inc.aic.common.data.model.Artwork
import mes.inc.aic.common.data.model.Reel
import mes.inc.aic.common.data.repository.ArtworkRepository
import mes.inc.aic.common.ui.HomeScreenStateScope
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule

class HomeScreenTest

class HomeScreenStateScopeTest : KoinTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val artworkRepository: ArtworkRepository = mockk()

    private val module = module {
        single { artworkRepository }
    }

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(module)
    }

    @Test
    fun reelsDoRefresh() {
        composeTestRule.setContent { HomeScreenStateScope(refreshReels = true) }
        verify(exactly = 1) { artworkRepository.fetchArtworkReels() }
    }

    @Test
    fun reelsDoeNotRefresh() {
        composeTestRule.setContent { HomeScreenStateScope(refreshReels = false) }
        verify(exactly = 0) { artworkRepository.fetchArtworkReels() }
    }

    @Test
    fun artworksDoRefresh() {
        composeTestRule.setContent { HomeScreenStateScope(refreshArtwork = true) }
        verify(exactly = 1) { artworkRepository.fetchArtworkReels() }
    }

    @Test
    fun artworksDoeNotRefresh() {
        composeTestRule.setContent { HomeScreenStateScope(refreshArtwork = false) }
        verify(exactly = 0) { artworkRepository.fetchArtworkReels() }
    }

    @Test
    fun artworksStateUpdatedWhenRefreshed() {
        var artworks = listOf<Artwork>()
        every { artworkRepository.fetchArtworks() } returns flowOf(listOf(Artwork()))
        composeTestRule.setContent {
            HomeScreenStateScope(refreshArtwork = true) {
                artworks = it.value.artworks
            }
        }
        assertEquals(1, artworks.size)
    }

    @Test
    fun reelsStateUpdatedWhenRefreshed() {
        val expectedReel = Reel()
        var reel: Reel? = null
        every { artworkRepository.fetchArtworkReels() } returns flowOf(listOf(expectedReel))
        composeTestRule.setContent {
            HomeScreenStateScope(refreshReels = true) {
                reel = it.value.reel
            }
        }
        assertEquals(expectedReel, reel)
    }
}