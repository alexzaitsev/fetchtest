package com.alexzaitsev.fetchtest.ui.screen.main

import app.cash.turbine.test
import com.alexzaitsev.fetchtest.TestCoroutineRule
import com.alexzaitsev.fetchtest.data.model.Item
import com.alexzaitsev.fetchtest.data.repository.ItemsRepository
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class MainViewModelTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @RelaxedMockK
    private lateinit var itemsRepository: ItemsRepository
    private lateinit var sut: MainViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = spyk(MainViewModel(itemsRepository = itemsRepository))
    }

    @Test
    fun `sut loadData() calls ItemsRepository getItems()`() = runTest {
        coEvery { itemsRepository.getItems() } returns produceDummyItemsList().success()

        sut.loadData()

        coVerify(exactly = 1) { itemsRepository.getItems() }
    }

    @Test
    fun `sut loadData() emits isLoading true when loading starts`() = runTest {
        coEvery { itemsRepository.getItems() } returns produceDummyItemsList().success()
        sut.state.test {

            sut.loadData()

            val expectedState = MainState(
                isLoading = true,
                items = produceEmptyStateList(),
                error = null
            )
            val initialState = awaitItem()
            assertEquals(expectedState, initialState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `sut loadData() emits error when loading fails`() = runTest {
        coEvery { itemsRepository.getItems() } returns Exception("error").failure()
        sut.state.test {

            sut.loadData()
            skipItems(1)

            val expectedState = MainState(
                isLoading = false,
                items = produceEmptyStateList(),
                error = "error"
            )
            val state = awaitItem()
            assertEquals(expectedState, state)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `sut loadData() emits list when loading succeed`() = runTest {
        coEvery { itemsRepository.getItems() } returns produceDummyItemsList().success()
        sut.state.test {

            sut.loadData()
            skipItems(1)

            val expectedState = MainState(
                isLoading = false,
                items = produceDummyMainItemsList(),
                error = null
            )
            val state = awaitItem()
            assertEquals(expectedState, state)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `sut loadData() emits sorted and filtered list when loading succeed`() = runTest {
        coEvery { itemsRepository.getItems() } returns produceFullItemsList().success()
        sut.state.test {

            sut.loadData()
            skipItems(1)

            val expectedState = MainState(
                isLoading = false,
                items = produceFilteredFullMainItemsList(),
                error = null
            )
            val state = awaitItem()
            assertEquals(expectedState, state)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `sut loadData() does not load items twice`() = runTest {
        coEvery { itemsRepository.getItems() } returns produceDummyItemsList().success()
        sut.state.test {

            sut.loadData()
            skipItems(2)

            sut.loadData()
            expectNoEvents()
        }
    }

    companion object {
        private fun produceDummyItemsList() = listOf(
            Item(id = 1, listId = 1, name = "Item 1"),
            Item(id = 2, listId = 2, name = "Item 2"),
        )

        private fun produceFullItemsList() = listOf(
            Item(id = 1, listId = 1, name = "Item 2"),
            Item(id = 2, listId = 1, name = "Item 1"),
            Item(id = 3, listId = 2, name = "Item 1"),
            Item(id = 4, listId = 2, name = "Item 2"),
        )

        private fun produceDummyMainItemsList() = listOf(
            MainItem(id = 1, listId = 1, name = "Item 1"),
            MainItem(id = 2, listId = 2, name = "Item 2"),
        ).toImmutableList()

        private fun produceFilteredFullMainItemsList() = listOf(
            MainItem(id = 2, listId = 1, name = "Item 1"),
            MainItem(id = 1, listId = 1, name = "Item 2"),
            MainItem(id = 3, listId = 2, name = "Item 1"),
            MainItem(id = 4, listId = 2, name = "Item 2"),
        ).toImmutableList()

        private fun produceEmptyStateList() = emptyList<MainItem>().toImmutableList()
    }
}
