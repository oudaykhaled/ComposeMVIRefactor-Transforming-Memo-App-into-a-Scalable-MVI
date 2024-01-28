package com.ouday.memo.memo.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ouday.memo.memo.presentation.add_edit_memo.AddEditMemoScreen
import com.ouday.memo.memo.presentation.memos.MemosScreen
import com.ouday.memo.memo.presentation.util.Screen
import com.ouday.memo.ui.theme.memoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            memoTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
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
                                navController = navController,
//                                memoColor = color
                            )
                        }
                    }
                }
            }
        }
    }
}
