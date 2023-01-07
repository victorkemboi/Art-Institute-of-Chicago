package mes.inc.aic.common.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import mes.inc.aic.common.ui.Reel
import org.junit.Rule
import org.junit.Test
import mes.inc.aic.common.data.model.Reel as ReelModel

class ReelTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val title = "Reel 98"
    private val description = "Reel 98"
    private val reel = ReelModel(
        title = title,
        description = description,
    )

    @Test
    fun titleDisplayed() {
        composeTestRule.setContent { Reel(reel) }
        composeTestRule.onNodeWithText(title).assertIsDisplayed()
        composeTestRule.onNodeWithText(description).assertIsDisplayed()
    }

    @Test
    fun descriptionDisplayed() {
        composeTestRule.setContent { Reel(reel) }
        composeTestRule.onNodeWithText(description).assertIsDisplayed()
    }

    @Test
    fun thumbnailNotDisplayed() {
        composeTestRule.setContent { Reel(reel.copy(thumbnail = null)) }
        composeTestRule.onNodeWithText(description).assertIsDisplayed()
    }

    @Test
    fun thumbnailDisplayed() {
        composeTestRule.setContent {
            Reel(
                reel.copy(
                    thumbnail = "https://images.pexels.com/photos/1906795/pexels-photo-1906795.jpeg"
                )
            )
        }
        composeTestRule.onNodeWithText(description).assertIsDisplayed()
    }
}