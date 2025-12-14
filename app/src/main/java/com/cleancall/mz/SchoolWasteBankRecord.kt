package com.cleancall.mz

import org.json.JSONArray
import org.json.JSONObject

data class SchoolWasteBankRecord(
    val id: String,
    val schoolId: String?,
    val schoolName: String,
    val lga: String,
    val reportingPeriodType: String,
    val reportingDate: String?,
    val reportingWeekStart: String?,
    val reportingMonth: String?,
    val status: String,
    val plasticCollectedKg: Double?,
    val plasticRecycledKg: Double?,
    val paperCollectedKg: Double?,
    val paperRecycledKg: Double?,
    val metalCollectedKg: Double?,
    val metalRecycledKg: Double?,
    val glassCollectedKg: Double?,
    val glassRecycledKg: Double?,
    val organicCollectedKg: Double?,
    val organicRecycledKg: Double?,
    val otherType: String?,
    val otherCollectedKg: Double?,
    val otherRecycledKg: Double?,
    val soldToRecycler: Boolean,
    val incomeFromSale: Double?,
    val buyerName: String?,
    val challenges: List<String>,
    val studentParticipationLevel: Int?,
    val remarks: String?,
    val photoBase64s: List<String>,
    val recordedByUserId: String,
    val createdAt: Long,
    val updatedAt: Long
) {
    fun toJson(): JSONObject {
        val obj = JSONObject()
        obj.put("id", id)
        obj.put("schoolId", schoolId)
        obj.put("schoolName", schoolName)
        obj.put("lga", lga)
        obj.put("reportingPeriodType", reportingPeriodType)
        obj.put("reportingDate", reportingDate)
        obj.put("reportingWeekStart", reportingWeekStart)
        obj.put("reportingMonth", reportingMonth)
        obj.put("status", status)
        obj.put("plasticCollectedKg", plasticCollectedKg)
        obj.put("plasticRecycledKg", plasticRecycledKg)
        obj.put("paperCollectedKg", paperCollectedKg)
        obj.put("paperRecycledKg", paperRecycledKg)
        obj.put("metalCollectedKg", metalCollectedKg)
        obj.put("metalRecycledKg", metalRecycledKg)
        obj.put("glassCollectedKg", glassCollectedKg)
        obj.put("glassRecycledKg", glassRecycledKg)
        obj.put("organicCollectedKg", organicCollectedKg)
        obj.put("organicRecycledKg", organicRecycledKg)
        obj.put("otherType", otherType)
        obj.put("otherCollectedKg", otherCollectedKg)
        obj.put("otherRecycledKg", otherRecycledKg)
        obj.put("soldToRecycler", soldToRecycler)
        obj.put("incomeFromSale", incomeFromSale)
        obj.put("buyerName", buyerName)
        obj.put("challenges", JSONArray(challenges))
        obj.put("studentParticipationLevel", studentParticipationLevel)
        obj.put("remarks", remarks)
        obj.put("photoBase64s", JSONArray(photoBase64s))
        obj.put("recordedByUserId", recordedByUserId)
        obj.put("createdAt", createdAt)
        obj.put("updatedAt", updatedAt)
        return obj
    }

    companion object {
        fun fromJson(obj: JSONObject): SchoolWasteBankRecord {
            val challengesArr = obj.optJSONArray("challenges") ?: JSONArray()
            val challenges = mutableListOf<String>()
            for (i in 0 until challengesArr.length()) challenges.add(challengesArr.optString(i))

            val photosArr = obj.optJSONArray("photoBase64s") ?: JSONArray()
            val photos = mutableListOf<String>()
            for (i in 0 until photosArr.length()) photos.add(photosArr.optString(i))

            return SchoolWasteBankRecord(
                id = obj.optString("id"),
                schoolId = obj.optString("schoolId").takeIf { it.isNotEmpty() },
                schoolName = obj.optString("schoolName"),
                lga = obj.optString("lga"),
                reportingPeriodType = obj.optString("reportingPeriodType"),
                reportingDate = obj.optString("reportingDate").takeIf { it.isNotEmpty() },
                reportingWeekStart = obj.optString("reportingWeekStart").takeIf { it.isNotEmpty() },
                reportingMonth = obj.optString("reportingMonth").takeIf { it.isNotEmpty() },
                status = obj.optString("status"),
                plasticCollectedKg = obj.optDouble("plasticCollectedKg").let { if (it == 0.0 && !obj.has("plasticCollectedKg")) null else it },
                plasticRecycledKg = obj.optDouble("plasticRecycledKg").let { if (it == 0.0 && !obj.has("plasticRecycledKg")) null else it },
                paperCollectedKg = obj.optDouble("paperCollectedKg").let { if (it == 0.0 && !obj.has("paperCollectedKg")) null else it },
                paperRecycledKg = obj.optDouble("paperRecycledKg").let { if (it == 0.0 && !obj.has("paperRecycledKg")) null else it },
                metalCollectedKg = obj.optDouble("metalCollectedKg").let { if (it == 0.0 && !obj.has("metalCollectedKg")) null else it },
                metalRecycledKg = obj.optDouble("metalRecycledKg").let { if (it == 0.0 && !obj.has("metalRecycledKg")) null else it },
                glassCollectedKg = obj.optDouble("glassCollectedKg").let { if (it == 0.0 && !obj.has("glassCollectedKg")) null else it },
                glassRecycledKg = obj.optDouble("glassRecycledKg").let { if (it == 0.0 && !obj.has("glassRecycledKg")) null else it },
                organicCollectedKg = obj.optDouble("organicCollectedKg").let { if (it == 0.0 && !obj.has("organicCollectedKg")) null else it },
                organicRecycledKg = obj.optDouble("organicRecycledKg").let { if (it == 0.0 && !obj.has("organicRecycledKg")) null else it },
                otherType = obj.optString("otherType").takeIf { it.isNotEmpty() },
                otherCollectedKg = obj.optDouble("otherCollectedKg").let { if (it == 0.0 && !obj.has("otherCollectedKg")) null else it },
                otherRecycledKg = obj.optDouble("otherRecycledKg").let { if (it == 0.0 && !obj.has("otherRecycledKg")) null else it },
                soldToRecycler = obj.optBoolean("soldToRecycler"),
                incomeFromSale = obj.optDouble("incomeFromSale").let { if (it == 0.0 && !obj.has("incomeFromSale")) null else it },
                buyerName = obj.optString("buyerName").takeIf { it.isNotEmpty() },
                challenges = challenges,
                studentParticipationLevel = obj.optInt("studentParticipationLevel").let { if (it == 0 && !obj.has("studentParticipationLevel")) null else it },
                remarks = obj.optString("remarks").takeIf { it.isNotEmpty() },
                photoBase64s = photos,
                recordedByUserId = obj.optString("recordedByUserId"),
                createdAt = obj.optLong("createdAt"),
                updatedAt = obj.optLong("updatedAt")
            )
        }
    }
}
