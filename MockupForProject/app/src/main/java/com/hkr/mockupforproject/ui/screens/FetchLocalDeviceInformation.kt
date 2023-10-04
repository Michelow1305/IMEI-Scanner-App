package com.hkr.mockupforproject.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hkr.mockupforproject.MainActivity
import com.hkr.mockupforproject.ui.AppViewModel

/*
Function name:	FetchDeviceInformation()
Inputs:			appViewModel : AppViewModel
Outputs:		Fetches local device information
Called by:		ViewThisDevice() if user clicks "View this device"-button
Calls:			Methods of the TelephonyManager
Author:         Joel Andersson
 */

@Composable
fun FetchDeviceInformation(appViewModel : AppViewModel): MutableMap<String, String> {

    val context = LocalContext.current
    val telephonyManager =
        context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager

    var deviceInformation = mutableMapOf(
        "IMEI" to "",
        "Brand" to "",
        "Model" to "",
        "Current Network" to "",
        "Network Operator" to "",
        "Signal Strength" to "",
        "Available Network" to "",
        "Recommendation" to "",
        "MCC and MCN" to "",
        "Latitude" to "",
        "Longitude" to ""
    )

    // Fetching the device location
    if(appViewModel.hasReadPhoneLocationPermission(context)) {
        fetchLocation(context, deviceInformation)
    }
    else Log.d(ContentValues.TAG, "No permission to access location")

    // Getting IMEI and Signal Strength etc, most are only available on (26 < API < 28)
    fetchSecureInformation(appViewModel, telephonyManager!!, context, deviceInformation)

    deviceInformation["Network Operator"] = telephonyManager!!.networkOperatorName
    deviceInformation["MCC and MCN"] = telephonyManager.networkOperator

    deviceInformation["Brand"] = Build.MANUFACTURER
    deviceInformation["Model"] = Build.MODEL

    return deviceInformation
}


/*
Function name:	logDeviceInformation()
Inputs:			deviceInformation : MutableMap<String, String>
Outputs:		Prints all information stored in the device information to the Logcat
Called by:		FetchDeviceInformation()
Calls:			NA
Author:         Joel Andersson
 */
fun logDeviceInformation(deviceInformation : MutableMap<String, String>)
{
    println("-- DEVICE INFORMATION -- ")
    for ((key, value) in deviceInformation) {
        println("$key: $value")
    }
    println("-- END -- ")
}


/*
Function name:	FetchNetworkType()
Inputs:			Context, TelephonyManager
Outputs:		Fetches Network type from the telephony manager (e.g 3G, 2G etc)
Called by:		FetchDeviceInformation()
Calls:			Methods of the TelephonyManager
Author:         Joel Andersson
 */
@SuppressLint("MissingPermission")
fun FetchNetworkType(context : Context, telephonyManager : TelephonyManager) : String
{
    val networkName = when (telephonyManager.networkType) {
        TelephonyManager.NETWORK_TYPE_GPRS,
        TelephonyManager.NETWORK_TYPE_EDGE,
        TelephonyManager.NETWORK_TYPE_CDMA,
        TelephonyManager.NETWORK_TYPE_1xRTT,
        TelephonyManager.NETWORK_TYPE_IDEN -> "2G"

        TelephonyManager.NETWORK_TYPE_UMTS,
        TelephonyManager.NETWORK_TYPE_EVDO_0,
        TelephonyManager.NETWORK_TYPE_EVDO_A,
        TelephonyManager.NETWORK_TYPE_HSDPA,
        TelephonyManager.NETWORK_TYPE_HSUPA,
        TelephonyManager.NETWORK_TYPE_HSPA,
        TelephonyManager.NETWORK_TYPE_EVDO_B,
        TelephonyManager.NETWORK_TYPE_EHRPD,
        TelephonyManager.NETWORK_TYPE_HSPAP -> "3G"

        TelephonyManager.NETWORK_TYPE_LTE -> "4G"

        TelephonyManager.NETWORK_TYPE_NR -> "5G"

        else -> "Unknown"
    }
    return networkName
}


/*
Function name:	FetchLocation()
Inputs:			Context, TelephonyManager
Outputs:		Fetches Network type from the telephony manager (e.g 3G, 2G etc)
Called by:		FetchDeviceInformation()
Calls:			Methods of the TelephonyManager
Author:         Joel Andersson
 */
@SuppressLint("MissingPermission")
fun fetchLocation(context: Context, deviceInformation : MutableMap<String, String>) {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
    val locationProvider = LocationManager.GPS_PROVIDER
    val lastKnownLocation = locationManager?.getLastKnownLocation(locationProvider)

    deviceInformation["Latitude"] = lastKnownLocation?.latitude.toString()
    deviceInformation["Longitude"] = lastKnownLocation?.longitude.toString()
}


/*
Function name:	fetchSecureInformation()
Inputs:			appViewModel: AppViewModel, telephonyManager: TelephonyManager, context: Context, deviceInformation : MutableMap
Outputs:		Fetches Secure Information from the telephony manager (e.g 3G, 2G etc)
Called by:		FetchDeviceInformation()
Calls:			Methods of the TelephonyManager
Author:         Joel Andersson
 */
@SuppressLint("MissingPermission")
fun fetchSecureInformation(appViewModel: AppViewModel, telephonyManager: TelephonyManager, context: Context, deviceInformation : MutableMap<String, String>)
{
    // Getting IMEI, only available on (26 < API < 28)
    if (appViewModel.hasReadPhoneStatePermission(context)) {
        // Getting the current network type
        Log.d(ContentValues.TAG, "Current Network")
        deviceInformation["Current Network"] = FetchNetworkType(context, telephonyManager!!)

        // Getting IMEI, require API > 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                if (telephonyManager != null) {
                    deviceInformation["IMEI"] = telephonyManager.imei
                }
            } catch (e: SecurityException) {
                Log.d(ContentValues.TAG, "Security Exception", e)
            }
        } else {
            Log.d(ContentValues.TAG, "Can not fetch IMEI, Incorrect Version")
            deviceInformation["IMEI"] = "NA"
        }

        // Getting signal strength, require API >28
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            deviceInformation["Signal Strength"] =
                telephonyManager.signalStrength!!.level.toString()
        else {
            Log.d(ContentValues.TAG, "Can not fetch signal strength, Incorrect Version")
            deviceInformation["Signal Strength"] = "NA"
        }

    } else Log.d(ContentValues.TAG, "No permission to access telephone states")
}