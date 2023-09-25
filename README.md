# IMEI-Scanner-App
Welcome to our repository!

Here we build an app for Assimilatus during the Mobile Development Course (HT2023).

The app should be able to scan IMEI using barcodes, scanning the number directly or entering the IMEI manually.

Then, the selected device will be scanned to evaluate which type of mobile connection is used (3G/4G/5G), to then give a recommendation if an upgrade is possible/needed.

# Important Links
Jira project:
https://eno-maet.atlassian.net/jira/software/projects/DAPJ

Material Design:
https://m3.material.io/

Canvas course page:
https://hkr.instructure.com/courses/6287

Android Dev.:
https://developer.android.com/



# Feature List
* Concerning app device
  * Checking available LTE technologies (3G, 4G and 5G etc)
  * Network Scanning (detecting signal strength to the device)
  * Provide some feedback (information)
    * E.g upgrade device or buy new one
    * Use other alternative e.g loRa
    * If signal is weak? Set up a repeater? Change location?
* Concerning other devices
  * Ability to scan devices with either.
    * Bar code Scanner
    * Digit recognition
  * Local Database (for storing scanned devices)
  * Matching against existing device databases

## Checking available LTE technologies (3G, 4G and 5G etc)
Using the telephony manager of Android. E.g with getDataNetworkType() we can find out if the device is using a 3G network (like UMTS or HSDPA) or a 4G network (like LTE) or even 5G (like NR). We could also use getIMEI() and match the device against a database to see what technologies it supports. 

## Network Scanning (detecting signal strength to the device)
getSignalStrength() can get us the signal strength of the current connected network. Getting it for other providers might be difficult. 

The action of switching between different cellular networks (like from Telia to Telenor) is a user-privileged action. It's designed this way to prevent malicious apps from controlling a device's cellular connection or causing unintended roaming charges. 

Another approach would be to get the device location using the telephonymanager and then checking it against a Mobile Operator coverage service like https://opencellid.org/#zoom=16&lat=37.77889&lon=-122.41942

## Bar code Scanner and Digit Recognition
Using Googles ML-kit. Its free to use and seems easy to integrate.

https://developers.google.com/ml-kit/vision/barcode-scanning/android

https://developers.google.com/ml-kit/vision/text-recognition/v2

## Local Database
The app needs a small local storage so that the user can save some information of each scanned device. We opted for SQLite relational database. 

https://developer.android.com/codelabs/basic-android-kotlin-compose-sql?hl=en#0

Matching against existing device databases 

# Grading

## Basic functions
*	Designed an application.
*	Implemented a basic database able to store device information.
*	Checking available LTE technologies by fetching IMEI of device. (App device)
*	Entering IMEI manually and checking against databases. (Other devices)

## More Advanced
*	Detecting signal strength of different network and providers.
*	Bar code scanning.
*	IMEI scanning.

## Contributors
* Per Magnusson
* Joel Andersson
* Valon Beka
* Sathwik Kannam
* Michel Jensen
