package com.ouday.memo.memo.data.repository

import com.ouday.memo.memo.domain.model.Memo
import com.ouday.memo.memo.domain.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeMemoRepository : MemoRepository {

    private val memos = mutableListOf<Memo>()

    override fun getMemos(): Flow<List<Memo>> {
        return flow { emit(memos) }
    }

    override suspend fun getMemoById(id: Int): Memo? {
        return memos.find { it.id == id }
    }

    override suspend fun insertMemo(Memo: Memo) {
        memos.add(Memo)
    }

    override suspend fun deleteMemo(Memo: Memo) {
        memos.remove(Memo)
    }
}