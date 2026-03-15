package com.cleancall.mz

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object EvacuationStore {
    private const val PREFS = "clean_call"
    private const val KEY = "evac_store"
    private const val KEY_PENDING = "evac_store_pending"

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

    fun savePending(context: Context, task: EvacuationTask) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY_PENDING, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        arr.put(task.toJson())
        prefs.edit().putString(KEY_PENDING, arr.toString()).apply()
    }

    fun pending(context: Context): List<EvacuationTask> {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY_PENDING, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        val out = mutableListOf<EvacuationTask>()
        for (i in 0 until arr.length()) {
            val obj = arr.optJSONObject(i) ?: JSONObject()
            out.add(EvacuationTask.fromJson(obj))
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
        var successCount = 0
        for (t in pend) {
            try {
                val (code, _) = ApiClient.postEvacuationTask(context, t)
                if (code in 200..299 || code == 409) {
                    removePending(context, t.id)
                    successCount++
                }
            } catch (_: Exception) {}
        }
        return successCount
    }
}
