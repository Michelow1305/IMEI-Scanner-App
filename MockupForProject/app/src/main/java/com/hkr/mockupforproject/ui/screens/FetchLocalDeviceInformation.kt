package com.hkr.mockupforproject.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hkr.mockupforproject.MainActivity

/*
Function name:	FetchDeviceInformation()
Inputs:			None
Outputs:		Fetches local device information
Called by:		ViewThisDevice() if user clicks "View this device"-button
Calls:			Methods of the TelephonyManager
Author:         Joel Andersson
 */
@SuppressLint("MissingPermission", "NewApi")
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun FetchDeviceInformation() :  MutableMap<String, String>
{
    var deviceInformation = mutableMapOf(
        "IMEI" to "",
        "Brand" to "",
        "Model" to "",
        "Current Network" to "",
        "Network Operator" to "",
        "Signal Strength" to "",
        "Available Network" to "",
        "Recommendation" to "")

    val context = LocalContext.current
    val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager

    deviceInformation["Network Operator"] = telephonyManager!!.networkOperatorName
    Log.d(ContentValues.TAG,"Network Operator")


    //deviceInformation["Current Network"] = FetchNetworkType(context, telephonyManager)
    //Log.d(ContentValues.TAG,"Current Network")


    //deviceInformation["IMEI"] = telephonyManager!!.imei
    //Log.d(ContentValues.TAG,"IMEI")


    deviceInformation["Model"] = telephonyManager!!.phoneType.toString()
    Log.d(ContentValues.TAG,"Model")
    deviceInformation["Signal Strength"] = telephonyManager!!.signalStrength.toString()
    Log.d(ContentValues.TAG,"Signal Strength")
    return deviceInformation
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
fun FetchSecureData(context : Context, telephonyManager : TelephonyManager) : String
{

    val networkName = when (telephonyManager!!.networkType) {
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

fun FetchLocalIMEI()
{


}

fun hasReadPhoneStatePermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
}

fun requestReadPhoneStatePermission(activity: MainActivity) {
    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_PHONE_STATE), REQUEST_CODE)
}

override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    when (requestCode) {
        REQUEST_CODE -> {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                FetchSecureData() // Create a data class
            } else {
                // Permission denied, provide alternative functionality or notify user
            }
            return
        }
        else -> {
            // Handle other request codes if needed.
        }
    }
}
