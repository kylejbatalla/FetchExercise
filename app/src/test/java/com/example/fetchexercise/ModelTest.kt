package com.example.fetchexercise

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`

class ModelTest {

    private lateinit var model: Contract.Model
    private lateinit var testDispatcher: TestDispatcher
    private lateinit var testScope: TestScope

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        testDispatcher = StandardTestDispatcher()
        testScope = TestScope(testDispatcher)
        Dispatchers.setMain(testDispatcher)
        model = Model()
    }

    @Test
    fun fetchItemsReturnsCorrectListSizeOnSuccessfulResponse() = testScope.runTest {
        val items = model.fetchItems()
        val itemsSize = 1000
        assertEquals(items.size, itemsSize)
    }

    @Test(expected = Exception::class)
    fun fetchItemsThrowsExceptionOnURLError() = testScope.runTest {
        `when`(Constants.BASE_URL).thenReturn("http://fakeurl.com")
        model.fetchItems()
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