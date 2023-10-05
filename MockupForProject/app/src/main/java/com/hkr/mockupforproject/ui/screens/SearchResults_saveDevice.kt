package com.hkr.mockupforproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
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
import androidx.compose.ui.text.input.ImeAction
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
fun SearchResult_saveDevicePreview() {
    SearchResult_saveDevice(appViewModel = viewModel())
}

@Composable
fun SearchResult_saveDevice(
    appViewModel: AppViewModel,
    navController: NavHostController = rememberNavController()
) {

    Column(modifier = Modifier.padding(40.dp)) {
        Text(
            text = "Save device",
            modifier = Modifier.padding(bottom = 14.dp),
            fontSize = 28.sp,
            fontWeight = FontWeight(700),
            color = Color.Black
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
        )

        var text1 by remember { mutableStateOf("") }
        OutlinedTextField(
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth(),
            value = text1,
            onValueChange = { text1 = it },
            label = { Text(text = "Name") },
            supportingText = { Text(text = "Name this device") }
        )
        Spacer(modifier = Modifier.size(10.dp))

        var text2 by remember { mutableStateOf("") }
        OutlinedTextField(
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth(),
            value = text2,
            onValueChange = { text2 = it },
            label = { Text(text = "Name") },
            supportingText = { Text(text = "Name this device") }
        )

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
                navController.navigate("SearchResults_updatePriority");
                appViewModel.currentDeviceToSave.deviceName = text1;
                appViewModel.currentDeviceToSave.deviceDescription = text2
                      },
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
            onClick = {
                appViewModel.searchInfo =
                    !appViewModel.searchInfo; navController.navigate("SearchResults")
            }
        ) {
            Text(
                text = "Skip",
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}