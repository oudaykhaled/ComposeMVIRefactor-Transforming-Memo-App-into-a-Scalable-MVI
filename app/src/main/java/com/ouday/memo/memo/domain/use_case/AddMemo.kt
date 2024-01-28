package com.ouday.memo.memo.domain.use_case

import com.ouday.memo.memo.domain.model.InvalidMemoException
import com.ouday.memo.memo.domain.model.Memo
import com.ouday.memo.memo.domain.repository.MemoRepository

class AddMemo(
    private val repository: MemoRepository
) {

    @Throws(InvalidMemoException::class)
    suspend operator fun invoke(Memo: Memo) {
        if(Memo.title.isBlank()) {
            throw InvalidMemoException("The title of the Memo can't be empty.")
        }
        if(Memo.content.isBlank()) {
            throw InvalidMemoException("The content of the Memo can't be empty.")
        }
        repository.insertMemo(Memo)
    }
}