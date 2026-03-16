package com.cleancall.mz

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object SchoolWasteBankStore {
    private const val PREFS = "clean_call"
    private const val KEY = "school_waste_bank_store"
    private const val KEY_PENDING = "school_waste_bank_store_pending"

    fun save(context: Context, record: SchoolWasteBankRecord) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        arr.put(record.toJson())
        prefs.edit().putString(KEY, arr.toString()).apply()
    }

    fun list(context: Context): List<SchoolWasteBankRecord> {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        val out = mutableListOf<SchoolWasteBankRecord>()
        for (i in 0 until arr.length()) {
            val obj = arr.optJSONObject(i) ?: JSONObject()
            out.add(SchoolWasteBankRecord.fromJson(obj))
        }
        return out
    }

    fun find(context: Context, id: String): SchoolWasteBankRecord? {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        for (i in 0 until arr.length()) {
            val obj = arr.optJSONObject(i) ?: continue
            if (obj.optString("id") == id) return SchoolWasteBankRecord.fromJson(obj)
        }
        return null
    }

    fun replaceAll(context: Context, items: List<SchoolWasteBankRecord>) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arr = JSONArray()
        for (item in items) arr.put(item.toJson())
        prefs.edit().putString(KEY, arr.toString()).apply()
    }

    fun savePending(context: Context, record: SchoolWasteBankRecord) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY_PENDING, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        arr.put(record.toJson())
        prefs.edit().putString(KEY_PENDING, arr.toString()).apply()
    }

    fun pending(context: Context): List<SchoolWasteBankRecord> {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY_PENDING, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        val out = mutableListOf<SchoolWasteBankRecord>()
        for (i in 0 until arr.length()) {
            val obj = arr.optJSONObject(i) ?: JSONObject()
            out.add(SchoolWasteBankRecord.fromJson(obj))
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
        for (r in pend) {
            try {
                val ok = ApiClient.postSchoolWasteBankRecord(context, r)
                if (ok) { removePending(context, r.id); success++ }
            } catch (_: Exception) {}
        }
        return success
    }
}
