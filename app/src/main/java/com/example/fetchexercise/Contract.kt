package com.example.fetchexercise

/**
 * Contract that defines functionalities for MVP each component.
 */
interface Contract {
    interface Model {
        suspend fun fetchItems(): List<JSONEntry>
        fun sortAndFormatData(data: List<JSONEntry>): String
    }

    interface View {
        fun showData(data: String)
        fun showError(message: String)
    }

    interface Presenter {
        fun loadData()
    }
}