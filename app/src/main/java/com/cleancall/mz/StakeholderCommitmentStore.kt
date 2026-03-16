package com.cleancall.mz

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object StakeholderCommitmentStore {
    private const val PREFS = "clean_call"
    private const val KEY = "stakeholder_commitment_store"
    private const val KEY_PENDING = "stakeholder_commitment_store_pending"

    fun save(context: Context, item: StakeholderCommitment) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        arr.put(item.toJson())
        prefs.edit().putString(KEY, arr.toString()).apply()
    }

    fun list(context: Context): List<StakeholderCommitment> {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        val out = mutableListOf<StakeholderCommitment>()
        for (i in 0 until arr.length()) {
            val obj = arr.optJSONObject(i) ?: JSONObject()
            out.add(StakeholderCommitment.fromJson(obj))
        }
        return out
    }

    fun find(context: Context, id: String): StakeholderCommitment? {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        for (i in 0 until arr.length()) {
            val obj = arr.optJSONObject(i) ?: continue
            if (obj.optString("id") == id) return StakeholderCommitment.fromJson(obj)
        }
        return null
    }

    fun replaceAll(context: Context, items: List<StakeholderCommitment>) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arr = JSONArray()
        for (item in items) arr.put(item.toJson())
        prefs.edit().putString(KEY, arr.toString()).apply()
    }

    fun savePending(context: Context, item: StakeholderCommitment) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY_PENDING, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        arr.put(item.toJson())
        prefs.edit().putString(KEY_PENDING, arr.toString()).apply()
    }

    fun pending(context: Context): List<StakeholderCommitment> {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY_PENDING, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        val out = mutableListOf<StakeholderCommitment>()
        for (i in 0 until arr.length()) {
            val obj = arr.optJSONObject(i) ?: JSONObject()
            out.add(StakeholderCommitment.fromJson(obj))
        }
        return out
    }

    fun removePending(context: Context, id: String) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY_PENDING, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        val out = JSONArray()
        for (i in 0 until arr.length()) {
            val obj = arr.optJSONObject(i) ?: continue
            if (obj.optString("id") != id) out.put(obj)
        }
        prefs.edit().putString(KEY_PENDING, out.toString()).apply()
    }

    fun syncPending(context: Context): Int {
        val pend = pending(context)
        var success = 0
        for (it in pend) {
            try {
                val ok = ApiClient.postStakeholderCommitment(context, it)
                if (ok) { removePending(context, it.id); success++ }
            } catch (_: Exception) {}
        }
        return success
    }
}
