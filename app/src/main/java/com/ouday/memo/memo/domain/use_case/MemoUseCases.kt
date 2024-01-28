package com.ouday.memo.memo.domain.use_case

data class MemoUseCases(
    val getMemos: GetMemos,
    val deleteMemo: DeleteMemo,
    val addMemo: AddMemo,
    val getMemo: GetMemo
)
