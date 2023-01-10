package mes.inc.aic.common.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import mes.inc.aic.common.constants.NETWORK_IMAGE
import mes.inc.aic.common.constants.NETWORK_IMAGE_LOADER
import mes.inc.aic.common.ui.NetworkImage
import org.junit.Rule
import org.junit.Test

class NetworkImageComponentTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val imageUrl = "https://images.pexels.com/photos/1906795/pexels-photo-1906795.jpeg"

    @Test
    fun imageIsDisplayed() {
        composeTestRule.setContent { NetworkImage(link = imageUrl) }
        composeTestRule.onNodeWithTag(NETWORK_IMAGE).assertIsDisplayed()
    }

    @Test
    fun imageIsNotDisplayed() {
        composeTestRule.setContent { NetworkImage(link = "") }
        composeTestRule.onNodeWithTag(NETWORK_IMAGE).assertIsNotDisplayed()
    }

    @Test
    fun imageLoadingIsDisplayed() {
        composeTestRule.setContent { NetworkImage(link = "") }
        composeTestRule.onNodeWithTag(NETWORK_IMAGE_LOADER).assertIsNotDisplayed()
    }
}