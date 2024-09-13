package com.example.fetchexercise

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "PresenterImpl"

/**
 * Presenter class that acts as mediator between View and Model.
 *
 * Retrieves data from model and updates view.
 *
 * @property view View reference
 */
class Presenter(
    private val view: Contract.View,
    private val model: Contract.Model,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : Contract.Presenter {

    /**
     * Communicates with model to load data and perform business logic. Updates the view
     * based on data retrieved from the model.
     */
    override fun loadData() {
        CoroutineScope(dispatcher).launch {
            try {
                val items = model.fetchItems()
                val sortedItems = model.sortAndFormatData(items)
                view.showData(sortedItems)
            } catch (e: Exception) {
                view.showError()
                Log.d(TAG, "Failed to load items: ${e.message}")
            }
        }
    }
}