package com.cleancall.mz

import org.json.JSONArray
import org.json.JSONObject

data class WomenTrainingSession(
    val id: String,
    val title: String,
    val date: String,
    val startTime: String?,
    val endTime: String?,
    val lga: String,
    val community: String,
    val venueType: String,
    val facilitatorName: String,
    val organisation: String?,
    val totalWomen: Int,
    val age18_25: Int?,
    val age26_35: Int?,
    val age36_45: Int?,
    val age46Plus: Int?,
    val householdsRepresented: Int?,
    val attendanceType: String?,
    val topics: List<String>,
    val methods: List<String>,
    val duration: String,
    val pretestUsed: Boolean,
    val knowledgeSortingBefore: Int?,
    val knowledgeSortingAfter: Int?,
    val categoriesCorrectBefore: Int?,
    val categoriesCorrectAfter: Int?,
    val burnFalseBefore: Int?,
    val burnFalseAfter: Int?,
    val confidenceAvgBefore: Int?,
    val confidenceAvgAfter: Int?,
    val importanceAvgBefore: Int?,
    val importanceAvgAfter: Int?,
    val committedToSorting: Int,
    val followupType: String,
    val followupDate: String?,
    val notes: String?,
    val createdByUserId: String,
    val sessionCode: String,
    val createdAt: Long,
    val updatedAt: Long
) {
    fun toJson(): JSONObject {
        val obj = JSONObject()
        obj.put("id", id)
        obj.put("title", title)
        obj.put("date", date)
        obj.put("startTime", startTime)
        obj.put("endTime", endTime)
        obj.put("lga", lga)
        obj.put("community", community)
        obj.put("venueType", venueType)
        obj.put("facilitatorName", facilitatorName)
        obj.put("organisation", organisation)
        obj.put("totalWomen", totalWomen)
        obj.put("age18_25", age18_25)
        obj.put("age26_35", age26_35)
        obj.put("age36_45", age36_45)
        obj.put("age46Plus", age46Plus)
        obj.put("householdsRepresented", householdsRepresented)
        obj.put("attendanceType", attendanceType)
        obj.put("topics", JSONArray(topics))
        obj.put("methods", JSONArray(methods))
        obj.put("duration", duration)
        obj.put("pretestUsed", pretestUsed)
        obj.put("knowledgeSortingBefore", knowledgeSortingBefore)
        obj.put("knowledgeSortingAfter", knowledgeSortingAfter)
        obj.put("categoriesCorrectBefore", categoriesCorrectBefore)
        obj.put("categoriesCorrectAfter", categoriesCorrectAfter)
        obj.put("burnFalseBefore", burnFalseBefore)
        obj.put("burnFalseAfter", burnFalseAfter)
        obj.put("confidenceAvgBefore", confidenceAvgBefore)
        obj.put("confidenceAvgAfter", confidenceAvgAfter)
        obj.put("importanceAvgBefore", importanceAvgBefore)
        obj.put("importanceAvgAfter", importanceAvgAfter)
        obj.put("committedToSorting", committedToSorting)
        obj.put("followupType", followupType)
        obj.put("followupDate", followupDate)
        obj.put("notes", notes)
        obj.put("createdByUserId", createdByUserId)
        obj.put("sessionCode", sessionCode)
        obj.put("createdAt", createdAt)
        obj.put("updatedAt", updatedAt)
        return obj
    }

    companion object {
        fun fromJson(obj: JSONObject): WomenTrainingSession {
            val topicsArr = obj.optJSONArray("topics") ?: JSONArray()
            val topics = mutableListOf<String>()
            for (i in 0 until topicsArr.length()) {
                topics.add(topicsArr.optString(i))
            }
            val methodsArr = obj.optJSONArray("methods") ?: JSONArray()
            val methods = mutableListOf<String>()
            for (i in 0 until methodsArr.length()) {
                methods.add(methodsArr.optString(i))
            }
            return WomenTrainingSession(
                id = obj.optString("id"),
                title = obj.optString("title"),
                date = obj.optString("date"),
                startTime = obj.optString("startTime").takeIf { it.isNotEmpty() },
                endTime = obj.optString("endTime").takeIf { it.isNotEmpty() },
                lga = obj.optString("lga"),
                community = obj.optString("community"),
                venueType = obj.optString("venueType"),
                facilitatorName = obj.optString("facilitatorName"),
                organisation = obj.optString("organisation").takeIf { it.isNotEmpty() },
                totalWomen = obj.optInt("totalWomen"),
                age18_25 = obj.optInt("age18_25").let { if (it == 0 && !obj.has("age18_25")) null else it },
                age26_35 = obj.optInt("age26_35").let { if (it == 0 && !obj.has("age26_35")) null else it },
                age36_45 = obj.optInt("age36_45").let { if (it == 0 && !obj.has("age36_45")) null else it },
                age46Plus = obj.optInt("age46Plus").let { if (it == 0 && !obj.has("age46Plus")) null else it },
                householdsRepresented = obj.optInt("householdsRepresented").let { if (it == 0 && !obj.has("householdsRepresented")) null else it },
                attendanceType = obj.optString("attendanceType").takeIf { it.isNotEmpty() },
                topics = topics,
                methods = methods,
                duration = obj.optString("duration"),
                pretestUsed = obj.optBoolean("pretestUsed"),
                knowledgeSortingBefore = obj.optInt("knowledgeSortingBefore").let { if (it == 0 && !obj.has("knowledgeSortingBefore")) null else it },
                knowledgeSortingAfter = obj.optInt("knowledgeSortingAfter").let { if (it == 0 && !obj.has("knowledgeSortingAfter")) null else it },
                categoriesCorrectBefore = obj.optInt("categoriesCorrectBefore").let { if (it == 0 && !obj.has("categoriesCorrectBefore")) null else it },
                categoriesCorrectAfter = obj.optInt("categoriesCorrectAfter").let { if (it == 0 && !obj.has("categoriesCorrectAfter")) null else it },
                burnFalseBefore = obj.optInt("burnFalseBefore").let { if (it == 0 && !obj.has("burnFalseBefore")) null else it },
                burnFalseAfter = obj.optInt("burnFalseAfter").let { if (it == 0 && !obj.has("burnFalseAfter")) null else it },
                confidenceAvgBefore = obj.optInt("confidenceAvgBefore").let { if (it == 0 && !obj.has("confidenceAvgBefore")) null else it },
                confidenceAvgAfter = obj.optInt("confidenceAvgAfter").let { if (it == 0 && !obj.has("confidenceAvgAfter")) null else it },
                importanceAvgBefore = obj.optInt("importanceAvgBefore").let { if (it == 0 && !obj.has("importanceAvgBefore")) null else it },
                importanceAvgAfter = obj.optInt("importanceAvgAfter").let { if (it == 0 && !obj.has("importanceAvgAfter")) null else it },
                committedToSorting = obj.optInt("committedToSorting"),
                followupType = obj.optString("followupType"),
                followupDate = obj.optString("followupDate").takeIf { it.isNotEmpty() },
                notes = obj.optString("notes").takeIf { it.isNotEmpty() },
                createdByUserId = obj.optString("createdByUserId"),
                sessionCode = obj.optString("sessionCode"),
                createdAt = obj.optLong("createdAt"),
                updatedAt = obj.optLong("updatedAt")
            )
        }
    }
}

