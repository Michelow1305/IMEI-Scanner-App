package com.hkr.mockupforproject

import android.Manifest
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import com.hkr.mockupforproject.data.AppDatabase
import com.hkr.mockupforproject.data.AppRepository
import com.hkr.mockupforproject.data.haversineDistance
import com.hkr.mockupforproject.ui.AppViewModel
import com.hkr.mockupforproject.ui.AppViewModelFactory
import com.hkr.mockupforproject.ui.theme.MockupForProjectTheme
import androidx.core.app.ActivityCompat
import com.hkr.mockupforproject.ui.screens.FetchDeviceInformation
import androidx.compose.runtime.livedata.observeAsState



class MainActivity : ComponentActivity() {

    private val database by lazy { AppDatabase.getDatabase(this) }
    private val repository by lazy { AppRepository(database.cellTowerDao()) }
    private val viewModelFactory by lazy { AppViewModelFactory(repository, this) }

    private val viewModel: AppViewModel by viewModels { viewModelFactory }
    companion object {
        const val PHONE_STATE_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(ContentValues.TAG, "Created viewmodel")

        Log.d(ContentValues.TAG, viewModel.localDeviceInformation.latitude.toString() + " " + viewModel.localDeviceInformation.longitude.toString())

        println(viewModel.localDeviceInformation.latitude)
        println(viewModel.localDeviceInformation.longitude)

        Thread.sleep(500)

        viewModel.getCellTowersInRange(viewModel.localDeviceInformation.latitude.value.toFloat(), viewModel.localDeviceInformation.longitude.value.toFloat())
        viewModel.findByCid(208942101)

        // Observe the LiveData in Appviewmodel for permission request
        // If we need more permissions add it to the array below
        viewModel.requestPermission.observe(this, Observer
        {
            Log.d(ContentValues.TAG, "Requesting Permission")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                PHONE_STATE_REQUEST_CODE
            )
        })
        viewModel.checkAndAskPermission()
        Log.d(
            ContentValues.TAG,
            "Has permission " + viewModel.hasReadPhoneStatePermission(this).toString()
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        Log.d(ContentValues.TAG, "Entered onRequestPermissionResult")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PHONE_STATE_REQUEST_CODE)
        {
            setContent {
                //val cellTowers by viewModel.findByMncResult.observeAsState(initial = emptyList())
                //val cellTowers by viewModel.cellTowersInRangeResult.observeAsState(initial = emptyList())
                //viewModel.InRangeHasTowers()

                MockupForProjectTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    )
                    {
                        // Fetches local device information
                        FetchDeviceInformation(viewModel)
                        AppScreen(viewModel)
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MockupForProjectTheme {
        //StartScreen()
    }
}


/*
                        LazyColumn(
                            modifier = Modifier
                                  .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(10.dp))
                                .height(200.dp)
                        )
                        {
                            items(cellTowers)
                            { step ->
                                if (viewModel.cellTowersInRangeHasTowers) {
                                    Log.d(
                                        "",
                                        haversineDistance(
                                            viewModel.localDeviceInformation.latitude,
                                            viewModel.localDeviceInformation.longitude,
                                            step.latitude!!.toDouble(),
                                            step.longitude!!.toDouble()
                                        ).toString()
                                    )
                                }
                                Text(
                                    text = step.toString()
                                )
                            }
                        }
 */

