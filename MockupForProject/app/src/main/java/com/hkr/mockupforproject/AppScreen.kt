package com.hkr.mockupforproject

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hkr.mockupforproject.ui.AppViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Preview
@Composable
fun AppScreen(viewModel : AppViewModel) {

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
        sheetContent = {
            Box (modifier = Modifier, contentAlignment = Alignment.Center){
                bottomAppNavigation(appViewModel)
            }

        }
    ) {
        mainAppNavigation(appViewModel)
        var observer = appViewModel.searchInfo
        AnimatedVisibility(visible = observer, enter = fadeIn(), exit = fadeOut()) {
            Box(modifier = Modifier
                .background(Color.Black.copy(alpha = 0.5f))
                .fillMaxSize()
            )
        }

        LaunchedEffect(key1 = observer, block = {
            if (observer) {
                sheetScope.launch { sheetScaffoldState.bottomSheetState.expand() }
            } else {
                sheetScope.launch { sheetScaffoldState.bottomSheetState.hide()}
            }
        })
    }
}



