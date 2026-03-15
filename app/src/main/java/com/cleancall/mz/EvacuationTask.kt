package com.cleancall.mz

import org.json.JSONArray
import org.json.JSONObject

data class EvacuationTask(
    val id: String,
    val sourceType: String, // Household | School | Aggregation | Public bin
    val sourceName: String?,
    val lga: String,
    val address: String?,
    val scheduledAt: String?, // ISO-like string or simple text
    val assignedTo: String?,
    val status: String, // pending | collected | delivered
    val totalKg: Double?,
    val breakdown: List<Pair<String, Double>>, // material -> kg
    val contaminationScore: Int?, // 0-100
    val photoBase64s: List<String>,
    val gpsLat: Double?,
    val gpsLong: Double?,
    val createdByUserId: String?,
    val createdAt: Long
) {
    fun toJson(): JSONObject {
        val obj = JSONObject()
        obj.put("id", id)
        obj.put("sourceType", sourceType)
        obj.put("sourceName", sourceName)
        obj.put("lga", lga)
        obj.put("address", address)
        obj.put("scheduledAt", scheduledAt)
        obj.put("assignedTo", assignedTo)
        obj.put("status", status)
        obj.put("totalKg", totalKg)
        val arr = JSONArray()
        for (p in breakdown) {
            arr.put(JSONObject().apply {
                put("material", p.first)
                put("kg", p.second)
            })
        }
        obj.put("breakdown", arr)
        obj.put("contaminationScore", contaminationScore)
        obj.put("photoBase64s", JSONArray(photoBase64s))
        obj.put("gpsLat", gpsLat)
        obj.put("gpsLong", gpsLong)
        obj.put("createdByUserId", createdByUserId)
        obj.put("createdAt", createdAt)
        return obj
    }

    companion object {
        fun fromJson(obj: JSONObject): EvacuationTask {
            val breakdownArr = obj.optJSONArray("breakdown") ?: JSONArray()
            val breakdown = mutableListOf<Pair<String, Double>>()
            for (i in 0 until breakdownArr.length()) {
                val it = breakdownArr.optJSONObject(i) ?: continue
                breakdown.add(it.optString("material") to it.optDouble("kg"))
            }
            val photosArr = obj.optJSONArray("photoBase64s") ?: JSONArray()
            val photos = mutableListOf<String>()
            for (i in 0 until photosArr.length()) {
                photos.add(photosArr.optString(i))
            }
            return EvacuationTask(
                id = obj.optString("id"),
                sourceType = obj.optString("sourceType"),
                sourceName = obj.optString("sourceName").takeIf { it.isNotEmpty() },
                lga = obj.optString("lga"),
                address = obj.optString("address").takeIf { it.isNotEmpty() },
                scheduledAt = obj.optString("scheduledAt").takeIf { it.isNotEmpty() },
                assignedTo = obj.optString("assignedTo").takeIf { it.isNotEmpty() },
                status = obj.optString("status"),
                totalKg = obj.optDouble("totalKg").let { if (it == 0.0 && !obj.has("totalKg")) null else it },
                breakdown = breakdown,
                contaminationScore = obj.optInt("contaminationScore").let { if (obj.has("contaminationScore")) it else null },
                photoBase64s = photos,
                gpsLat = obj.optDouble("gpsLat").let { if (it == 0.0 && !obj.has("gpsLat")) null else it },
                gpsLong = obj.optDouble("gpsLong").let { if (it == 0.0 && !obj.has("gpsLong")) null else it },
                createdByUserId = obj.optString("createdByUserId").takeIf { it.isNotEmpty() },
                createdAt = obj.optLong("createdAt")
            )
        }
    }
}
