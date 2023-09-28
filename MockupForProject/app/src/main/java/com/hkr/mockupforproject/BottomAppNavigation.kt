package com.hkr.mockupforproject

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hkr.mockupforproject.ui.AppViewModel
import com.hkr.mockupforproject.ui.screens.SavedDevices
import com.hkr.mockupforproject.ui.screens.SearchResult
import com.hkr.mockupforproject.ui.screens.SearchResult_finish
import com.hkr.mockupforproject.ui.screens.SearchResult_saveDevice
import com.hkr.mockupforproject.ui.screens.SearchResult_updatePriority
import com.hkr.mockupforproject.ui.screens.StartScreen

@Composable
fun bottomAppNavigation(appViewModel: AppViewModel) {
    val bottomNavController = rememberNavController()

    NavHost(
        navController = bottomNavController,
        startDestination = "SearchResults",
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }) {
        composable("SearchResults") { SearchResult(appViewModel, bottomNavController) }
        composable("SearchResults_saveDevice") {
            SearchResult_saveDevice(
                appViewModel,
                bottomNavController
            )
        }
        composable("SearchResults_updatePriority") {
            SearchResult_updatePriority(
                appViewModel,
                bottomNavController
            )
        }
        composable("SearchResults_finish") {
            SearchResult_finish(
                appViewModel,
                bottomNavController
            )
        }
    }
}