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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.hkr.mockupforproject.data.SavedDevice
import com.hkr.mockupforproject.data.addDevice
import com.hkr.mockupforproject.data.haversineDistance
import com.hkr.mockupforproject.ui.AppViewModel
import kotlinx.coroutines.delay

@Preview
@Composable
fun SearchResultPreview() {

    SearchResult(appViewModel = viewModel())
}

@Composable
fun SearchResult(
    appViewModel: AppViewModel,
    navController: NavHostController = rememberNavController(),

) {
    appViewModel.getCellTowersInRange(appViewModel.localDeviceInformation.latitude.value.toFloat(), appViewModel.localDeviceInformation.longitude.value.toFloat())
    appViewModel.hasNumberOfTowers()

    if ((appViewModel.currentDeviceToSave.imei>0)&&(appViewModel.currentDeviceToSave.model == "Not defined")) {
        addDevice(viewModel = appViewModel, context = LocalContext.current, imei = appViewModel.currentDeviceToSave.imei)
    }
    getRecommendation(appViewModel = appViewModel)
    
    val scrollState = rememberScrollState()
    var expandAvailableOperators by remember { mutableStateOf(false) }
    var expendRecommendation by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .background(Color.White)) {
        Column(modifier = Modifier
            .padding(top = 30.dp, bottom = 40.dp, start = 40.dp, end = 40.dp)
            .verticalScroll(scrollState)) {
            Text(
                text = "Search results",
                modifier = Modifier.padding(bottom = 14.dp),
                fontSize = 28.sp,
                fontWeight = FontWeight(700),
                color = Color.Black
            )
            RowTextElement(
                textLeft = "My phone network",
                textRight = appViewModel.localDeviceInformation.networkOperator+"/"+appViewModel.localDeviceInformation.currentNetwork+" ",
                elementRight = {SignalStrengthBar(appViewModel.localDeviceInformation.signalStrength)}
            )
            Box(
                modifier = Modifier.clickable(onClick = {expandAvailableOperators=!expandAvailableOperators})
            ) {
                RowTextElement(textLeft = "Nearby cell towers", elementRight = { AnimatedExpandArrow(expanded = expandAvailableOperators) })
            }


            if(expandAvailableOperators || !appViewModel.searchInfo) {
                ShowCellTowers(appViewModel = appViewModel)
            }

            Divider(modifier = Modifier.fillMaxWidth())
            Text(
                text = "Scanned device",
                modifier = Modifier.padding(bottom = 14.dp, top = 14.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight(700),
                color = Color.Black
            )
            RowTextElement(textLeft = "IMEI", textRight = appViewModel.currentDeviceToSave.imei.toString())
            //Not in this version//RowTextElement(textLeft = "Brand", textRight = brand)
            RowTextElement(textLeft = "Model", textRight = appViewModel.currentDeviceToSave.model)
            RowTextElement(textLeft = "Radio support", textRight = appViewModel.currentDeviceToSave.supportedTechnologies)

            Box(
                modifier = Modifier.clickable(onClick = {expendRecommendation=!expendRecommendation})
            ) {
                RowTextElement(textLeft = "Recommendation", elementRight = { AnimatedExpandArrow(expanded = expendRecommendation) })
            }


            if(expendRecommendation || !appViewModel.searchInfo) {
                Text(text = appViewModel.currentDeviceToSave.recommendation)
                getRecommendation(appViewModel = appViewModel)

            }

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
                    appViewModel.currentDeviceToSave.recommendation = "Upgrade"
                    navController.navigate("SearchResults_saveDevice");
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

@Composable
fun getRecommendation(appViewModel: AppViewModel) {
    appViewModel.hasNumberOfTowers()
    val numberOfTowers = appViewModel.numberOfCellTowers
    val hardwareSupport = appViewModel.currentDeviceToSave.supportedTechnologies
    val currentNetwork = appViewModel.localDeviceInformation.currentNetwork
    var recommendation = ""

    val supportedTechnologiesList = hardwareSupport.split(", ").map { it.trim() }
    val currentNetworkList = currentNetwork.split(", ").map { it.trim() }

    if (numberOfTowers == 0) {
        //recommendation = "There is no 4G towers withing 1000m from this location, therefore you can't have any 4G!"
        recommendation = "" +
                "Number of Towers : $numberOfTowers \n" +
                "Hardware Support:  $hardwareSupport \n" +
                "\n" +
                "There is no 4G towers withing 1000m from this location, therefore you can't have any 4G!"
    }

    else if (!currentNetworkList.contains("4G")) {
        recommendation =  "" +
                "Number of Towers : $numberOfTowers \n" +
                "Hardware Support:  $hardwareSupport \n" +
                "\n" +
                "You are not covered by 4G network right now"
    }

    else if (!currentNetworkList.contains("4G") && numberOfTowers >= 1) {
        recommendation =  "" +
                "Number of Towers : $numberOfTowers \n" +
                "Hardware Support:  $hardwareSupport \n" +
                "\n" +
                "You are not covered by 4G network right now. \n" +
                " However there are $numberOfTowers in your area" +
                "so we recommmend to use a repeater to help you get some access on 4g to your current location. \n" +
                "For more info about repaters check YouTube :D "
    }

    else if (numberOfTowers >= 1 && supportedTechnologiesList.contains("LTE")) {
        recommendation =   "" +
                "Number of Towers : $numberOfTowers \n" +
                "Hardware Support:  $hardwareSupport \n" +
                "\n" +
                "Your Hardware support 4G and there are $numberOfTowers 4g towers withing 1000m from your location "
    }

    else if (numberOfTowers >= 1 && !supportedTechnologiesList.contains("LTE")) {
        recommendation =   "" +
                "Number of Towers : $numberOfTowers \n" +
                "Hardware Support:  $hardwareSupport \n" +
                "\n" +
                "Your Hardware doesn't support 4G but there are $numberOfTowers 4g towers withing 1000m from your location.\n" +
                "Please upgrade your device"
    }

    appViewModel.currentDeviceToSave.recommendation = recommendation
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
                text = "Phone information",
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
            .padding(top = 10.dp, bottom = 10.dp)
    ) {
        Text(
            text = textLeft,
            fontWeight = FontWeight(500),
            color = Color.Black,
            modifier = Modifier.weight(1.2f) // Give it a weight so it takes up only the space it needs
        )
        Row(
            modifier = Modifier.weight(1f), // This will ensure it takes up the remaining space
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = textRight,
                fontWeight = FontWeight(500),
                color = Color.Gray,
                maxLines = 2, // or any number you prefer
            )
            elementRight()
        }
    }

}

@Composable
fun ShowCellTowers(
    appViewModel: AppViewModel,
    latitude: Double = 0.0,
    longitude: Double = 0.0

) {
    FetchDeviceInformation(appViewModel = appViewModel)

    val cellTowersList: List<CellTower>? by appViewModel.cellTowersInRangeResult.observeAsState()

    var localDeviceLatitude by remember { mutableStateOf(0.0) }
    var localDeviceLongitude by remember { mutableStateOf(0.0) }

    if (latitude != 0.0) {
        localDeviceLongitude = longitude
        localDeviceLatitude = latitude
    } else {
        localDeviceLatitude = appViewModel.localDeviceInformation.latitude.value
        localDeviceLongitude = appViewModel.localDeviceInformation.longitude.value
    }

    appViewModel.getCellTowersInRange(localDeviceLatitude.toFloat(),localDeviceLongitude.toFloat())


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
                            Text(text = (index+1).toString()+". ", color = Color.Black)
                            Text(text = step.mnc.toString()+"/", color = Color.Black)
                            Text(text = step.radio.toString(), color = Color.Black)
                        }
                        Text(text = "Distance: "+theDistance+"m", color = Color.Black)
                    }
                }
            }
        }

    } else {
        Text(text = "List is null")
        // Handle the case when the list is null, e.g., show a loading indicator or a message
    }


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
