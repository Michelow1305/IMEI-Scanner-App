package com.hkr.mockupforproject.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.TextButton
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hkr.mockupforproject.R
import com.hkr.mockupforproject.ui.AppViewModel

@Preview
@Composable
fun SavedDevicesPreview() {
    SavedDevices(appViewModel = viewModel())
}

@Composable
fun SavedDevices(
    appViewModel: AppViewModel,
    navController: NavHostController = rememberNavController()
) {
    var showCard = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {

        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 24.dp)
                .verticalScroll(scrollState)
        ) {
            TextButton(
                modifier = Modifier.padding(start = 10.dp),
                onClick = { navController.navigate("StartScreen") }) {
                Icon(
                    Icons.Default.KeyboardArrowLeft,
                    contentDescription = "",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Text(text = "Back", color = Color.Gray, fontSize = 16.sp)
            }
            Text(
                text = "Saved Devices",
                fontSize = 28.sp,
                fontWeight = FontWeight(700),
                modifier = Modifier.padding(start = 18.dp, bottom = 15.dp, top = 47.dp),
                color = Color.Black
            )

            for (i in 1..5) {
                listObjectMaker(TextDecoration.None, false, showCard)
            }
            Divider(modifier = Modifier.padding(top = 30.dp, bottom = 30.dp))
            Text(
                text = "Updated Devices",
                fontSize = 20.sp,
                fontWeight = FontWeight(400),
                modifier = Modifier.padding(start = 18.dp, bottom = 15.dp),
                color = Color.Black
            )
            for (i in 1..3) {
                listObjectMaker(TextDecoration.LineThrough, true, showCard)
            }
        }
    }
    moreInfoCard(showCard = showCard)
}

@Composable
fun imeiInformation() {
    Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 50.dp)) {
        Text(
            text = "IMEI Information",
            modifier = Modifier.padding(bottom = 14.dp),
            fontSize = 28.sp,
            fontWeight = FontWeight(700),
            color = Color.Black
        )
        RowTextElement(textLeft = "Name", textRight = "This is the name")
        RowTextElement(textLeft = "Information", textRight = "Phone is in this location")
        RowTextElement(textLeft = "Priority", textRight = "5 stars")
        Divider(modifier = Modifier.padding(top = 30.dp, bottom = 30.dp))
        RowTextElement(textLeft = "IMEI", textRight = "345454279843245")
        RowTextElement(textLeft = "Brand", textRight = "Samsung")
        RowTextElement(textLeft = "Model", textRight = "Smart system 1")
        RowTextElement(textLeft = "Current Network", textRight = "3G/ -105dBm")
        RowTextElement(textLeft = "Available Network", textRight = "4G/ -76dBm")
        RowTextElement(textLeft = "Recommendation", textRight = "Upgrade to 4G device")
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun moreInfoCard(showCard: MutableState<Boolean>) {
    AnimatedVisibility(
        visible = showCard.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .background(Color.Black.copy(0.5f))
                .fillMaxSize()
            //.animateEnterExit(enter = fadeIn(), exit = fadeOut())
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.93f)
                    .align(Alignment.Center)
                    .animateEnterExit(
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut() + slideOutVertically()
                    ),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                IconButton(modifier = Modifier
                    .align(Alignment.End)
                    .size(44.dp), onClick = { showCard.value = !showCard.value }) {
                    Icon(Icons.Filled.Close, contentDescription = null)
                }
                imeiInformation()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun listObjectMaker(
    linethroughOrNo: TextDecoration,
    checked: Boolean,
    showCard: MutableState<Boolean>
) {
    Row(modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.CenterVertically) {
        val checkedState = remember { mutableStateOf(checked) }
        Checkbox(
            checked = checkedState.value,
            onCheckedChange = { checkedState.value = it },
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF3654F4),
                checkmarkColor = Color.White
            )
        )
        TextButton(onClick = { showCard.value = !showCard.value }) {
            Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                Text(
                    text = "Sony Ericsson",
                    textDecoration = linethroughOrNo,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Phone at Infanterivägen 16",
                    textDecoration = linethroughOrNo,
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        var expanded by remember { mutableStateOf(false) }
        Box {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = !expanded }) {
                DropdownMenuItem(text = { Text(text = "Delete") }, onClick = { /*TODO*/ })
            }
        }

        BadgedBox(
            badge = {
                Badge {
                    Text(text = "5")
                }
            }) {
            Icon(painter = painterResource(id = R.drawable.star), contentDescription = "star")
        }
    }
}