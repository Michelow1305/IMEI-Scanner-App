package com.hkr.mockupforproject.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hkr.mockupforproject.data.CellTower
import com.hkr.mockupforproject.data.SavedDeviceData
import com.hkr.mockupforproject.ui.AppViewModel
import kotlinx.coroutines.delay

@Preview
@Composable
fun SearchResult_finishPreview() {
    SearchResult_finish(appViewModel = viewModel())
}

@Composable
fun SearchResult_finish(
    appViewModel: AppViewModel,
    navController: NavHostController = rememberNavController()
) {
    // State to track whether navigation should occur
    var shouldNavigate by remember { mutableStateOf(false) }
    var savedDeviceData = SavedDeviceData(
        imei = appViewModel.currentDeviceToSave.imei,
        deviceDescription =  appViewModel.currentDeviceToSave.deviceDescription,
        brand = appViewModel.currentDeviceToSave.brand,
        model = appViewModel.currentDeviceToSave.model,
        recommendation = appViewModel.currentDeviceToSave.recommendation,
        deviceName = appViewModel.currentDeviceToSave.deviceName,
        priority = appViewModel.currentDeviceToSave.priority,
        checked = false

    )
    appViewModel.upsertSavedDevice(savedDeviceData)
    Log.d("Device to save",appViewModel.currentDeviceToSave.toString())
    // Use LaunchedEffect to trigger navigation after 1000ms
    LaunchedEffect(shouldNavigate) {
        if (shouldNavigate) {
            // Simulate navigation to the next composable
            delay(2000)
            // Replace this with your actual navigation logic
            // For example, navigate to another screen or composable
            // Replace the lambda below with your navigation logic
            navController.navigate("SearchResults")
            appViewModel.searchInfo = !appViewModel.searchInfo
        }
    }

    // When this composable is opened, set shouldNavigate to true
    shouldNavigate = true

    Box(modifier = Modifier.fillMaxSize()) {
        Icon(
            Icons.Default.Check, contentDescription = "", tint = Color.Black, modifier = Modifier
                .size(60.dp)
                .align(
                    Alignment.Center
                )
        )
    }
}
