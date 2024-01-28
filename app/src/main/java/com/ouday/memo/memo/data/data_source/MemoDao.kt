package com.ouday.memo.memo.data.data_source

import androidx.room.*
import com.ouday.memo.memo.domain.model.Memo
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoDao {

    @Query("SELECT * FROM Memo")
    fun getMemos(): Flow<List<Memo>>

    @Query("SELECT * FROM Memo WHERE id = :id")
    suspend fun getMemoById(id: Int): Memo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemo(Memo: Memo)

    @Delete
    suspend fun deleteMemo(Memo: Memo)
}