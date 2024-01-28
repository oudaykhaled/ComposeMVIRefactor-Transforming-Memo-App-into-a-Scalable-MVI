package com.ouday.memo.memo.presentation.util

sealed class Screen(val route: String) {
    object MemosScreen: Screen("Memos_screen")
    object AddEditMemoScreen: Screen("add_edit_Memo_screen")
}
