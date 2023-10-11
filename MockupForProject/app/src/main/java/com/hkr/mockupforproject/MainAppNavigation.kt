package com.hkr.mockupforproject

import android.content.Context
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hkr.mockupforproject.ui.AppViewModel
import com.hkr.mockupforproject.ui.screens.CameraWithBoundedBox
import com.hkr.mockupforproject.ui.screens.SavedDevices
import com.hkr.mockupforproject.ui.screens.StartScreen

@Composable
fun mainAppNavigation(appViewModel: AppViewModel, context: Context) {
    val MainNavController = rememberNavController()

    NavHost(
        navController = MainNavController,
        startDestination = "StartScreen",
        enterTransition = { EnterTransition.None},
        exitTransition = {ExitTransition.None}

    ) {
        composable("StartScreen") { StartScreen(MainNavController, appViewModel)}
        composable(
            "SavedDevices",
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) { SavedDevices(appViewModel,MainNavController)}
        composable("CameraWithBoundedBox") {CameraWithBoundedBox(context = context, appViewModel = appViewModel, mainNavController = MainNavController)}
    }
}


@Preview
@Composable
fun mainAppNavigationPreview()
{
    //mainAppNavigation(appViewModel = viewModel())
}