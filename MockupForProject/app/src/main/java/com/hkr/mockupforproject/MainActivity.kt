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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.hkr.mockupforproject.ui.AppViewModel
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
                setContent {
                    MockupForProjectTheme {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            AppScreen(viewModel = viewModel)
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




