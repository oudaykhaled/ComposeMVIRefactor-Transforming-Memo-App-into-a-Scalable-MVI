package com.ouday.memo.memo.domain.repository

import com.ouday.memo.memo.domain.model.Memo
import kotlinx.coroutines.flow.Flow

interface MemoRepository {

    fun getMemos(): Flow<List<Memo>>

    suspend fun getMemoById(id: Int): Memo?

    suspend fun insertMemo(memo: Memo)

    suspend fun deleteMemo(memo: Memo)
}