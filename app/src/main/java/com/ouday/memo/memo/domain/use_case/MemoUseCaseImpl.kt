package com.ouday.memo.memo.domain.use_case

import com.ouday.memo.memo.domain.model.Memo
import com.ouday.memo.memo.domain.repository.MemoRepository
import com.ouday.memo.memo.domain.util.MemoOrder
import com.ouday.memo.memo.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MemoUseCaseImpl(private val repository: MemoRepository): MemoUseCase {

    override suspend fun addMemo(memo: Memo) {
        repository.insertMemo(memo)
    }

    override suspend fun deleteMemo(memo: Memo) {
        repository.deleteMemo(memo)
    }

    override suspend fun getMemo(id: Int): Memo? {
        return repository.getMemoById(id)
    }

    override fun getMemos(
        memoOrder: MemoOrder
    ): Flow<List<Memo>> {
        return repository.getMemos().map { memos ->
            when(memoOrder.orderType) {
                is OrderType.Ascending -> {
                    when(memoOrder) {
                        is MemoOrder.Title -> memos.sortedBy { it.title.lowercase() }
                        is MemoOrder.Date -> memos.sortedBy { it.timestamp }
                        is MemoOrder.Color -> memos.sortedBy { it.color }
                    }
                }
                is OrderType.Descending -> {
                    when(memoOrder) {
                        is MemoOrder.Title -> memos.sortedByDescending { it.title.lowercase() }
                        is MemoOrder.Date -> memos.sortedByDescending { it.timestamp }
                        is MemoOrder.Color -> memos.sortedByDescending { it.color }
                    }
                }
            }
        }
    }
}
