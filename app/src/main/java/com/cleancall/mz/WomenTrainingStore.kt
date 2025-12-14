package com.cleancall.mz

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object WomenTrainingStore {
    private const val PREFS = "clean_call"
    private const val KEY = "women_training_store"

    fun save(context: Context, session: WomenTrainingSession) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        arr.put(session.toJson())
        prefs.edit().putString(KEY, arr.toString()).apply()
    }

    fun list(context: Context): List<WomenTrainingSession> {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        val out = mutableListOf<WomenTrainingSession>()
        for (i in 0 until arr.length()) {
            val obj = arr.optJSONObject(i) ?: JSONObject()
            out.add(WomenTrainingSession.fromJson(obj))
        }
        return out
    }

    fun find(context: Context, id: String): WomenTrainingSession? {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        for (i in 0 until arr.length()) {
            val obj = arr.optJSONObject(i) ?: continue
            if (obj.optString("id") == id) return WomenTrainingSession.fromJson(obj)
        }
        return null
    }

    fun replaceAll(context: Context, items: List<WomenTrainingSession>) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arr = JSONArray()
        for (item in items) arr.put(item.toJson())
        prefs.edit().putString(KEY, arr.toString()).apply()
    }
}
