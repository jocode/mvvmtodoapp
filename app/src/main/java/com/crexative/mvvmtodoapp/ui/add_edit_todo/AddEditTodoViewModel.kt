package com.crexative.mvvmtodoapp.ui.add_edit_todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crexative.mvvmtodoapp.data.Todo
import com.crexative.mvvmtodoapp.domain.TodoRepository
import com.crexative.mvvmtodoapp.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTodoViewModel @Inject constructor(
    private val repository: TodoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var todo by mutableStateOf<Todo?>(null)
        private set

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val todoId = savedStateHandle.get<Int>("todoId")
        if (todoId != -1) {
            getTodoById(todoId!!)
        }
    }

    private fun getTodoById(todoId: Int) {
        viewModelScope.launch {
            repository.getTodoById(todoId)?.let { item ->
                todo = item
                title = item.title
                description = item.description ?: ""
            }
        }
    }

    fun onEvent(event: AddEditTodoEvent) {
        when (event) {
            is AddEditTodoEvent.OnTitleChange -> {

            }
            is AddEditTodoEvent.OnDescriptionChange -> {

            }
            AddEditTodoEvent.OnSaveClick -> {
                viewModelScope.launch {
                    if (title.isBlank()) {
                        sendUiEvent(UiEvent.ShowSnackbar("Title cannot be empty"))
                        return@launch
                    }
                    val newTodo = Todo(
                        title = title,
                        description = description,
                        isDone = todo?.isDone ?: false,
                        id = todo?.id,
                    )
                    repository.insertTodo(newTodo)
                    sendUiEvent(UiEvent.PopBackStack)
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}