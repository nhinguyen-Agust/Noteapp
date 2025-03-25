package com.example.noteapp

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class NoteRepository {
    private val db: FirebaseFirestore = Firebase.firestore
    private val notesCollection = db.collection("notes")

    suspend fun getNotes(userId: String): List<Note> {
        return try {
            val snapshot = notesCollection
                .whereEqualTo("userId", userId)
                .get()
                .await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Note::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun addNote(note: Note): OperationResult {
        return try {
            notesCollection.add(note).await()
            OperationResult.Success
        } catch (e: Exception) {
            OperationResult.Failure(e)
        }
    }

    suspend fun updateNote(note: Note): OperationResult {
        return try {
            note.id.let {
                notesCollection.document(it).set(note).await()
                OperationResult.Success
            }
        } catch (e: Exception) {
            OperationResult.Failure(e)
        }
    }

    suspend fun deleteNote(noteId: String): OperationResult {
        return try {
            notesCollection.document(noteId).delete().await()
            OperationResult.Success
        } catch (e: Exception) {
            OperationResult.Failure(e)
        }
    }
}