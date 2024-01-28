package com.ouday.memo.memo.presentation.memos

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ouday.memo.memo.domain.model.Memo
import com.ouday.memo.memo.domain.use_case.MemoUseCase
import com.ouday.memo.memo.domain.util.MemoOrder
import com.ouday.memo.memo.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemosViewModel @Inject constructor(
    private val memoUseCases: MemoUseCase
) : ViewModel() {

    val memoOrder = mutableStateOf<MemoOrder>(MemoOrder.Date(OrderType.Descending))
    val memos = mutableStateOf(emptyList<Memo>())
    val isOrderSectionVisible = mutableStateOf(false)

    private var recentlyDeletedMemo: Memo? = null

    private var getMemosJob: Job? = null

    init {
        getMemos(MemoOrder.Date(OrderType.Descending))
    }

    fun restoreMemo(){
        viewModelScope.launch {
            memoUseCases.addMemo(recentlyDeletedMemo ?: return@launch)
            recentlyDeletedMemo = null
        }
    }

    fun deleteMemo(memo: Memo){
        viewModelScope.launch {
            memoUseCases.deleteMemo(memo)
            recentlyDeletedMemo = memo
        }
    }

    fun getMemos(requiredMemoOrder: MemoOrder) {
        getMemosJob?.cancel()
        getMemosJob = memoUseCases.getMemos(memoOrder.value)
            .onEach { retrievedMemos ->
                memos.value = retrievedMemos
                memoOrder.value = requiredMemoOrder
            }
            .launchIn(viewModelScope)
    }
}