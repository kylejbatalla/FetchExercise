package com.example.fetchexercise

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * The Model class is responsible for fetching and processing data.
 * It implements the `Contract.Model` interface, providing the data needed by the Presenter.
 */
class Model : Contract.Model {
    private val client = OkHttpClient()
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val listMyDataType = Types.newParameterizedType(List::class.java, JSONEntry::class.java)
    private val jsonAdapter = moshi.adapter<List<JSONEntry>>(listMyDataType)

    /**
     * Fetches a list of JSONEntry items from a remote JSON endpoint.
     *
     * This method performs a network request to retrieve JSON data and parses it into a list of
     * `JSONEntry`.
     *
     * @return A list of `JSONEntry` objects retrieved and parsed from the JSON response.
     * @throws Exception If the network request fails, the response is empty, or JSON parsing fails.
     */
    override suspend fun fetchItems(): List<JSONEntry> {
        return withContext(Dispatchers.IO) {
            val url = "https://fetch-hiring.s3.amazonaws.com/hiring.json"

            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw Exception("Network error: ${response.message()}")
                val responseBody =
                    response.body()?.string() ?: throw Exception("Empty response body")
                jsonAdapter.fromJson(responseBody) ?: throw Exception("Failed to parse JSON")
            }
        }
    }

    /**
     * Sorts and formats a list of JSONEntry items into a human-readable string.
     *
     * This method filters out entries with null or empty names, sorts the remaining entries first
     * by `listId` and then by `name`, and formats them into a single string separated by newlines.
     *
     * @param data The list of `JSONEntry` objects to be sorted and formatted.
     * @return A string representation of the sorted and formatted data.
     */
    override fun sortAndFormatData(data: List<JSONEntry>): String {
        return data
            .filter { it.name != null && it.name != "" }
            .sortedWith(compareBy<JSONEntry> { it.listId }.thenBy { it.name ?: "" })
            .joinToString(separator = "\n") {
                "List ID: ${it.listId}, Name: ${it.name}, Id:${it.id}"
            }
    }
}