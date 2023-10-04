package com.hkr.mockupforproject

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hkr.mockupforproject.ui.AppViewModel
import com.hkr.mockupforproject.ui.screens.SavedDevices
import com.hkr.mockupforproject.ui.screens.StartScreen

@Composable
fun mainAppNavigation(appViewModel: AppViewModel) {
    val MainNavController = rememberNavController()

    NavHost(navController = MainNavController, startDestination = "StartScreen") {
        composable("StartScreen") { StartScreen(MainNavController, appViewModel)}
        composable("SavedDevices") { SavedDevices(appViewModel,MainNavController)}
    }
}


@Preview
@Composable
fun mainAppNavigationPreview()
{
    mainAppNavigation(appViewModel = viewModel())
}