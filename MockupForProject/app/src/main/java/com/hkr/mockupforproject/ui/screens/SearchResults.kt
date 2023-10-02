package com.hkr.mockupforproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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

@Preview
@Composable
fun SearchResultPreview() {
    SearchResult(appViewModel = viewModel())
}

@Composable
fun SearchResult(
    appViewModel: AppViewModel,
    navController: NavHostController = rememberNavController()
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
            appViewModel.imei = 12381239
            RowTextElement(textLeft = "IMEI", textRight = "345454279843245")
            RowTextElement(textLeft = "Brand", textRight = "Samsung")
            RowTextElement(textLeft = "Model", textRight = "Smart system 1")
            RowTextElement(textLeft = "Current Network", textRight = "3G/ -105dBm")
            RowTextElement(textLeft = "Available Network", textRight = "4G/ -76dBm")
            RowTextElement(textLeft = "Recommendation", textRight = "Upgrade to 4G device")
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
                onClick = { navController.navigate("SearchResults_saveDevice") },
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
fun RowTextElement(textLeft: String, textRight: String) {
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
        Text(
            text = textRight,
            fontWeight = FontWeight(500),
            color = Color.Gray,
        )
    }
}