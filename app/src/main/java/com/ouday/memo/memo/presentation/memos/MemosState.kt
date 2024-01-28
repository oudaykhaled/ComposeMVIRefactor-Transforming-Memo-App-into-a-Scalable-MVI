package com.ouday.memo.memo.presentation.memos

import com.ouday.memo.memo.domain.model.Memo
import com.ouday.memo.memo.domain.util.MemoOrder
import com.ouday.memo.memo.domain.util.OrderType

data class MemosState(
    val memos: List<Memo> = emptyList(),
    val memoOrder: MemoOrder = MemoOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
