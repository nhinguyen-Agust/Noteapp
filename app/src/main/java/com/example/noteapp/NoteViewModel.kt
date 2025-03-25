package com.example.noteapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NoteViewModel : ViewModel() {
    private val repository = NoteRepository()
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadNotes(userId: String) {
        viewModelScope.launch {
            _notes.value = repository.getNotes(userId)
        }
    }

    fun addNote(title: String, description: String, userId: String) {
        viewModelScope.launch {
            val newNote = Note(title = title, description = description, userId = userId)
            when (val result = repository.addNote(newNote)) {
                is OperationResult.Success -> {
                    loadNotes(userId)
                    _errorMessage.value = null
                }
                is OperationResult.Failure -> {
                    _errorMessage.value = "Failed to add note: ${result.exception.message}"
                }
            }
        }
    }

    fun updateNote(note: Note, userId: String) {
        viewModelScope.launch {
            when (val result = repository.updateNote(note)) {
                is OperationResult.Success -> {
                    loadNotes(userId)
                    _errorMessage.value = null
                }
                is OperationResult.Failure -> {
                    _errorMessage.value = "Failed to update note: ${result.exception.message}"
                }
            }
        }
    }

    fun deleteNote(noteId: String, userId: String) {
        viewModelScope.launch {
            when (val result = repository.deleteNote(noteId)) {
                is OperationResult.Success -> {
                    loadNotes(userId)
                    _errorMessage.value = null
                }
                is OperationResult.Failure -> {
                    _errorMessage.value = "Failed to delete note: ${result.exception.message}"
                }
            }
        }
    }
}