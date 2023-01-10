package mes.inc.aic.common.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import mes.inc.aic.common.data.model.Artwork
import mes.inc.aic.common.ui.ArtworkComponent
import mes.inc.aic.common.constants.NETWORK_IMAGE
import org.junit.Rule
import org.junit.Test

class ArtworkComponentTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val title = "Artwork 76"
    private val thumbnail = "https://images.pexels.com/photos/1906795/pexels-photo-1906795.jpeg"
    private val dateDisplay = "Ancient Classical art"
    private val artwork = Artwork(
        title = title,
        thumbnail = thumbnail,
        dateDisplay = dateDisplay
    )

    @Test
    fun thumbnailNotDisplayed() {
        composeTestRule.setContent { ArtworkComponent(artwork.copy(thumbnail = null)) }
        composeTestRule.onNodeWithTag(NETWORK_IMAGE).assertIsDisplayed()
    }

    @Test
    fun thumbnailDisplayed() {
        composeTestRule.setContent { ArtworkComponent(artwork) }
        composeTestRule.onNodeWithTag(NETWORK_IMAGE).assertIsDisplayed()
    }

    @Test
    fun titleIsDisplayed() {
        composeTestRule.setContent { ArtworkComponent(artwork) }
        composeTestRule.onNodeWithText(title).assertIsDisplayed()
    }

    @Test
    fun dateIsDisplayed() {
        composeTestRule.setContent { ArtworkComponent(artwork) }
        composeTestRule.onNodeWithText(dateDisplay).assertIsDisplayed()
    }

    @Test
    fun dateIsNotDisplayed() {
        composeTestRule.setContent { ArtworkComponent(artwork.copy(dateDisplay = null)) }
        composeTestRule.onNodeWithText(dateDisplay).assertIsDisplayed()
    }
}