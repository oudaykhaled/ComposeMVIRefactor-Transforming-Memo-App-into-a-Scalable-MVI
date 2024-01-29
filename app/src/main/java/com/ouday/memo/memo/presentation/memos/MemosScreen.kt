package com.ouday.memo.memo.presentation.memos

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ouday.memo.core.util.TestTags
import com.ouday.memo.memo.domain.model.Memo
import com.ouday.memo.memo.domain.util.MemoOrder
import com.ouday.memo.memo.domain.util.OrderType
import com.ouday.memo.memo.presentation.memos.components.MemoItem
import com.ouday.memo.memo.presentation.memos.components.OrderSection
import com.ouday.memo.memo.presentation.util.Screen
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@Composable
fun MemosScreen(
    navController: NavController,
    state: MemosState,
    event: (MemosEvent) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditMemoScreen.route)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your Memo",
                    style = MaterialTheme.typography.h4
                )
                IconButton(
                    onClick = {
                        event(MemosEvent.ToggleOrderSection)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = "Sort"
                    )
                }
            }
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .testTag(TestTags.FILTER),
                    memoOrder = state.memoOrder,
                    onOrderChange = {
                        event(MemosEvent.Order(it))
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.memos) { memo ->
                    MemoItem(
                        memo = memo,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    Screen.AddEditMemoScreen.route +
                                            "?memoId=${memo.id}&memoColor=${memo.color}"
                                )
                            },
                        onDeleteClick = {
                            event(MemosEvent.DeleteMemo(memo))
                            scope.launch {
                                val result = scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Memo deleted",
                                    actionLabel = "Undo"
                                )
                                if(result == SnackbarResult.ActionPerformed) {
                                    event(MemosEvent.RestoreMemo)
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Preview(showBackground = true)
@Composable
fun MemosScreenPreview() {
    val sampleMemos = listOf(
        Memo(
            title = "Sample Memo 1",
            content = "This is the first sample memo.",
            timestamp = System.currentTimeMillis(),
            color = Memo.memoColors.random().toArgb(),
            id = 1
        ),
        Memo(
            title = "Sample Memo 2",
            content = "This is the second sample memo.",
            timestamp = System.currentTimeMillis(),
            color = Memo.memoColors.random().toArgb(),
            id = 2
        )
    )

    val sampleState = MemosState(
        memos = sampleMemos,
        memoOrder = MemoOrder.Date(OrderType.Descending),
        isOrderSectionVisible = true
    )

    MemosScreen(
        navController = rememberNavController(),
        state = sampleState,
        event = { }
    )
}
