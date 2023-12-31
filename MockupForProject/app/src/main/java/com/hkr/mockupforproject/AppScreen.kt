package com.hkr.mockupforproject

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hkr.mockupforproject.ui.AppViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AppScreen(viewModel : AppViewModel, context: Context) {
    Box (modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars)){
        val appViewModel: AppViewModel = viewModel

        val sheetScope = rememberCoroutineScope()
        val bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
        val sheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState)

        BottomSheetScaffold(
            sheetPeekHeight = 0.dp,
            sheetContainerColor = Color.White,
            scaffoldState = sheetScaffoldState,
            sheetSwipeEnabled = true,
            sheetContent = {
                Box(modifier = Modifier.height(620.dp), contentAlignment = Alignment.Center) {
                    bottomAppNavigation(appViewModel)
                }
            }
        ) {
            mainAppNavigation(appViewModel, context = context)

            Log.d("Bottomsheet", (sheetScaffoldState.bottomSheetState.currentValue).toString())
            var observer = appViewModel.searchInfo


            LaunchedEffect(
                key1 = ((sheetScaffoldState.bottomSheetState.currentValue==SheetValue.Expanded)),
                block = { if ((sheetScaffoldState.bottomSheetState.currentValue==SheetValue.Expanded)){appViewModel.searchInfo = true} else {appViewModel.searchInfo=false} })


            AnimatedVisibility(visible = (sheetScaffoldState.bottomSheetState.currentValue==SheetValue.Expanded), enter = fadeIn(), exit = fadeOut()) {
                Box(modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.5f))
                    .fillMaxSize()
                )
            }

            LaunchedEffect(key1 = observer, block = {
                if (observer) {
                    sheetScope.launch { sheetScaffoldState.bottomSheetState.expand() }
                } else {
                    sheetScope.launch { sheetScaffoldState.bottomSheetState.hide() }
                }
            })

        }
    }
}



