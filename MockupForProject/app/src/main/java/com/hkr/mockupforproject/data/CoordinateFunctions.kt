package com.hkr.mockupforproject.data

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

fun deltaLat(Lat: Float): Double {
    return Lat / 111000.0
}

fun deltaLon(Lon: Float, phoneLat: Float): Double {
    return Lon / (111000.0 * cos(Math.toRadians(phoneLat.toDouble())))
}
fun haversineDistance(p1_lat: Double, p1_lon: Double, p2_lat: Double, p2_lon: Double): Double {
    val lat1 = Math.toRadians(p1_lat)
    val lon1 = Math.toRadians(p1_lon)
    val lat2 = Math.toRadians(p2_lat)
    val lon2 = Math.toRadians(p2_lon)

    val dLat = lat2 - lat1
    val dLon = lon2 - lon1

    val a = sin(dLat / 2).pow(2.0) +
            cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2.0)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    val R = 6371000 // Earth's radius in meters
    return R * c
}