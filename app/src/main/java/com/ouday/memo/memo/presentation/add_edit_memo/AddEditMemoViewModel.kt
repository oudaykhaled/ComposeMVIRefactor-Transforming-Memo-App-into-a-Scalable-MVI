package com.ouday.memo.memo.presentation.add_edit_memo

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
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

    private val _memoTitle = mutableStateOf(MemoTextFieldState(
        hint = "Enter title..."
    ))
    val memoTitle: State<MemoTextFieldState> = _memoTitle

    private val _memoContent = mutableStateOf(MemoTextFieldState(
        hint = "Enter some content"
    ))
    val memoContent: State<MemoTextFieldState> = _memoContent

    private val _memoColor = mutableStateOf(Memo.memoColors.random().toArgb())
    val memoColor: State<Int> = _memoColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentMemoId: Int? = null

    init {
        savedStateHandle.get<Int>("MemoId")?.let { MemoId ->
            if(MemoId != -1) {
                viewModelScope.launch {
                    memoUseCases.getMemo(MemoId)?.also { Memo ->
                        currentMemoId = Memo.id
                        _memoTitle.value = memoTitle.value.copy(
                            text = Memo.title,
                            isHintVisible = false
                        )
                        _memoContent.value = _memoContent.value.copy(
                            text = Memo.content,
                            isHintVisible = false
                        )
                        _memoColor.value = Memo.color
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditMemoEvent) {
        when(event) {
            is AddEditMemoEvent.EnteredTitle -> {
                _memoTitle.value = memoTitle.value.copy(
                    text = event.value
                )
            }
            is AddEditMemoEvent.ChangeTitleFocus -> {
                _memoTitle.value = memoTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            memoTitle.value.text.isBlank()
                )
            }
            is AddEditMemoEvent.EnteredContent -> {
                _memoContent.value = _memoContent.value.copy(
                    text = event.value
                )
            }
            is AddEditMemoEvent.ChangeContentFocus -> {
                _memoContent.value = _memoContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _memoContent.value.text.isBlank()
                )
            }
            is AddEditMemoEvent.ChangeColor -> {
                _memoColor.value = event.color
            }
            is AddEditMemoEvent.SaveMemo -> {
                viewModelScope.launch {
                    try {
                        memoUseCases.addMemo(
                            Memo(
                                title = memoTitle.value.text,
                                content = memoContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = memoColor.value,
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