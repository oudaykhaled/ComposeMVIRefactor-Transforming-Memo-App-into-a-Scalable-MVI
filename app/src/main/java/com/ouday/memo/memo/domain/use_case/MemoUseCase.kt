package com.ouday.memo.memo.domain.use_case

import com.ouday.memo.memo.domain.model.Memo
import com.ouday.memo.memo.domain.util.MemoOrder
import com.ouday.memo.memo.domain.util.OrderType
import kotlinx.coroutines.flow.Flow

interface MemoUseCase {
    suspend fun addMemo(memo: Memo)
    suspend fun deleteMemo(memo: Memo)
    suspend fun getMemo(id: Int): Memo?
    fun getMemos(memoOrder: MemoOrder = MemoOrder.Date(OrderType.Descending)): Flow<List<Memo>>
}
