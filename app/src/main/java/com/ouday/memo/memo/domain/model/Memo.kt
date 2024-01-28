package com.ouday.memo.memo.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ouday.memo.ui.theme.*

@Entity
data class Memo(
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    @PrimaryKey val id: Int? = null
) {
    companion object {
        val memoColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}

class InvalidMemoException(message: String): Exception(message)