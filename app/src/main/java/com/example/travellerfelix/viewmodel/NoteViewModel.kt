package com.example.travellerfelix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travellerfelix.data.local.model.Note
import com.example.travellerfelix.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository):ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    init {
        loadNotes()
    }

    private fun loadNotes(){
        viewModelScope.launch {
            repository.getAllNotes().collectLatest { noteList ->
                _notes.value = noteList
            }
        }
    }

    fun addNote(note:Note){
        viewModelScope.launch {
            repository.insertNote(note)
        }
    }
    fun updateNote(note:Note){
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }
    fun deleteNote(note:Note){
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

}