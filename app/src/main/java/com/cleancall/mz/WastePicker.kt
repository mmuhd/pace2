package com.cleancall.mz

import org.json.JSONArray
import org.json.JSONObject

data class WastePicker(
    val id: String,
    val fullName: String,
    val nickname: String?,
    val gender: String,
    val ageRange: String,
    val phone: String?,
    val idNumber: String?,
    val lga: String,
    val community: String,
    val clusterName: String?,
    val primaryLocation: String,
    val wasteTypes: List<String>,
    val yearsExperience: String,
    val sellingMode: String?,
    val incomeRange: String?,
    val ppeUsage: String,
    val hadTraining: Boolean,
    val trainingProvider: String?,
    val willingToJoin: String,
    val specialNeeds: String?,
    val photoBase64: String?,
    val createdAt: Long
) {
    fun toJson(): JSONObject {
        val obj = JSONObject()
        obj.put("id", id)
        obj.put("fullName", fullName)
        obj.put("nickname", nickname)
        obj.put("gender", gender)
        obj.put("ageRange", ageRange)
        obj.put("phone", phone)
        obj.put("idNumber", idNumber)
        obj.put("lga", lga)
        obj.put("community", community)
        obj.put("clusterName", clusterName)
        obj.put("primaryLocation", primaryLocation)
        obj.put("wasteTypes", JSONArray(wasteTypes))
        obj.put("yearsExperience", yearsExperience)
        obj.put("sellingMode", sellingMode)
        obj.put("incomeRange", incomeRange)
        obj.put("ppeUsage", ppeUsage)
        obj.put("hadTraining", hadTraining)
        obj.put("trainingProvider", trainingProvider)
        obj.put("willingToJoin", willingToJoin)
        obj.put("specialNeeds", specialNeeds)
        obj.put("photoBase64", photoBase64)
        obj.put("createdAt", createdAt)
        return obj
    }

    companion object {
        fun fromJson(obj: JSONObject): WastePicker {
            val typesArr = obj.optJSONArray("wasteTypes") ?: JSONArray()
            val types = mutableListOf<String>()
            for (i in 0 until typesArr.length()) {
                types.add(typesArr.optString(i))
            }
            return WastePicker(
                id = obj.optString("id"),
                fullName = obj.optString("fullName"),
                nickname = obj.optString("nickname").takeIf { it.isNotEmpty() },
                gender = obj.optString("gender"),
                ageRange = obj.optString("ageRange"),
                phone = obj.optString("phone").takeIf { it.isNotEmpty() },
                idNumber = obj.optString("idNumber").takeIf { it.isNotEmpty() },
                lga = obj.optString("lga"),
                community = obj.optString("community"),
                clusterName = obj.optString("clusterName").takeIf { it.isNotEmpty() },
                primaryLocation = obj.optString("primaryLocation"),
                wasteTypes = types,
                yearsExperience = obj.optString("yearsExperience"),
                sellingMode = obj.optString("sellingMode").takeIf { it.isNotEmpty() },
                incomeRange = obj.optString("incomeRange").takeIf { it.isNotEmpty() },
                ppeUsage = obj.optString("ppeUsage"),
                hadTraining = obj.optBoolean("hadTraining"),
                trainingProvider = obj.optString("trainingProvider").takeIf { it.isNotEmpty() },
                willingToJoin = obj.optString("willingToJoin"),
                specialNeeds = obj.optString("specialNeeds").takeIf { it.isNotEmpty() },
                photoBase64 = obj.optString("photoBase64").takeIf { it.isNotEmpty() },
                createdAt = obj.optLong("createdAt")
            )
        }
    }
}
