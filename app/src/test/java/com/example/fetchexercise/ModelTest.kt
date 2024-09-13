package com.example.fetchexercise

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`

class ModelTest {

    private lateinit var model: Model

    @Before
    fun setup() {
        model = Model()
    }

    @Test
    fun fetchItemsReturnsCorrectListSizeOnSuccessfulResponse() = runBlocking {
        val items = model.fetchItems()
        val itemsSize = 1000
        assertEquals(items.size, itemsSize)
    }

    @Test(expected = Exception::class)
    fun fetchItemsThrowsExceptionOnURLError() {
        `when`(Constants.BASE_URL).thenReturn("http://fakeurl.com")
        runBlocking {
            model.fetchItems()
        }
    }

    @Test
    fun sortAndFormatDataFormatsAndSortsDataCorrectly() {
        val data = listOf(
            JSONEntry(listId = "2", name = "Item B", id = 2),
            JSONEntry(listId = "1", name = "Item A", id = 1),
            JSONEntry(listId = "1", name = "Item C", id = 3),
            JSONEntry(listId = "2", name = "", id = 4), // Should be filtered out
            JSONEntry(listId = "1", name = null, id = 5) // Should be filtered out
        )
        val expectedResult = "List Id: 1, Name: Item A, Id:1\n" +
                "List Id: 1, Name: Item C, Id:3\n" +
                "List Id: 2, Name: Item B, Id:2"

        val result = model.sortAndFormatData(data)
        assertEquals(expectedResult, result)
    }
}