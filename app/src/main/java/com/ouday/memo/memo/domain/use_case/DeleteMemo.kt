package com.ouday.memo.memo.domain.use_case

import com.ouday.memo.memo.domain.model.Memo
import com.ouday.memo.memo.domain.repository.MemoRepository

class DeleteMemo(
    private val repository: MemoRepository
) {

    suspend operator fun invoke(Memo: Memo) {
        repository.deleteMemo(Memo)
    }
}