package com.jonathansteele.topcointrack

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test
import kotlin.system.measureTimeMillis

class LoadTimeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testComposableLoadTime() {
        // Define composable with a tag for identification
        composeTestRule.setContent {
            CoinLoreApp() // Your composable here
        }

        // Measure time taken for composable to be displayed
        val loadTime = measureTimeMillis {
            composeTestRule.onNodeWithTag("CoinLoreAppTag")
                .assertIsDisplayed()
        }

        println("Composable load time: $loadTime ms")
    }
}