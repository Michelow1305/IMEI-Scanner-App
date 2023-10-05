package com.hkr.mockupforproject.ui.screens

import android.content.Context
import android.telephony.CellSignalStrength
import android.util.Log
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hkr.mockupforproject.R
import com.hkr.mockupforproject.data.CellTower
import com.hkr.mockupforproject.data.haversineDistance
import com.hkr.mockupforproject.ui.AppViewModel

@Preview
@Composable
fun SearchResultPreview() {

    SearchResult(appViewModel = viewModel())
}

@Composable
fun SearchResult(
    appViewModel: AppViewModel,
    navController: NavHostController = rememberNavController(),
    iMEI : String = "No information",
    brand : String = "No information",
    model : String = "No information",
    currentNetwork : String = "No information",
    availableNetwork : String = "4G/ -76dBm",
    recommendation : String = "Upgrade to 4G device"

) {
    val scrollState = rememberScrollState()
    var expandAvailableOperators by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .background(Color.White)) {
        Column(modifier = Modifier
            .padding(40.dp)
            .verticalScroll(scrollState)) {
            Text(
                text = "IMEI\nInformation",
                modifier = Modifier.padding(bottom = 14.dp),
                fontSize = 28.sp,
                fontWeight = FontWeight(700),
                color = Color.Black
            )
            Log.d("Currentnetwork", appViewModel.localDeviceInformation.currentNetwork)
            RowTextElement(textLeft = "IMEI", textRight = iMEI)
            RowTextElement(textLeft = "Brand", textRight = brand)
            RowTextElement(textLeft = "Model", textRight = model)
            RowTextElement(
                textLeft = "Current Network",
                textRight = appViewModel.localDeviceInformation.networkOperator+"/"+appViewModel.localDeviceInformation.currentNetwork+" ",
                elementRight = {SignalStrengthBar(appViewModel.localDeviceInformation.signalStrength)}
            )
            Box(
                modifier = Modifier.clickable(onClick = {expandAvailableOperators=!expandAvailableOperators})
            ) {
                RowTextElement(textLeft = "Nearby cell towers...", elementRight = { AnimatedExpandArrow(expanded = expandAvailableOperators) })
            }

            if(expandAvailableOperators || !appViewModel.searchInfo) {
                ShowCellTowers(appViewModel = appViewModel)
            }

            RowTextElement(textLeft = "Recommendation", textRight = recommendation)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3654F4)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp),
                onClick = {
                    navController.navigate("SearchResults_saveDevice");
                    appViewModel.currentDeviceToSave.imei = 123123;
                    appViewModel.currentDeviceToSave.brand = "test1";
                    appViewModel.currentDeviceToSave.model = "Motorola";
                    appViewModel.currentDeviceToSave.recommendation = "Upgrade"
                          },
            ) {
                Text(
                    text = "Save Device",
                    color = Color.White,
                )
                Icon(
                    modifier = Modifier.padding(start = 13.dp),
                    painter = painterResource(id = R.drawable.bookmark),
                    contentDescription = "",
                    tint = Color.White
                )
            }
            TextButton(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = { appViewModel.searchInfo = !appViewModel.searchInfo }
            ) {
                Text(
                    text = "Skip",
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}

/*
Function name:	LocalDeviceResult()
Inputs:			AppViewModel, NavHostController, Device information (optional (provides default values for debug purposes))
Outputs:		Draws local device information
Called by:		ViewThisDevice() if user clicks "View this device"-button
Calls:			None
Author:         Joel Andersson
 */
@Composable
fun LocalDeviceResult(
    appViewModel: AppViewModel,
    navController: NavHostController = rememberNavController(),
    iMEI : String = "345454279843245",
    brand : String = "Samsung",
    model : String = "Smart System 1",
    networkOperator : String = "Telia",
    signalStrength : String = "1",
    currentNetwork : String = "3G/ -105dBm",
    longitude : Double = 0.0,
    latitude : Double = 0.0

) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        Column(modifier = Modifier.padding(40.dp)) {
            Text(
                text = "IMEI\nInformation",
                modifier = Modifier.padding(bottom = 14.dp),
                fontSize = 28.sp,
                fontWeight = FontWeight(700),
                color = Color.Black
            )
            RowTextElement(textLeft = "IMEI", textRight = iMEI)
            RowTextElement(textLeft = "Brand", textRight = brand)
            RowTextElement(textLeft = "Model", textRight = model)
            RowTextElement(textLeft = "Network Operator", textRight = networkOperator)
            RowTextElement(textLeft = "Current Network", textRight = currentNetwork)
            RowTextElement(textLeft = "Signal Strength", textRight = signalStrength)
            RowTextElement(textLeft = "Longitude", textRight = longitude.toString())
            RowTextElement(textLeft = "Latitude", textRight = latitude.toString())
        }
    }
}


@Composable
fun RowTextElement(
    textLeft: String,
    textRight: String ="",
    elementRight: @Composable () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = textLeft,
            fontWeight = FontWeight(500),
            color = Color.Black
        )
        Row {
            Text(
                text = textRight,
                fontWeight = FontWeight(500),
                color = Color.Gray,
            )
            elementRight()
        }

    }
}

@Composable
fun ShowCellTowers(appViewModel: AppViewModel) {
    val localDeviceLatitude by remember { appViewModel.localDeviceInformation.latitude }
    val localDeviceLongitude by remember { appViewModel.localDeviceInformation.longitude }
    val cellTowersList: List<CellTower>? by appViewModel.cellTowersInRangeResult.observeAsState()

    Log.d("LatitudePhone", localDeviceLatitude.toString())
    Log.d("LongitudePhone", localDeviceLongitude.toString())

    appViewModel.getCellTowersInRange(localDeviceLatitude.toFloat(),localDeviceLongitude.toFloat())

    Log.d("DEBUG3", appViewModel.cellTowersInRangeResult.value.toString())

    if (cellTowersList != null) {
        cellTowersList?.let { cellTowers ->
            val sortedCellTowers = cellTowers.sortedBy { step ->
                haversineDistance(
                    localDeviceLatitude,
                    localDeviceLongitude,
                    step.latitude!!.toDouble(),
                    step.longitude!!.toDouble()
                )
            }
            appViewModel.currentDeviceToSave.nearbyTowers = sortedCellTowers
            Log.d("Towers to save", appViewModel.currentDeviceToSave.nearbyTowers.toString())
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.05f))
                    .padding(8.dp)
                    .height(200.dp)
            )
            {
                itemsIndexed(sortedCellTowers)
                { index, step ->
                    val theDistance = haversineDistance(
                        localDeviceLatitude,
                        localDeviceLongitude,
                        step.latitude!!.toDouble(),
                        step.longitude!!.toDouble()
                    ).toInt().toString()
                    Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth())  {
                        Row {
                            Text(text = (index+1).toString()+". ")
                            Text(text = step.mnc.toString()+"/")
                            Text(text = step.radio.toString())
                        }
                        Text(text = "Distance: "+theDistance+"m")
                    }
                }
            }
        }

    } else {
        Text(text = "List is null")
        // Handle the case when the list is null, e.g., show a loading indicator or a message
    }
    FetchDeviceInformation(appViewModel = appViewModel)

}

@Composable
fun AnimatedExpandArrow(expanded: Boolean, modifier: Modifier = Modifier) {
    val rotationDegree by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
    )

    Icon(
        imageVector = Icons.Default.ArrowDropDown,
        contentDescription = null,
        modifier = modifier.rotate(rotationDegree)
    )
}

@Composable
fun SignalStrengthBar(strength: Int) {
    Row(
        modifier = Modifier
            .width(24.dp)
            .height(20.dp)
            .padding(start = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        // Draw n bars
        for (i in 1..4) {
            Box(
                modifier = Modifier
                    .height((i * 4).dp)  // Adjust the height based on the index
                    .width(3.dp)
                    .background(
                        color = if (i <= strength) Color.Green else Color.Gray,
                        shape = RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp)
                    )
            )
        }
    }
}
