package com.alexzaitsev.fetchtest.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexzaitsev.fetchtest.data.repository.ItemsRepository
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {

    private val _state = MutableStateFlow(MainState.Initial)
    val state: StateFlow<MainState> = _state

    private var loadingJob: Job? = null
    private val isDataLoaded: Boolean
        get() = state.value.items.isNotEmpty() && state.value.error == null

    override fun onCleared() {
        super.onCleared()
        loadingJob?.cancel()
    }

    fun loadData() {
        if (isDataLoaded) {
            return
        }
        loadingJob?.cancel()
        loadingJob = viewModelScope.launch {
            _state.value = MainState.Initial

            itemsRepository.getItems().fold({ items ->
                val filtered = items.mapNotNull { dataItem ->
                    val name = dataItem.name
                    if (name.isNullOrEmpty()) {
                        null
                    } else {
                        MainItem(
                            id = dataItem.id,
                            listId = dataItem.listId,
                            name = name
                        )
                    }
                }.sortedWith(compareBy({ it.listId }, { it.name }))

                _state.value = MainState(
                    isLoading = false,
                    items = filtered.toImmutableList(),
                    error = null
                )
            }, { ex ->
                _state.value = MainState(
                    isLoading = false,
                    items = emptyList<MainItem>().toImmutableList(),
                    error = ex.message
                )
            })
        }
    }
}
