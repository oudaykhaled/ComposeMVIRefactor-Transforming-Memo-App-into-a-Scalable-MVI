package com.ouday.memo.memo.presentation.add_edit_memo

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ouday.memo.memo.domain.model.InvalidMemoException
import com.ouday.memo.memo.domain.model.Memo
import com.ouday.memo.memo.domain.use_case.MemoUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditMemoViewModel @Inject constructor(
    private val memoUseCases: MemoUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(AddEditMemoState())
    val state: State<AddEditMemoState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentMemoId: Int? = null

    init {
        savedStateHandle.get<Int>("memoId")?.let { memoId ->
            if(memoId != -1) {
                viewModelScope.launch {
                    memoUseCases.getMemo(memoId)?.also { memo ->
                        currentMemoId = memo.id
                        _state.value = _state.value.copy(
                            title = _state.value.title.copy(
                                text = memo.title,
                                isHintVisible = false
                            ),
                            content = _state.value.content.copy(
                                text = memo.content,
                                isHintVisible = false
                            ),
                            memoColor = memo.color
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditMemoEvent) {
        when(event) {
            is AddEditMemoEvent.EnteredTitle -> {
                _state.value = _state.value.copy(
                    title = _state.value.title.copy(
                        text = event.value
                    )
                )
            }
            is AddEditMemoEvent.ChangeTitleFocus -> {
                _state.value = _state.value.copy(
                    title = _state.value.title.copy(
                        isHintVisible = !event.focusState.isFocused &&
                                _state.value.title.text.isBlank()
                    )
                )
            }
            is AddEditMemoEvent.EnteredContent -> {
                _state.value = _state.value.copy(
                    content = _state.value.content.copy(
                        text = event.value
                    )
                )
            }
            is AddEditMemoEvent.ChangeContentFocus -> {
                _state.value = _state.value.copy(
                    content = _state.value.content.copy(
                        isHintVisible = !event.focusState.isFocused &&
                                _state.value.content.text.isBlank()
                    )
                )
            }
            is AddEditMemoEvent.ChangeColor -> {
                _state.value = _state.value.copy(
                    memoColor = event.color
                )
            }
            is AddEditMemoEvent.SaveMemo -> {
                viewModelScope.launch {
                    try {
                        memoUseCases.addMemo(
                            Memo(
                                title = _state.value.title.text,
                                content = _state.value.content.text,
                                timestamp = System.currentTimeMillis(),
                                color = _state.value.memoColor,
                                id = currentMemoId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveMemo)
                    } catch(e: InvalidMemoException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save Memo"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        object SaveMemo: UiEvent()
    }
}