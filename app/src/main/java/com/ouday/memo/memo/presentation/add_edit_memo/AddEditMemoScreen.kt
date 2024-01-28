package com.ouday.memo.memo.presentation.add_edit_memo

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ouday.memo.core.util.TestTags
import com.ouday.memo.memo.domain.model.Memo
import com.ouday.memo.memo.presentation.add_edit_memo.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AddEditMemoScreen(
    navController: NavController,
    viewModel: AddEditMemoViewModel = hiltViewModel()
) {

    val scaffoldState = rememberScaffoldState()

    val memoBackgroundAnimatable = remember {
        Animatable(
            Color(viewModel.memoColor.value)
        )
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.saveMemo.collectLatest { success ->
            if (success) {
                navController.navigateUp()
            } else {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "Failed to save memo!"
                )
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.saveMemo(
                        viewModel.memoTitle.value,
                        viewModel.memoContent.value,
                        viewModel.memoColor.value
                    )
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save")
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(memoBackgroundAnimatable.value)
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
                                color = if (viewModel.memoColor.value == colorInt) {
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
                                viewModel.memoColor.value = colorInt
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = viewModel.memoTitle.value,
                hint = "Enter title...",
                isHintVisible = viewModel.memoTitle.value.isBlank(),
                onValueChange = {
                    viewModel.memoTitle.value = it
                },
                textStyle = MaterialTheme.typography.h5,
                singleLine = true,
                testTag = TestTags.TEXT_FIELD_TITLE
            ) {

            }
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = viewModel.memoContent.value,
                hint = "Enter some content",
                isHintVisible = viewModel.memoContent.value.isBlank(),
                modifier = Modifier.fillMaxHeight(),
                onValueChange = {
                    viewModel.memoContent.value = it
                },
                textStyle = MaterialTheme.typography.body1,
                testTag = TestTags.TEXT_FIELD_CONTENT
            ) {
            }

        }
    }
}