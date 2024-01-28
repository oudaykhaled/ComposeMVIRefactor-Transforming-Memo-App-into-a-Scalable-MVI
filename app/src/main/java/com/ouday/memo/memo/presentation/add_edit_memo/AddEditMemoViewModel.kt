package com.ouday.memo.memo.presentation.add_edit_memo

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ouday.memo.memo.domain.model.InvalidMemoException
import com.ouday.memo.memo.domain.model.Memo
import com.ouday.memo.memo.domain.use_case.MemoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditMemoViewModel @Inject constructor(
    private val memoUseCases: MemoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _saveMemo = MutableSharedFlow<Boolean>()
    val saveMemo = _saveMemo.asSharedFlow()

    var currentMemoId = mutableStateOf<Int?>(null)

    val memoTitle = mutableStateOf("")
    val memoContent = mutableStateOf("")
    val memoColor = mutableStateOf(Memo.memoColors.random().toArgb())


    init {
        savedStateHandle.get<Int>("memoId")?.let { memoId ->
            if (memoId != -1) {
                viewModelScope.launch {
                    memoUseCases.getMemo(memoId)?.also { memo ->
                        currentMemoId.value = memo.id
                        memoTitle.value = memo.title
                        memoContent.value = memo.content
                        memoColor.value = memo.color
                    }
                }
            }
        }
    }

    fun saveMemo(title: String, memoContent: String, memoColor: Int) {
        viewModelScope.launch {
            try {
                memoUseCases.addMemo(
                    Memo(
                        title = title,
                        content = memoContent,
                        timestamp = System.currentTimeMillis(),
                        color = memoColor,
                        id = currentMemoId.value
                    )
                )
                _saveMemo.emit(true)
            } catch (e: InvalidMemoException) {
                _saveMemo.emit(false)
            }
        }
    }

}