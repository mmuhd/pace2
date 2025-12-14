package com.cleancall.mz

import org.json.JSONArray
import org.json.JSONObject

data class WasteAggregationReport(
    val id: String,
    val lga: String,
    val wasteSource: String,
    val siteName: String,
    val collectionDate: String,
    val team: String?,
    val totalWasteKg: Double,
    val recyclablePercentage: Int,
    val plasticKg: Double?,
    val paperKg: Double?,
    val metalKg: Double?,
    val glassKg: Double?,
    val organicKg: Double?,
    val otherKg: Double?,
    val finalDisposalSite: String,
    val transportUsed: String?,
    val recyclersInvolved: String?,
    val tripCount: Int?,
    val vehicleCount: Int?,
    val hoursWorked: Double?,
    val avgLoadKg: Double?,
    val staffCount: Int?,
    val moisturePercent: Int?,
    val contaminationPercent: Int?,
    val weather: String?,
    val hazardousFound: Boolean,
    val hazardousDescription: String?,
    val challenges: List<String>,
    val remarks: String?,
    val photos: List<String>,
    val recordedByUserId: String,
    val gpsLat: Double?,
    val gpsLong: Double?,
    val createdAt: Long,
    val updatedAt: Long
) {
    fun toJson(): JSONObject {
        val obj = JSONObject()
        obj.put("id", id)
        obj.put("lga", lga)
        obj.put("wasteSource", wasteSource)
        obj.put("siteName", siteName)
        obj.put("collectionDate", collectionDate)
        obj.put("team", team)
        obj.put("totalWasteKg", totalWasteKg)
        obj.put("recyclablePercentage", recyclablePercentage)
        obj.put("plasticKg", plasticKg)
        obj.put("paperKg", paperKg)
        obj.put("metalKg", metalKg)
        obj.put("glassKg", glassKg)
        obj.put("organicKg", organicKg)
        obj.put("otherKg", otherKg)
        obj.put("finalDisposalSite", finalDisposalSite)
        obj.put("transportUsed", transportUsed)
        obj.put("recyclersInvolved", recyclersInvolved)
        obj.put("tripCount", tripCount)
        obj.put("vehicleCount", vehicleCount)
        obj.put("hoursWorked", hoursWorked)
        obj.put("avgLoadKg", avgLoadKg)
        obj.put("staffCount", staffCount)
        obj.put("moisturePercent", moisturePercent)
        obj.put("contaminationPercent", contaminationPercent)
        obj.put("weather", weather)
        obj.put("hazardousFound", hazardousFound)
        obj.put("hazardousDescription", hazardousDescription)
        obj.put("challenges", JSONArray(challenges))
        obj.put("remarks", remarks)
        obj.put("photos", JSONArray(photos))
        obj.put("recordedByUserId", recordedByUserId)
        obj.put("gpsLat", gpsLat)
        obj.put("gpsLong", gpsLong)
        obj.put("createdAt", createdAt)
        obj.put("updatedAt", updatedAt)
        return obj
    }

    companion object {
        fun fromJson(obj: JSONObject): WasteAggregationReport {
            val challengesArr = obj.optJSONArray("challenges") ?: JSONArray()
            val challenges = mutableListOf<String>()
            for (i in 0 until challengesArr.length()) challenges.add(challengesArr.optString(i))

            val photosArr = obj.optJSONArray("photos") ?: JSONArray()
            val photos = mutableListOf<String>()
            for (i in 0 until photosArr.length()) photos.add(photosArr.optString(i))

            return WasteAggregationReport(
                id = obj.optString("id"),
                lga = obj.optString("lga"),
                wasteSource = obj.optString("wasteSource"),
                siteName = obj.optString("siteName"),
                collectionDate = obj.optString("collectionDate"),
                team = obj.optString("team").takeIf { it.isNotEmpty() },
                totalWasteKg = obj.optDouble("totalWasteKg"),
                recyclablePercentage = obj.optInt("recyclablePercentage"),
                plasticKg = obj.optDouble("plasticKg").let { if (it == 0.0 && !obj.has("plasticKg")) null else it },
                paperKg = obj.optDouble("paperKg").let { if (it == 0.0 && !obj.has("paperKg")) null else it },
                metalKg = obj.optDouble("metalKg").let { if (it == 0.0 && !obj.has("metalKg")) null else it },
                glassKg = obj.optDouble("glassKg").let { if (it == 0.0 && !obj.has("glassKg")) null else it },
                organicKg = obj.optDouble("organicKg").let { if (it == 0.0 && !obj.has("organicKg")) null else it },
                otherKg = obj.optDouble("otherKg").let { if (it == 0.0 && !obj.has("otherKg")) null else it },
                finalDisposalSite = obj.optString("finalDisposalSite"),
                transportUsed = obj.optString("transportUsed").takeIf { it.isNotEmpty() },
                recyclersInvolved = obj.optString("recyclersInvolved").takeIf { it.isNotEmpty() },
                tripCount = obj.optInt("tripCount").let { if (it == 0 && !obj.has("tripCount")) null else it },
                vehicleCount = obj.optInt("vehicleCount").let { if (it == 0 && !obj.has("vehicleCount")) null else it },
                hoursWorked = obj.optDouble("hoursWorked").let { if (it == 0.0 && !obj.has("hoursWorked")) null else it },
                avgLoadKg = obj.optDouble("avgLoadKg").let { if (it == 0.0 && !obj.has("avgLoadKg")) null else it },
                staffCount = obj.optInt("staffCount").let { if (it == 0 && !obj.has("staffCount")) null else it },
                moisturePercent = obj.optInt("moisturePercent").let { if (it == 0 && !obj.has("moisturePercent")) null else it },
                contaminationPercent = obj.optInt("contaminationPercent").let { if (it == 0 && !obj.has("contaminationPercent")) null else it },
                weather = obj.optString("weather").takeIf { it.isNotEmpty() },
                hazardousFound = obj.optBoolean("hazardousFound"),
                hazardousDescription = obj.optString("hazardousDescription").takeIf { it.isNotEmpty() },
                challenges = challenges,
                remarks = obj.optString("remarks").takeIf { it.isNotEmpty() },
                photos = photos,
                recordedByUserId = obj.optString("recordedByUserId"),
                gpsLat = obj.optDouble("gpsLat").let { if (it == 0.0 && !obj.has("gpsLat")) null else it },
                gpsLong = obj.optDouble("gpsLong").let { if (it == 0.0 && !obj.has("gpsLong")) null else it },
                createdAt = obj.optLong("createdAt"),
                updatedAt = obj.optLong("updatedAt")
            )
        }
    }
}
