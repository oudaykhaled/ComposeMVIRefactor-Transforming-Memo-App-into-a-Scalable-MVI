package com.ouday.memo.memo.presentation.add_edit_memo

import androidx.compose.ui.graphics.toArgb
import com.ouday.memo.memo.domain.model.Memo

data class AddEditMemoState(
    val title: MemoTextFieldState = MemoTextFieldState(
        hint = "Enter title..."
    ),
    val content: MemoTextFieldState = MemoTextFieldState(
        hint = "Enter some content"
    ),
    val memoColor: Int = Memo.memoColors.random().toArgb()
)
