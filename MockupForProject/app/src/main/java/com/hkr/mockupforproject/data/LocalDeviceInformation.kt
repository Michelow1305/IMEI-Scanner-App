package com.hkr.mockupforproject.data

import com.hkr.mockupforproject.ui.AppViewModel

data class LocalDeviceInformation(
    var iMEI: String = TODO(),
    var brand: String = "NA",
    var model: String = "NA",
    var currentNetwork: String = "NA",
    var networkOperator: String = "NA",
    var signalStrength: Int = TODO(),
    var mCC_mCN: String = TODO(),
    var latitude: Double = TODO(),
    var longitude: Double = TODO()
) {

    /*
    Function name:	logDeviceInformation()
    Inputs:			deviceInformation : MutableMap<String, String>
    Outputs:		Prints all information stored in the device information to the Logcat
    Called by:		FetchDeviceInformation()
    Calls:			NA
    Author:         Joel Andersson
     */
    fun logDeviceInformation() {
        println("-- DEVICE INFORMATION -- ")

        println("Brand: $brand")
        println("Model: $model")
        println("Current Network: $currentNetwork")
        println("Network Operator: $networkOperator")
        println("Latitude: $latitude")
        println("Longitude: $longitude")
        println("IMEI: $iMEI")
        println("MCC and MCN: $mCC_mCN")
        println("Signal Strength: $signalStrength")

        println("-- END -- ")
    }

}




