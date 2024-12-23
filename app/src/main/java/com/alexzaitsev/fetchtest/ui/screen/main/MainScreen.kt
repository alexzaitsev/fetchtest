package com.alexzaitsev.fetchtest.ui.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexzaitsev.fetchtest.ui.component.util.OneTime
import kotlinx.collections.immutable.ImmutableList
import my.nanihadesuka.compose.LazyColumnScrollbar
import my.nanihadesuka.compose.ScrollbarSettings
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<MainViewModel>()
    OneTime { viewModel.loadData() }
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading -> StateIsLoading()
            state.error != null -> state.error?.let { StateError(it) { viewModel.loadData() } }
            state.items.isEmpty() -> StateEmptyList()
            else -> StateList(items = state.items)
        }
    }
}

@Composable
private fun StateIsLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun StateError(error: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = "Error happened: $error"
        )
        Spacer(modifier = Modifier.size(10.dp))
        OutlinedButton(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}

@Composable
private fun StateEmptyList() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("There are no items.")
    }
}

@Composable
private fun StateList(items: ImmutableList<MainItem>) {
    val listState = rememberLazyListState()
    LazyColumnScrollbar(
        state = listState,
        settings = ScrollbarSettings.Default
    ) {
        LazyColumn(state = listState) {
            items(items = items, key = { item -> item.id }) { item ->
                Item(name = item.name)
            }
        }
    }
}

@Composable
private fun Item(name: String) {
    Box(modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                text = name
            )
        }
    }
}
