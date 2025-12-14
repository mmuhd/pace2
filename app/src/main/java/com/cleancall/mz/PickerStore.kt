package com.cleancall.mz

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object PickerStore {
    private const val PREFS = "clean_call"
    private const val KEY = "picker_store"

    fun save(context: Context, picker: WastePicker) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        arr.put(picker.toJson())
        prefs.edit().putString(KEY, arr.toString()).apply()
    }

    fun list(context: Context): List<WastePicker> {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        val out = mutableListOf<WastePicker>()
        for (i in 0 until arr.length()) {
            val obj = arr.optJSONObject(i) ?: JSONObject()
            out.add(WastePicker.fromJson(obj))
        }
        return out
    }

    fun find(context: Context, id: String): WastePicker? {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        for (i in 0 until arr.length()) {
            val obj = arr.optJSONObject(i) ?: continue
            if (obj.optString("id") == id) return WastePicker.fromJson(obj)
        }
        return null
    }

    fun replaceAll(context: Context, items: List<WastePicker>) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arr = JSONArray()
        for (item in items) arr.put(item.toJson())
        prefs.edit().putString(KEY, arr.toString()).apply()
    }
}
