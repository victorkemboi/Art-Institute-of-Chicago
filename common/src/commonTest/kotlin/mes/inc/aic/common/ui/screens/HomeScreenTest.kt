package mes.inc.aic.common.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import io.mockk.*
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.flowOf
import mes.inc.aic.common.data.model.Artwork
import mes.inc.aic.common.data.model.Reel
import mes.inc.aic.common.data.repository.ArtworkRepository
import mes.inc.aic.common.ui.HomeScreen
import mes.inc.aic.common.ui.HomeScreenStateScope
import mes.inc.aic.common.constants.HOMES_SCREEN_ARTWORKS
import mes.inc.aic.common.constants.HOMES_SCREEN_REEL
import mes.inc.aic.common.constants.HOMES_SCREEN_REFRESH_BUTTON
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule

class HomeScreenTest {
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
    fun reelIsDisplayed() {
        every { artworkRepository.fetchArtworkReels() } returns flowOf(listOf(Reel()))
        composeTestRule.setContent { HomeScreen() }
        composeTestRule.onNodeWithTag(HOMES_SCREEN_REEL).assertIsDisplayed()
    }

    @Test
    fun reelIsNotDisplayed() {
        composeTestRule.setContent { HomeScreen() }
        composeTestRule.onNodeWithTag(HOMES_SCREEN_REEL).assertIsNotDisplayed()
    }

    @Test
    fun artworksAreDisplayed() {
        coEvery { artworkRepository.fetchArtworks() } returns flowOf(listOf(Artwork(), Artwork()))
        composeTestRule.setContent { HomeScreen() }
        composeTestRule
            .onNodeWithTag(HOMES_SCREEN_ARTWORKS)
            .onChildren()
            .assertCountEquals(2)
    }

    @Test
    fun refreshButtonIsDisplayed() {
        composeTestRule.setContent { HomeScreen() }
        composeTestRule
            .onNodeWithTag(HOMES_SCREEN_REFRESH_BUTTON)
            .assertIsDisplayed()
    }

    @Test
    fun artworksAreRefreshedWhenRefreshButtonIsClicked() {
        composeTestRule.setContent { HomeScreen() }
        composeTestRule
            .onNodeWithTag(HOMES_SCREEN_REFRESH_BUTTON)
            .performClick()
        coVerify (exactly = 2) { artworkRepository.fetchArtworks() }
    }

    @Test
    fun reelIsRefreshedWhenRefreshButtonIsClicked() {
        composeTestRule.setContent { HomeScreen() }
        composeTestRule
            .onNodeWithTag(HOMES_SCREEN_REFRESH_BUTTON)
            .performClick()
        verify(exactly = 2) { artworkRepository.fetchArtworkReels() }
    }
}

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
        coEvery { artworkRepository.fetchArtworks() } returns flowOf(listOf(Artwork()))
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