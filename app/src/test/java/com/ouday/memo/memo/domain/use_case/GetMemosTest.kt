package com.ouday.memo.memo.domain.use_case

import com.google.common.truth.Truth.assertThat
import com.ouday.memo.memo.data.repository.FakeMemoRepository
import com.ouday.memo.memo.domain.model.Memo
import com.ouday.memo.memo.domain.util.MemoOrder
import com.ouday.memo.memo.domain.util.OrderType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetMemosTest {

    private lateinit var getMemos: GetMemos
    private lateinit var fakeRepository: FakeMemoRepository

    @Before
    fun setUp() {
        fakeRepository = FakeMemoRepository()
        getMemos = GetMemos(fakeRepository)

        val memosToInsert = mutableListOf<Memo>()
        ('a'..'z').forEachIndexed { index, c ->
            memosToInsert.add(
                Memo(
                    title = c.toString(),
                    content = c.toString(),
                    timestamp = index.toLong(),
                    color = index
                )
            )
        }
        memosToInsert.shuffle()
        runBlocking {
            memosToInsert.forEach { fakeRepository.insertMemo(it) }
        }
    }

    @Test
    fun `Order Memos by title ascending, correct order`() = runBlocking {
        val memos = getMemos(MemoOrder.Title(OrderType.Ascending)).first()

        for(i in 0..memos.size - 2) {
            assertThat(memos[i].title).isLessThan(memos[i+1].title)
        }
    }

    @Test
    fun `Order Memos by title descending, correct order`() = runBlocking {
        val memos = getMemos(MemoOrder.Title(OrderType.Descending)).first()

        for(i in 0..memos.size - 2) {
            assertThat(memos[i].title).isGreaterThan(memos[i+1].title)
        }
    }

    @Test
    fun `Order Memos by date ascending, correct order`() = runBlocking {
        val memos = getMemos(MemoOrder.Date(OrderType.Ascending)).first()

        for(i in 0..memos.size - 2) {
            assertThat(memos[i].timestamp).isLessThan(memos[i+1].timestamp)
        }
    }

    @Test
    fun `Order Memos by date descending, correct order`() = runBlocking {
        val memos = getMemos(MemoOrder.Date(OrderType.Descending)).first()

        for(i in 0..memos.size - 2) {
            assertThat(memos[i].timestamp).isGreaterThan(memos[i+1].timestamp)
        }
    }

    @Test
    fun `Order Memos by color ascending, correct order`() = runBlocking {
        val memos = getMemos(MemoOrder.Color(OrderType.Ascending)).first()

        for(i in 0..memos.size - 2) {
            assertThat(memos[i].color).isLessThan(memos[i+1].color)
        }
    }

    @Test
    fun `Order Memos by color descending, correct order`() = runBlocking {
        val memos = getMemos(MemoOrder.Color(OrderType.Descending)).first()

        for(i in 0..memos.size - 2) {
            assertThat(memos[i].color).isGreaterThan(memos[i+1].color)
        }
    }
}