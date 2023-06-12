package com.crexative.mvvmtodoapp.ui.todo_list

import com.crexative.mvvmtodoapp.data.Todo

sealed class TodoListEvent {
    data class DeleteTodoClick(val todo: Todo): TodoListEvent()
    data class OnDoneChange(val todo: Todo, val isDone: Boolean): TodoListEvent()
    object OnUndoDeleteClick: TodoListEvent()
    data class OnTodoClick(val todo: Todo): TodoListEvent()
    object OnAddTodoClick: TodoListEvent()
}