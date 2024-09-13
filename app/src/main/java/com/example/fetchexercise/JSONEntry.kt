package com.example.fetchexercise

/**
 * Represents a data object for a JSON entry.
 *
 * @property id
 * @property listId
 * @property name
 */
data class JSONEntry(
     val id: Int,
     val listId: String,
     val name: String?
)
