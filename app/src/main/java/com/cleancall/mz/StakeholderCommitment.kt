package com.cleancall.mz

import org.json.JSONArray
import org.json.JSONObject

data class StakeholderCommitment(
    val id: String,
    val stakeholderName: String,
    val stakeholderType: String,
    val lga: String,
    val contactPerson: String?,
    val phone: String?,
    val engagementDate: String,
    val engagementType: String,
    val engagedByUserId: String,
    val engagementDescription: String?,
    val commitmentText: String,
    val commitmentCategory: String?,
    val dueDate: String?,
    val priorityLevel: String?,
    val actionTaken: String?,
    val status: String,
    val followupRequired: Boolean,
    val followupType: String?,
    val followupDate: String?,
    val followupAssignedTo: String?,
    val remarks: String?,
    val evidence: List<String>,
    val evidenceTypes: List<String>,
    val recordedByUserId: String,
    val isSystemFlagged: Boolean?,
    val createdAt: Long,
    val updatedAt: Long
) {
    fun toJson(): JSONObject {
        val obj = JSONObject()
        obj.put("id", id)
        obj.put("stakeholderName", stakeholderName)
        obj.put("stakeholderType", stakeholderType)
        obj.put("lga", lga)
        obj.put("contactPerson", contactPerson)
        obj.put("phone", phone)
        obj.put("engagementDate", engagementDate)
        obj.put("engagementType", engagementType)
        obj.put("engagedByUserId", engagedByUserId)
        obj.put("engagementDescription", engagementDescription)
        obj.put("commitmentText", commitmentText)
        obj.put("commitmentCategory", commitmentCategory)
        obj.put("dueDate", dueDate)
        obj.put("priorityLevel", priorityLevel)
        obj.put("actionTaken", actionTaken)
        obj.put("status", status)
        obj.put("followupRequired", followupRequired)
        obj.put("followupType", followupType)
        obj.put("followupDate", followupDate)
        obj.put("followupAssignedTo", followupAssignedTo)
        obj.put("remarks", remarks)
        obj.put("evidence", JSONArray(evidence))
        obj.put("evidenceTypes", JSONArray(evidenceTypes))
        obj.put("recordedByUserId", recordedByUserId)
        obj.put("isSystemFlagged", isSystemFlagged)
        obj.put("createdAt", createdAt)
        obj.put("updatedAt", updatedAt)
        return obj
    }

    companion object {
        fun fromJson(obj: JSONObject): StakeholderCommitment {
            val evidenceArr = obj.optJSONArray("evidence") ?: JSONArray()
            val evidence = mutableListOf<String>()
            for (i in 0 until evidenceArr.length()) evidence.add(evidenceArr.optString(i))

            val typesArr = obj.optJSONArray("evidenceTypes") ?: JSONArray()
            val evidenceTypes = mutableListOf<String>()
            for (i in 0 until typesArr.length()) evidenceTypes.add(typesArr.optString(i))

            return StakeholderCommitment(
                id = obj.optString("id"),
                stakeholderName = obj.optString("stakeholderName"),
                stakeholderType = obj.optString("stakeholderType"),
                lga = obj.optString("lga"),
                contactPerson = obj.optString("contactPerson").takeIf { it.isNotEmpty() },
                phone = obj.optString("phone").takeIf { it.isNotEmpty() },
                engagementDate = obj.optString("engagementDate"),
                engagementType = obj.optString("engagementType"),
                engagedByUserId = obj.optString("engagedByUserId"),
                engagementDescription = obj.optString("engagementDescription").takeIf { it.isNotEmpty() },
                commitmentText = obj.optString("commitmentText"),
                commitmentCategory = obj.optString("commitmentCategory").takeIf { it.isNotEmpty() },
                dueDate = obj.optString("dueDate").takeIf { it.isNotEmpty() },
                priorityLevel = obj.optString("priorityLevel").takeIf { it.isNotEmpty() },
                actionTaken = obj.optString("actionTaken").takeIf { it.isNotEmpty() },
                status = obj.optString("status"),
                followupRequired = obj.optBoolean("followupRequired"),
                followupType = obj.optString("followupType").takeIf { it.isNotEmpty() },
                followupDate = obj.optString("followupDate").takeIf { it.isNotEmpty() },
                followupAssignedTo = obj.optString("followupAssignedTo").takeIf { it.isNotEmpty() },
                remarks = obj.optString("remarks").takeIf { it.isNotEmpty() },
                evidence = evidence,
                evidenceTypes = evidenceTypes,
                recordedByUserId = obj.optString("recordedByUserId"),
                isSystemFlagged = if (obj.has("isSystemFlagged")) obj.optBoolean("isSystemFlagged") else null,
                createdAt = obj.optLong("createdAt"),
                updatedAt = obj.optLong("updatedAt")
            )
        }
    }
}
