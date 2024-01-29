package com.ouday.memo.memo.presentation.memos

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ouday.memo.memo.domain.model.Memo
import com.ouday.memo.memo.domain.use_case.MemoUseCases
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
    private val memoUseCases: MemoUseCases
) : ViewModel() {

    private val _state = mutableStateOf(MemosState())
    val state: State<MemosState> = _state

    private var recentlyDeletedMemo: Memo? = null

    private var getMemosJob: Job? = null

    init {
        getMemos(MemoOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: MemosEvent) {
        when (event) {
            is MemosEvent.Order -> {
                if (state.value.memoOrder::class == event.memoOrder::class &&
                    state.value.memoOrder.orderType == event.memoOrder.orderType
                ) {
                    return
                }
                getMemos(event.memoOrder)
            }
            is MemosEvent.DeleteMemo -> {
                viewModelScope.launch {
                    memoUseCases.deleteMemo(event.memo)
                    recentlyDeletedMemo = event.memo
                }
            }
            is MemosEvent.RestoreMemo -> {
                viewModelScope.launch {
                    memoUseCases.addMemo(recentlyDeletedMemo ?: return@launch)
                    recentlyDeletedMemo = null
                }
            }
            is MemosEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getMemos(memoOrder: MemoOrder) {
        getMemosJob?.cancel()
        getMemosJob = memoUseCases.getMemos(memoOrder)
            .onEach { Memos ->
                _state.value = state.value.copy(
                    memos = Memos,
                    memoOrder = memoOrder
                )
            }
            .launchIn(viewModelScope)
    }
}