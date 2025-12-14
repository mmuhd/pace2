package com.cleancall.mz

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object WasteAggregationStore {
    private const val PREFS = "clean_call"
    private const val KEY = "waste_aggregation_store"

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
}
