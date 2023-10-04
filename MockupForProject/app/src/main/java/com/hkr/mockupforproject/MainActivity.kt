package com.hkr.mockupforproject

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.hkr.mockupforproject.data.AppDatabase
import com.hkr.mockupforproject.data.AppRepository
import com.hkr.mockupforproject.data.haversineDistance
import com.hkr.mockupforproject.ui.AppViewModel
import com.hkr.mockupforproject.ui.AppViewModelFactory
import com.hkr.mockupforproject.ui.theme.MockupForProjectTheme
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hkr.mockupforproject.ui.screens.FetchDeviceInformation
import com.hkr.mockupforproject.ui.screens.SavedDevices
import com.hkr.mockupforproject.ui.screens.StartScreen
import com.hkr.mockupforproject.ui.theme.MockupForProjectTheme


open class MainActivity : ComponentActivity() {

    private val viewModel: AppViewModel by viewModels()
    companion object {
        const val PHONE_STATE_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database by lazy { AppDatabase.getDatabase(this) }
        val repository by lazy { AppRepository(database.cellTowerDao()) }
        val viewModelFactory = AppViewModelFactory(repository, this)

        val viewModel : AppViewModel by viewModels {
            viewModelFactory
        }

        viewModel.getCellTowersInRange(56.049877F, 14.150383F)
        viewModel.findByCid(208942101)

        // Observe the LiveData in Appviewmodel for permission request
        // If we need more permissions add it to the array below
        viewModel.requestPermission.observe(this, Observer {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_FINE_LOCATION),
                PHONE_STATE_REQUEST_CODE
            )
        })
        viewModel.checkAndAskPermission()
        Log.d(ContentValues.TAG, "Has permission " + viewModel.hasReadPhoneStatePermission(this).toString())
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PHONE_STATE_REQUEST_CODE -> {
                setContent()
                {
                    //val cellTowers by viewModel.findByMncResult.observeAsState(initial = emptyList())
                    val cellTowers by viewModel.cellTowersInRangeResult.observeAsState(initial = emptyList())
                    viewModel.InRangeHasTowers()

                    MockupForProjectTheme
                    {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        )
                        {
                            //AppScreen()

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White, shape = RoundedCornerShape(10.dp))
                                    .height(200.dp)

                            )
                            {
                                items(cellTowers)
                                { step ->
                                    if (viewModel.cellTowersInRangeHasTowers)
                                    {
                                        Log.d(
                                            "",
                                            haversineDistance(
                                                56.049877,
                                                14.150383,
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
                        }
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




