package com.alexzaitsev.fetchtest.ui.screen.main

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appScreenMainModule = module {
    viewModelOf(::MainViewModel)
    // other screen-related dependencies go here
}
