package com.hkr.mockupforproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hkr.mockupforproject.R
import com.hkr.mockupforproject.ui.AppViewModel
@Composable
fun SearchResult_updatePriority(
    appViewModel: AppViewModel,
    navController: NavHostController = rememberNavController()) {

    Column (modifier = Modifier.padding(40.dp)) {
        Text(
            text = "Update priority",
            modifier = Modifier.padding(bottom = 14.dp),
            fontSize = 28.sp,
            fontWeight = FontWeight(700),
            color = Color.Black
        )
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp))

        Text(text = "Drag the slider to update the priority of the device", color = Color.Black)
        var sliderPosition by remember { mutableStateOf(0f) }
        val interactionSource = remember { MutableInteractionSource() }
        Slider(
            colors = SliderDefaults.colors(thumbColor = Color(0xFF3654F4), activeTrackColor = Color(0xFF3654F4), activeTickColor = Color.White),
            value = sliderPosition,
            onValueChange = {sliderPosition=it},
            steps = 5,
        )
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp))
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3654F4)),
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp),
            onClick = {navController.navigate("SearchResults_finish")},
        ) {
            Text(
                text = "Next",
                color = Color.White,
            )
            Icon(
                Icons.Outlined.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 13.dp)
                    .size(25.dp),
                tint = Color.White
                )
        }
        TextButton(
            modifier = Modifier
                .padding(top = 24.dp)
                .align(Alignment.CenterHorizontally),
            onClick = { appViewModel.searchInfo = !appViewModel.searchInfo; navController.navigate("SearchResults") }
        ) {
            Text(
                text = "Skip",
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}