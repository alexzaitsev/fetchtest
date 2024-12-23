package com.alexzaitsev.fetchtest.ui.component.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope

@Composable
fun OneTime(block: suspend CoroutineScope.() -> Unit) {
    LaunchedEffect(Unit) {
        block()
    }
}
