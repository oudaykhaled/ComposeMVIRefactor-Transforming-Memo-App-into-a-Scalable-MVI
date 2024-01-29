package com.ouday.memo.memo.presentation.add_edit_memo

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ouday.memo.core.util.TestTags
import com.ouday.memo.memo.domain.model.Memo
import com.ouday.memo.memo.presentation.add_edit_memo.components.TransparentHintTextField
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@Composable
fun AddEditMemoScreen(
    navController: NavController,
    state: AddEditMemoState,
    event: (event: AddEditMemoEvent) -> Unit,
    eventFlow: Flow<AddEditMemoViewModel.UiEvent>
) {
    val titleState = state.title
    val contentState = state.content

    val scaffoldState = rememberScaffoldState()

    val memoBackgroundAnimatable = remember {
        Animatable(
            Color(state.memoColor)
        )
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        eventFlow.collectLatest { event ->
            when(event) {
                is AddEditMemoViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is AddEditMemoViewModel.UiEvent.SaveMemo -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    event(AddEditMemoEvent.SaveMemo)
                },
                backgroundColor = Color(state.memoColor)
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save")
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(state.memoColor))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Memo.memoColors.forEach { color ->
                    val colorInt = color.toArgb()
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(15.dp, CircleShape)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = 3.dp,
                                color = if (state.memoColor == colorInt) {
                                    Color.Black
                                } else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    memoBackgroundAnimatable.animateTo(
                                        targetValue = Color(colorInt),
                                        animationSpec = tween(
                                            durationMillis = 500
                                        )
                                    )
                                }
                                event(AddEditMemoEvent.ChangeColor(colorInt))
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    event(AddEditMemoEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    event(AddEditMemoEvent.ChangeTitleFocus(it))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.h5,
                testTag = TestTags.TEXT_FIELD_TITLE
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = contentState.text,
                hint = contentState.hint,
                onValueChange = {
                    event(AddEditMemoEvent.EnteredContent(it))
                },
                onFocusChange = {
                    event(AddEditMemoEvent.ChangeContentFocus(it))
                },
                isHintVisible = contentState.isHintVisible,
                textStyle = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxHeight(),
                testTag = TestTags.TEXT_FIELD_CONTENT
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun `Memo screen with hint`() {
    val sampleState = AddEditMemoState(
        title = MemoTextFieldState(
            text = "",
            hint = "Enter title..."
        ),
        content = MemoTextFieldState(
            text = "",
            hint = "Enter some content"
        ),
        memoColor = Memo.memoColors.random().toArgb()
    )

    AddEditMemoScreen(
        navController = rememberNavController(),
        state = sampleState,
        event = {  },
        eventFlow = flow {  }
    )
}
@Preview(showBackground = true)
@Composable
fun `Memo screen with no hint`() {
    val sampleState = AddEditMemoState(
        title = MemoTextFieldState(
            text = "Memo title example",
            hint = ""
        ),
        content = MemoTextFieldState(
            text = "Memo content example",
            hint = ""
        ),
        memoColor = Memo.memoColors.random().toArgb()
    )

    AddEditMemoScreen(
        navController = rememberNavController(),
        state = sampleState,
        event = {  },
        eventFlow = flow {  }
    )
}