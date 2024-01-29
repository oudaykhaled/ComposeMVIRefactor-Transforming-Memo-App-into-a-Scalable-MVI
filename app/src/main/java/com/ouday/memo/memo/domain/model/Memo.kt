package com.ouday.memo.memo.domain.model

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ouday.memo.ui.theme.BabyBlue
import com.ouday.memo.ui.theme.LightGreen
import com.ouday.memo.ui.theme.RedOrange
import com.ouday.memo.ui.theme.RedPink
import com.ouday.memo.ui.theme.Violet

@Entity
@Immutable
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