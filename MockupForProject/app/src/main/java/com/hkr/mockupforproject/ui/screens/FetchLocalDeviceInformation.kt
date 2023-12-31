package com.hkr.mockupforproject.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
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
fun FetchDeviceInformation(appViewModel : AppViewModel) {

    val context = LocalContext.current
    val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager

    // Getting IMEI and Signal Strength etc, most are only available on (26 < API < 28)
    Log.d(ContentValues.TAG, "Fetching Secure Information")
    fetchSecureInformation(appViewModel, telephonyManager!!, context)

    Log.d(ContentValues.TAG, "Fetching Network Information")
    appViewModel.localDeviceInformation.networkOperator = telephonyManager!!.networkOperatorName
    appViewModel.localDeviceInformation.mCC_mCN = telephonyManager.networkOperator

    Log.d(ContentValues.TAG, "Fetching Manufacturer and Build")
    appViewModel.localDeviceInformation.brand = Build.MANUFACTURER
    appViewModel.localDeviceInformation.model = Build.MODEL

    // Fetching the device location
    Log.d(ContentValues.TAG, "Fetching Location")
    if(appViewModel.hasReadPhoneLocationPermission(context)) {
        fetchLocation(context, appViewModel)
    }
    else Log.d(ContentValues.TAG, "No permission to access location")
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
fun fetchLocation(context: Context, appViewModel: AppViewModel) {

    Log.d("Joel Log", "Fetching Location")

    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager

    var gpsLocation: Location? = null
    var networkLocation: Location? = null

    val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            // Update locations based on provider type
            when (location.provider) {
                LocationManager.GPS_PROVIDER -> gpsLocation = location
                LocationManager.NETWORK_PROVIDER -> networkLocation = location
            }

            // Remove location updates to save battery
            locationManager?.removeUpdates(this)

            determineBestLocationAndUpdateViewModel(gpsLocation, networkLocation, appViewModel)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        // If GPS Provider is enabled, request location update

        if (locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true) {
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null)
            gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            Log.d("Joel Log", "GPS Position Available")
        }

        // If Network Provider is enabled, request location update
        if (locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true) {
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null)
            networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            Log.d("Joel Log", "Network Position Available")
        }

        if (gpsLocation == null && networkLocation == null) {
            Log.d("Joel Log", "Neither GPS nor Network location is available.")
        }
    }
}

fun determineBestLocationAndUpdateViewModel(gpsLocation: Location?, networkLocation: Location?, appViewModel: AppViewModel) {
    val bestLocation: Location? = when {
        gpsLocation == null && networkLocation == null -> null
        gpsLocation == null -> networkLocation
        networkLocation == null -> gpsLocation
        gpsLocation.accuracy <= networkLocation.accuracy -> gpsLocation
        else -> networkLocation
    }

    if (bestLocation != null) {
        Log.d("Joel Log", "Determined Most Accurate Location Provider")
        Log.d("Joel Log", "Latidute: " + bestLocation.latitude.toString() + " Longitude: " + bestLocation.longitude.toString())
        appViewModel.localDeviceInformation.latitude.value = bestLocation.latitude
        appViewModel.localDeviceInformation.longitude.value = bestLocation.longitude
    } else {
        Log.d("Joel Log", "GPS Location Not Available")
        Log.d("Joel Log", "Trying to use tower triangulation instead")
    }
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
fun fetchSecureInformation(appViewModel: AppViewModel, telephonyManager: TelephonyManager, context: Context)
{
    // Getting IMEI, only available on (26 < API < 28)
    if (appViewModel.hasReadPhoneStatePermission(context)) {
        // Getting the current network type
        Log.d(ContentValues.TAG, "Current Network")
        appViewModel.localDeviceInformation.currentNetwork = FetchNetworkType(context, telephonyManager!!)

        // Getting IMEI, require API > 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                if (telephonyManager != null) {
                    appViewModel.localDeviceInformation.iMEI = telephonyManager.imei
                }
            } catch (e: SecurityException) {
                Log.d(ContentValues.TAG, "Security Exception Fetching IMEI")
            }
        } else {
            Log.d(ContentValues.TAG, "Can not fetch IMEI, Incorrect Version")
        }

        // Getting signal strength, require API >28
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            appViewModel.localDeviceInformation.signalStrength =
                telephonyManager.signalStrength!!.level
        else {
            Log.d(ContentValues.TAG, "Can not fetch signal strength, Incorrect Version")
        }

    } else Log.d(ContentValues.TAG, "No permission to access telephone states")
}