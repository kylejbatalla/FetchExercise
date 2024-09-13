package com.example.fetchexercise

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity

private const val TAG = "MainActivity"

/**
 * Main entry point of the application.
 *
 * Interacts with the Presenter to fetch and display data to the user.
 */
class MainActivity : ComponentActivity(), Contract.View {

    private lateinit var presenter: Presenter
    private lateinit var textViewItems: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        textViewItems = findViewById(R.id.textViewItems)

        presenter = Presenter(this, Model())

        Log.d(TAG, "Loading data from model")
        presenter.loadData()
    }

    /**
     * Called by the Presenter to update the view with the data fetched from the Model.
     * This method sets the text of `textViewItems` to display the data.
     *
     * @param data The data to be displayed in the TextView.
     */
    override fun showData(data: String) {
        textViewItems.text = data
    }

    /**
     * Called by the Presenter to display an error message in the view.
     * This method sets the text of `textViewItems` to show the error message.
     *
     */
    override fun showError() {
        textViewItems.text = getString(R.string.failed_data_load)
    }
}