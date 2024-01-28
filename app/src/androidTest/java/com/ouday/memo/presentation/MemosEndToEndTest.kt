package com.ouday.memo.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ouday.memo.core.util.TestTags
import com.ouday.memo.di.AppModule
import com.ouday.memo.memo.presentation.MainActivity
import com.ouday.memo.memo.presentation.add_edit_memo.AddEditMemoScreen
import com.ouday.memo.memo.presentation.memos.MemosScreen
import com.ouday.memo.memo.presentation.util.Screen
import com.ouday.memo.ui.theme.memoTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class MemosEndToEndTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @ExperimentalAnimationApi
    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            memoTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.MemosScreen.route
                ) {
                    composable(route = Screen.MemosScreen.route) {
                        MemosScreen(navController = navController)
                    }
                    composable(
                        route = Screen.AddEditMemoScreen.route +
                                "?memoId={memoId}&memoColor={memoColor}",
                        arguments = listOf(
                            navArgument(
                                name = "memoId"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            },
                            navArgument(
                                name = "memoColor"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            },
                        )
                    ) {
                        val color = it.arguments?.getInt("memoColor") ?: -1
                        AddEditMemoScreen(
                            navController = navController
                        )
                    }
                }
            }
        }
    }

    @Test
    fun saveNewMemo_editAfterwards() {
        // Click on FAB to get to add memo screen
        composeRule.onNodeWithContentDescription("Add").performClick()

        // Enter texts in title and content text fields
        composeRule
            .onNodeWithTag(TestTags.TEXT_FIELD_TITLE)
            .performTextInput("test-title")
        composeRule
            .onNodeWithTag(TestTags.TEXT_FIELD_CONTENT)
            .performTextInput("test-content")
        // Save the new
        composeRule.onNodeWithContentDescription("Save").performClick()

        // Make sure there is a memo in the list with our title and content
        composeRule.onNodeWithText("test-title").assertIsDisplayed()
        // Click on memo to edit it
        composeRule.onNodeWithText("test-title").performClick()

        composeRule
            .onNodeWithTag(TestTags.TEXT_FIELD_TITLE)
            .performTextInput("2")

        // Update the memo
        composeRule.onNodeWithContentDescription("Save").performClick()

    }

    @Test
    fun saveNewMemos_orderByTitleDescending() {
        for(i in 1..3) {
            // Click on FAB to get to add memo screen
            composeRule.onNodeWithContentDescription("Add").performClick()

            // Enter texts in title and content text fields
            composeRule
                .onNodeWithTag(TestTags.TEXT_FIELD_TITLE)
                .performTextInput(i.toString())
            composeRule
                .onNodeWithTag(TestTags.TEXT_FIELD_CONTENT)
                .performTextInput(i.toString())
            // Save the new
            composeRule.onNodeWithContentDescription("Save").performClick()
        }

        composeRule.onNodeWithText("1").assertIsDisplayed()
        composeRule.onNodeWithText("2").assertIsDisplayed()
        composeRule.onNodeWithText("3").assertIsDisplayed()

        composeRule
            .onNodeWithContentDescription("Sort")
            .performClick()
        composeRule
            .onNodeWithContentDescription("Title")
            .performClick()
        composeRule
            .onNodeWithContentDescription("Descending")
            .performClick()

        composeRule.onAllNodesWithTag(TestTags.ROW_ITEM)[0]
            .assertTextContains("3")
        composeRule.onAllNodesWithTag(TestTags.ROW_ITEM)[1]
            .assertTextContains("2")
        composeRule.onAllNodesWithTag(TestTags.ROW_ITEM)[2]
            .assertTextContains("1")
    }
}