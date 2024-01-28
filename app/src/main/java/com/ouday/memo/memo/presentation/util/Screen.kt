package com.ouday.memo.memo.presentation.util

sealed class Screen(val route: String) {
    object MemosScreen: Screen("memos_screen")
    object AddEditMemoScreen: Screen("add_edit_memo_screen")
}
