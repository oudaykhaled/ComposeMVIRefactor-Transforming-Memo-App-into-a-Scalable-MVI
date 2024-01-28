package com.ouday.memo.memo.domain.use_case

import com.ouday.memo.memo.domain.model.Memo
import com.ouday.memo.memo.domain.repository.MemoRepository
import com.ouday.memo.memo.domain.util.MemoOrder
import com.ouday.memo.memo.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMemos(
    private val repository: MemoRepository
) {

    operator fun invoke(
        memoOrder: MemoOrder = MemoOrder.Date(OrderType.Descending)
    ): Flow<List<Memo>> {
        return repository.getMemos().map { Memos ->
            when(memoOrder.orderType) {
                is OrderType.Ascending -> {
                    when(memoOrder) {
                        is MemoOrder.Title -> Memos.sortedBy { it.title.lowercase() }
                        is MemoOrder.Date -> Memos.sortedBy { it.timestamp }
                        is MemoOrder.Color -> Memos.sortedBy { it.color }
                    }
                }
                is OrderType.Descending -> {
                    when(memoOrder) {
                        is MemoOrder.Title -> Memos.sortedByDescending { it.title.lowercase() }
                        is MemoOrder.Date -> Memos.sortedByDescending { it.timestamp }
                        is MemoOrder.Color -> Memos.sortedByDescending { it.color }
                    }
                }
            }
        }
    }
}