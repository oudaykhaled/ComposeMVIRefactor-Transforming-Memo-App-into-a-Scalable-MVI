package com.ouday.memo.memo.presentation.memos

import com.ouday.memo.memo.domain.model.Memo
import com.ouday.memo.memo.domain.util.MemoOrder

sealed class MemosEvent {
    data class Order(val memoOrder: MemoOrder): MemosEvent()
    data class DeleteMemo(val memo: Memo): MemosEvent()
    object RestoreMemo: MemosEvent()
    object ToggleOrderSection: MemosEvent()
}
