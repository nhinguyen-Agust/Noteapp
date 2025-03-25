package com.example.noteapp

import androidx.compose.ui.graphics.Color

// Màu sắc theme
val DarkBlue = Color(0xFF020B11)
val Orange = Color(0xFF54AAE0)
val White = Color(0xFFC2B016)

// Data class cho Note
data class Note(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val userId: String = ""
)

// Sealed class để biểu diễn kết quả
sealed class OperationResult {
    object Success : OperationResult()
    data class Failure(val exception: Exception) : OperationResult()
}