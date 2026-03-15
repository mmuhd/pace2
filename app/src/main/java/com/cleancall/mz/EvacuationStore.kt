package com.cleancall.mz

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object EvacuationStore {
    private const val PREFS = "clean_call"
    private const val KEY = "evac_store"

    fun save(context: Context, task: EvacuationTask) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        arr.put(task.toJson())
        prefs.edit().putString(KEY, arr.toString()).apply()
    }

    fun list(context: Context): List<EvacuationTask> {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        val out = mutableListOf<EvacuationTask>()
        for (i in 0 until arr.length()) {
            val obj = arr.optJSONObject(i) ?: JSONObject()
            out.add(EvacuationTask.fromJson(obj))
        }
        return out
    }
}
