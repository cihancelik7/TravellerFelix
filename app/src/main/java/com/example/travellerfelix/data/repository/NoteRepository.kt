package com.example.travellerfelix.data.repository

import com.example.travellerfelix.data.local.dao.NoteDao
import com.example.travellerfelix.data.local.model.Note
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao:NoteDao) {

    fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()
    suspend fun insertNote(note:Note) =noteDao.insertNote(note)
    suspend fun updateNote(note:Note) = noteDao.updateNote(note)
    suspend fun deleteNote(note:Note) = noteDao.deleteNote(note)

}