package com.hkr.mockupforproject.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import com.hkr.mockupforproject.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions

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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.hkr.mockupforproject.ui.AppViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    navController: NavHostController = rememberNavController(),
    appViewModel: AppViewModel
) {
    var bottomSheetExpand by remember { mutableStateOf(false) }

    Box {
        val imagePath = painterResource(id = R.drawable.bg_blue_x2)
        Image(
            painter = imagePath,
            contentDescription = "null",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        LargeFloatingActionButton(
            onClick = {appViewModel.searchInfo = !appViewModel.searchInfo},
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .padding(bottom = 70.dp),
            containerColor = Color(0xFF3654F4)
        ) {
            Icon(painter = painterResource(id = R.drawable.scan),
                contentDescription = "",
                tint = Color.White)
        }
        FloatingActionButton(
            onClick = {navController.navigate("SavedDevices")},
            modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .padding(bottom = 89.dp, end = 40.dp),
            containerColor = Color.White

        ) {
            Icon(
                painter = painterResource(id = R.drawable.bookmark),
                contentDescription = "",
                tint = Color.Black
            )
        }

        Column {
            Column (modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(all = 40.dp), verticalArrangement = Arrangement.Top) {
                Image(modifier = Modifier.size(80.dp), painter = painterResource(id = R.drawable.assimilatus_logo_blue_), contentDescription = "")
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp))
                Text(text = "IMEI Scanning application",
                    fontWeight = FontWeight(900),
                    lineHeight = 51.sp,style = MaterialTheme.typography.headlineLarge,
                    fontSize = 50.sp,
                    color = Color(0xFF2F2E51)
                )

                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp))
                Text(
                    text = stringResource(R.string.startpage_text),
                    //fontWeight = FontWeight(900),
                    lineHeight = 26.sp,style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF2F2E51)
                    //fontSize = 50.sp
                )

            }
            Column (modifier = Modifier
                .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ){
                TextButton(onClick = {
                    bottomSheetExpand = !bottomSheetExpand;
                }) {
                    Text(text = "Enter manually",
                        fontWeight = FontWeight(400),
                        fontSize = 14.sp,
                        color = Color(0xFF2F2E51)
                    )
                }
            }
        }
        if (bottomSheetExpand) {
            ModalBottomSheet(
                onDismissRequest = {bottomSheetExpand = !bottomSheetExpand},
                containerColor = Color.Gray

            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    var text by remember { mutableStateOf("") }
                    OutlinedTextField(
                        shape = RoundedCornerShape(16.dp),
                        value = text,
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = { text = it },
                        label = { Text(text = "Enter IMEI", color = Color.White) },
                        supportingText = { Text(text = "Enter IMEI")},
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                appViewModel.searchInfo = !appViewModel.searchInfo
                                bottomSheetExpand = !bottomSheetExpand

                                //keyboardController?.hide()
                            }

                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                            cursorColor = Color.Black,
                            focusedBorderColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp))
                }
            }
        }
    }
    /*
    BottomSheetScaffold(
        sheetContainerColor = Color.DarkGray,
        modifier = Modifier.fillMaxWidth(),
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                OutlinedTextField(
                    shape = RoundedCornerShape(16.dp),
                    value = "Enter IMEI",
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        cursorColor = Color.Black
                    )
                )
                /*Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp))

                 */
                Text(text = "")
            }
        }
    ) {

    }

     */
}