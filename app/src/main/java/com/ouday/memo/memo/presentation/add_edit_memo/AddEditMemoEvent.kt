package com.ouday.memo.memo.presentation.add_edit_memo

import androidx.compose.ui.focus.FocusState

sealed class AddEditMemoEvent{
    data class EnteredTitle(val value: String): AddEditMemoEvent()
    data class ChangeTitleFocus(val focusState: FocusState): AddEditMemoEvent()
    data class EnteredContent(val value: String): AddEditMemoEvent()
    data class ChangeContentFocus(val focusState: FocusState): AddEditMemoEvent()
    data class ChangeColor(val color: Int): AddEditMemoEvent()
    object SaveMemo: AddEditMemoEvent()
}

