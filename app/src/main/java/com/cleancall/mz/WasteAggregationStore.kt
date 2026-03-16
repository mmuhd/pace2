package com.cleancall.mz

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object WasteAggregationStore {
    private const val PREFS = "clean_call"
    private const val KEY = "waste_aggregation_store"
    private const val KEY_PENDING = "waste_aggregation_store_pending"

    fun save(context: Context, report: WasteAggregationReport) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        arr.put(report.toJson())
        prefs.edit().putString(KEY, arr.toString()).apply()
    }

    fun list(context: Context): List<WasteAggregationReport> {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        val out = mutableListOf<WasteAggregationReport>()
        for (i in 0 until arr.length()) {
            val obj = arr.optJSONObject(i) ?: JSONObject()
            out.add(WasteAggregationReport.fromJson(obj))
        }
        return out
    }

    fun find(context: Context, id: String): WasteAggregationReport? {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        for (i in 0 until arr.length()) {
            val obj = arr.optJSONObject(i) ?: continue
            if (obj.optString("id") == id) return WasteAggregationReport.fromJson(obj)
        }
        return null
    }

    fun savePending(context: Context, report: WasteAggregationReport) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY_PENDING, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        arr.put(report.toJson())
        prefs.edit().putString(KEY_PENDING, arr.toString()).apply()
    }

    fun pending(context: Context): List<WasteAggregationReport> {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY_PENDING, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        val out = mutableListOf<WasteAggregationReport>()
        for (i in 0 until arr.length()) {
            val obj = arr.optJSONObject(i) ?: JSONObject()
            out.add(WasteAggregationReport.fromJson(obj))
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
                val ok = ApiClient.postWasteAggregation(context, r)
                if (ok) { removePending(context, r.id); success++ }
            } catch (_: Exception) {}
        }
        return success
    }
}
