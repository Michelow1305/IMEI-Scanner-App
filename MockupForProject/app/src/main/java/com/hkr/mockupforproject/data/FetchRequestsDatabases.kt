package com.hkr.mockupforproject.data

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.hkr.mockupforproject.ui.AppViewModel
import java.io.BufferedReader
import java.io.InputStreamReader
import org.json.JSONObject


/*
Function name:	findModelByTacFromCsv()
Inputs:			context: Context, tacToSearch: String
Function:       Searches CSV for a matching TAC. If found it fetches corresponding
                model name.
Outputs:		String (model name)
Called by:		findModelAndTechnologyByTac
Calls:			NA
Author:         Joel Andersson
 */
fun findModelByTacFromCsv(context: Context, tacToSearch: String): String? {
    val fileName = "taccsv.csv"
    var modelNameFound: String? = null

    try {
        val inputStream = context.assets.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))

        var line: String? = reader.readLine()
        while (line != null) {
            val parts = line.split(",")
            if (parts.size >= 2) {
                val tac = parts[0].trim()
                val modelName = parts[1].trim()

                if (tacToSearch == tac) {
                    modelNameFound = modelName
                    Log.d("Joel Log", "TAC: $tac matches $modelName in CSV database")
                    break
                }
            }
            line = reader.readLine()
        }

        reader.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    if (modelNameFound == null) {
        Log.d(ContentValues.TAG, "TAC not found in CSV: $tacToSearch")
    }

    return modelNameFound
}


/*
Function name:	findModelByTacFromJSON()
Inputs:			context: Context, tacToSearch: String
Function:       Searches JSON for a matching TAC. If found it fetches corresponding
                model name.
Outputs:		String (model name)
Called by:		findModelAndTechnologyByTac
Calls:			NA
Author:         Joel Andersson
 */
fun findModelByTacFromJSON(context: Context, tacToSearch: String): String? {
    val fileName = "tac.json"

    try {
        val inputStream = context.assets.open(fileName)
        val jsonContent = inputStream.bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(jsonContent)
        val brandsObject = jsonObject.getJSONObject("brands")

        for (brand in brandsObject.keys()) {
            val brandObject = brandsObject.getJSONObject(brand)
            val modelsArray = brandObject.getJSONArray("models")

            for (i in 0 until modelsArray.length()) {
                val modelObject = modelsArray.getJSONObject(i)
                val modelName = modelObject.keys().next()
                val tacsArray = modelObject.getJSONObject(modelName).getJSONArray("tacs")

                for (j in 0 until tacsArray.length()) {
                    val tac = tacsArray.optString(j)
                    if (tac == tacToSearch) {
                        Log.d("Joel Log", "TAC: $tac matches $modelName in JSON database")
                        return modelName
                    }
                }
            }
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }

    Log.d(ContentValues.TAG, "TAC not found in JSON: $tacToSearch")
    return null
}


/*
Function name:	findSupportedNetworkByModel()
Inputs:			context: Context, modelNameToSearch: String
Outputs:		MutableMap<String, List<String>>
Function:       Looping through "RECORDS" in the JSON, looking for a matching value of "name":
                Once found it fetches the values for Technology\": and then returns a Key:Value pair
                as Model_Name:ListOf(SupportedTechnologies)
Called by:		findModelAndTechnologyByTac or findTechnologyByModel
Calls:			NA
Author:         Joel Andersson
 */
fun findSupportedNetworkByModel(context: Context, modelNameToSearch: String): MutableMap<String, List<String>> {
    val resultMap = mutableMapOf<String, List<String>>()
    val fileName = "devices.json"

    try {
        val inputStream = context.assets.open(fileName)
        val jsonContent = inputStream.bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(jsonContent)
        val recordsArray = jsonObject.getJSONArray("RECORDS")

        for (i in 0 until recordsArray.length()) {
            val recordObject = recordsArray.getJSONObject(i)
            val name = recordObject.getString("name")

            if (name == modelNameToSearch) {
                Log.d("Joel Log", "$modelNameToSearch found in device database")
                val specificationsStr = recordObject.getString("specifications")
                val specificationsJSON = JSONObject(specificationsStr)
                val technology = specificationsJSON.optString("Technology", "")

                if (technology.isNotEmpty()) {
                    val parsedTechnologies = technology.split(" \\/ ").map { it.trim() }
                    resultMap[name] = parsedTechnologies
                    break
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return resultMap
}


/*
Function name:	findModelAndTechnologyByTac()
Inputs:			context: Context, tac: String
Outputs:		MutableMap<String, List<String>>
Function:       Searches to databases for a matching TAC. If found it takes the corresponding model name
                and searches the device database for what network technologies the device supports.
Called by:		addDevice() in FetchRequestsDatabses.kt
Calls:			findModelByTacFromCsv and findModelByTacFromJSON in FetchRequestsDatabses.kt
Author:         Joel Andersson
 */
fun findModelAndTechnologyByTac(context: Context, tac: String): MutableMap<String, List<String>> {
    var modelName = findModelByTacFromCsv(context, tac)

    if (modelName == null) modelName = findModelByTacFromJSON(context, tac)
    if (modelName == null) return mutableMapOf()

    return findSupportedNetworkByModel(context, modelName)
}


/*
Function name:	findTechnologyByModel()
Inputs:			context: Context, model: String
Outputs:		MutableMap<String, List<String>>
Function:       Searches database for supported network technologies by device model name
Called by:		NA
Calls:			findSupportedNetworkByModel() in FetchRequestsDatabses.kt
Author:         Joel Andersson
 */
fun findTechnologyByModel(context: Context, model: String): MutableMap<String, List<String>> {
    return findSupportedNetworkByModel(context, model)
}


/*
Function name:	addDevice()
Inputs:			viewModel : AppViewModel, context: Context, imei: Long
Function:       Search for device by IMEI and fill in information for database
Outputs:		Boolean
Called by:		NA
Calls:			findModelAndTechnologyByTac() in FetchRequestsDatabses.kt
Author:         Joel Andersson
 */
fun addDevice(viewModel : AppViewModel, context: Context, imei: Long) : Boolean
{
    // Takes the 8 first digits from imei
    val tac = imei.toString().substring(0, 8)

    // Calls function to search devices by TAC
    val result = findModelAndTechnologyByTac(context, tac)

    // If nothing found, return false
    if(result.isEmpty()) return false
    // Else, Save information to currentDeviceToSave in the appViewModel.
    else
    {
        viewModel.currentDeviceToSave.model = result.keys.toList()[0]
        viewModel.currentDeviceToSave.supportedTechnologies = result[result.keys.toList()[0]].toString()

        return true
    }
}



