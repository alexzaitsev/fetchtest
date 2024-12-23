package com.alexzaitsev.fetchtest.ui.screen.main

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class MainState(
    val isLoading: Boolean,
    val items: ImmutableList<MainItem>,
    val error: String?
) {
    companion object {
        val Initial: MainState = MainState(
            isLoading = true,
            items = emptyList<MainItem>().toImmutableList(),
            error = null
        )
    }
}

data class MainItem(
    val id: Int,
    val listId: Int,
    val name: String
)
