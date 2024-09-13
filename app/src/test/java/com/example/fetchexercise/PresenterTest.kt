package com.example.fetchexercise

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class PresenterTest {

    private lateinit var model: Contract.Model
    private lateinit var presenter: Presenter
    private lateinit var view: Contract.View
    private lateinit var testDispatcher: TestDispatcher

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)

        model = mock()
        view = mock()
        presenter = Presenter(view = view, model = model, dispatcher = testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadDataShowDataIsTriggered() = runTest {
        val items = listOf(
            JSONEntry(listId = "2", name = "Item B", id = 2)
        )
        val sortedItems = "List Id: 1, Name: Item A, Id:1"

        `when`(model.fetchItems()).thenReturn(items)
        `when`(model.sortAndFormatData(items)).thenReturn(sortedItems)

        presenter.loadData()

        testDispatcher.scheduler.advanceUntilIdle()

        verify(view).showData(anyString())
    }

    @Test(expected = Exception::class)
    fun loadDataFailedToLoadData() = runTest {
        `when`(model.fetchItems()).thenThrow(Exception())
        presenter.loadData()

        testDispatcher.scheduler.advanceUntilIdle()

        verify(view).showError()
    }
}