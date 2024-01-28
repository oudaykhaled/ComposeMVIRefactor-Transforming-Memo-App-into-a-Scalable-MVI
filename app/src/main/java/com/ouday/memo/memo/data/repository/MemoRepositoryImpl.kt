package com.ouday.memo.memo.data.repository

import com.ouday.memo.memo.data.data_source.MemoDao
import com.ouday.memo.memo.domain.model.Memo
import com.ouday.memo.memo.domain.repository.MemoRepository
import kotlinx.coroutines.flow.Flow

class MemoRepositoryImpl(
    private val dao: MemoDao
) : MemoRepository {

    override fun getMemos(): Flow<List<Memo>> {
        return dao.getMemos()
    }

    override suspend fun getMemoById(id: Int): Memo? {
        return dao.getMemoById(id)
    }

    override suspend fun insertMemo(memo: Memo) {
        dao.insertMemo(memo)
    }

    override suspend fun deleteMemo(memo: Memo) {
        dao.deleteMemo(memo)
    }
}