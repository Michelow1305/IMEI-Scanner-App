package com.hkr.mockupforproject.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import com.hkr.mockupforproject.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imeAnimationTarget
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hkr.mockupforproject.data.SavedDeviceData
import com.hkr.mockupforproject.ui.AppViewModel
import kotlinx.coroutines.launch

@Preview
@Composable
fun startScreenPreview() {
    //StartScreen(appViewModel = viewModel())
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun StartScreen(
    navController: NavHostController = rememberNavController(),
    appViewModel: AppViewModel,
) {
    ////////////////
    Log.d("YES", WindowInsets.imeAnimationTarget.toString())
    ///////////////
    var bottomMenuOption: Int by remember { mutableIntStateOf(1) }
    // Box holding the camera scan button and the saved devices button
    Box {
        val imagePath = painterResource(id = R.drawable.bg_blue_x2)
        Image(
            painter = imagePath,
            contentDescription = "null",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        LargeFloatingActionButton(
            onClick = {
                /*
                    When I click on this button, it opens the bottom sheet, "View this device",
                    so I uncommented this temporarily.
                 */
                //appViewModel.searchInfo = !appViewModel.searchInfo
                navController.navigate("CameraWithBoundedBox")
            },
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .padding(bottom = 70.dp),
            containerColor = Color(0xFF3654F4)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.scan),
                contentDescription = "",
                tint = Color.White
            )
        }
        FloatingActionButton(
            onClick = { navController.navigate("SavedDevices") },
            modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .padding(bottom = 89.dp, end = 40.dp),
            containerColor = Color.White

        )
        {
            Icon(
                painter = painterResource(id = R.drawable.bookmark),
                contentDescription = "",
                tint = Color.Black
            )
        }


        // Main content column of the main screen
        // Hold the logo, intro text etc
        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.92f)
                    .padding(all = 40.dp), verticalArrangement = Arrangement.Top
            ) {
                Image(
                    modifier = Modifier.size(80.dp),
                    painter = painterResource(id = R.drawable.assimilatus_logo_blue_),
                    contentDescription = ""
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                )
                Text(
                    text = "IMEI Scanning application",
                    fontWeight = FontWeight(900),
                    lineHeight = 51.sp, style = MaterialTheme.typography.headlineLarge,
                    fontSize = 50.sp,
                    color = Color(0xFF2F2E51)
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                )
                Text(
                    text = stringResource(R.string.startpage_text),
                    //fontWeight = FontWeight(900),
                    lineHeight = 26.sp, style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF2F2E51)
                    //fontSize = 50.sp
                )

            }

            // Bottom row of the main screen
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Center
            ) {

                TextButton(onClick = {
                    appViewModel.bottomSheetExpand = !appViewModel.bottomSheetExpand
                    bottomMenuOption = 1
                }) {
                    Text(
                        text = "Enter IMEI manually",
                        fontWeight = FontWeight(400),
                        fontSize = 14.sp,
                        color = Color(0xFF2F2E51)
                    )
                }
                TextButton(onClick = {
                    appViewModel.bottomSheetExpand = !appViewModel.bottomSheetExpand
                    bottomMenuOption = 2
                }) {
                    Text(
                        text = "View this phone",
                        fontWeight = FontWeight(400),
                        fontSize = 14.sp,
                        color = Color(0xFF2F2E51)
                    )
                }
            }
        }
        if (appViewModel.bottomSheetExpand) {
            if (bottomMenuOption == 1) {
                Box(modifier = Modifier.align(Alignment.BottomCenter).windowInsetsPadding(
                    WindowInsets.ime)) {
                    EnterIMEIManually(appViewModel = appViewModel)
                }

            } else if (bottomMenuOption == 2) {
                ModalBottomSheet(
                    onDismissRequest = {
                        appViewModel.bottomSheetExpand = !appViewModel.bottomSheetExpand
                    },
                    containerColor = Color.White,
                ) {
                    ViewThisDevice(appViewModel = appViewModel, navController = navController)
                }
            }


        }
    }
}

/*
Function name:	ViewThisDevice()
Inputs:			AppViewModel, NavHostController
Outputs:		Draws local device information on the scaffold sheet
Called by:		StartScreen() if user clicks "view this device"-button
Calls:			LocalDeviceResult() in SearchResults.kt
Author:         Joel Andersson
 */
@Composable
fun ViewThisDevice(
    appViewModel: AppViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    FetchDeviceInformation(appViewModel)
    appViewModel.localDeviceInformation.logDeviceInformation()

    LocalDeviceResult(
        appViewModel = appViewModel, navController = navController,
        networkOperator = appViewModel.localDeviceInformation.networkOperator,
        iMEI = appViewModel.localDeviceInformation.iMEI,
        currentNetwork = appViewModel.localDeviceInformation.currentNetwork,
        model = appViewModel.localDeviceInformation.model,
        signalStrength = appViewModel.localDeviceInformation.signalStrength.toString(),
        latitude = appViewModel.localDeviceInformation.latitude.value,
        longitude = appViewModel.localDeviceInformation.longitude.value
    )

}

/*
Function name:	EnterIMEIManually()
Inputs:			AppViewModel, NavHostController
Outputs:			Draws input field on the scaffold sheet to allow user to enter an IMEI manually
Called by:		StartScreen() if user clicks "Enter IMEI manually"-button
Calls:			SearchResult() in SearchResults.kt
Author:         Joel Andersson & Per Magnusson
 */
@Composable
fun EnterIMEIManually(appViewModel: AppViewModel) {
    Column(modifier = Modifier.padding(start = 0.dp, end = 0.dp, bottom = 0.dp)) {
        var text by remember { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }
        var isError by remember { mutableStateOf(false) }

        TextField(
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            value = text,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            onValueChange = {
                if (it.length <= 15) text = it; if (it.length == 15) isError = false
            },
            isError = isError,
            label = {
                if (!isError) {
                    Text(text = "Enter IMEI", color = Color.Black)
                } else {
                    Text(text = "Please enter 15 digits.", color = Color.Red)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (text.length != 15) {
                        isError = true
                        // Optionally show a toast or some other feedback
                    } else {
                        isError = false
                        appViewModel.bottomSheetExpand = !appViewModel.bottomSheetExpand
                        appViewModel.searchInfo = !appViewModel.searchInfo
                        appViewModel.currentDeviceToSave.imei = text.toLong()
                        //keyboardController?.hide()
                    }
                }

            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                cursorColor = Color.Black,
            )
        )

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

    }
}
