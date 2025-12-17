package com.cleancall.mz

import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

object PickerStore {
    private const val PREFS = "clean_call"
    private const val KEY = "picker_store"
    private const val KEY_PENDING = "picker_store_pending"
    private const val KEY_SYNC_LOG = "picker_sync_log"

    fun save(context: Context, picker: WastePicker) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        arr.put(picker.toJson())
        prefs.edit().putString(KEY, arr.toString()).apply()
    }

    fun savePending(context: Context, picker: WastePicker) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY_PENDING, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        arr.put(picker.toJson())
        prefs.edit().putString(KEY_PENDING, arr.toString()).apply()
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

    fun pending(context: Context): List<WastePicker> {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY_PENDING, "[]") ?: "[]"
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
        run {
            val arrStr = prefs.getString(KEY, "[]") ?: "[]"
            val arr = JSONArray(arrStr)
            for (i in 0 until arr.length()) {
                val obj = arr.optJSONObject(i) ?: continue
                if (obj.optString("id") == id) return WastePicker.fromJson(obj)
            }
        }
        run {
            val arrStr = prefs.getString(KEY_PENDING, "[]") ?: "[]"
            val arr = JSONArray(arrStr)
            for (i in 0 until arr.length()) {
                val obj = arr.optJSONObject(i) ?: continue
                if (obj.optString("id") == id) return WastePicker.fromJson(obj)
            }
        }
        return null
    }

    fun replaceAll(context: Context, items: List<WastePicker>) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arr = JSONArray()
        for (item in items) arr.put(item.toJson())
        prefs.edit().putString(KEY, arr.toString()).apply()
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
        clearSyncLog(context)
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val base = prefs.getString("api_base_url", BuildConfig.BASE_URL) ?: BuildConfig.BASE_URL
        val token = prefs.getString("api_token", null)
        appendSyncLog(context, "Base=" + base)
        appendSyncLog(context, "TokenPresent=" + (!token.isNullOrEmpty()))
        var successCount = 0
        for (p in pend) {
            try {
                appendSyncLog(context, "Attempt id=" + p.id + " name=" + p.fullName)
                val (code, body) = ApiClient.postWastePickerResponse(context, p)
                appendSyncLog(context, "Status=" + code)
                if (code in 200..299 || code == 409) {
                    removePending(context, p.id)
                    successCount++
                    appendSyncLog(context, "Result=success")
                } else {
                    appendSyncLog(context, "Result=failure body=" + body.take(400))
                }
            } catch (e: Exception) {
                appendSyncLog(context, "Exception during sync: " + (e.message ?: "unknown"))
            }
        }
        return successCount
    }

    fun clearSyncLog(context: Context) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_SYNC_LOG, "").apply()
    }

    fun appendSyncLog(context: Context, line: String) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val prev = prefs.getString(KEY_SYNC_LOG, "") ?: ""
        val next = (prev + if (prev.isEmpty()) "" else "\n") + line
        prefs.edit().putString(KEY_SYNC_LOG, next.take(4000)).apply()
        Log.d("PickerSync", line)
    }

    fun getSyncLog(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getString(KEY_SYNC_LOG, "") ?: ""
    }

    fun migrateLegacyLocalToPending(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arrStr = prefs.getString(KEY, "[]") ?: "[]"
        val arr = JSONArray(arrStr)
        val keep = JSONArray()
        var moved = 0
        for (i in 0 until arr.length()) {
            val obj = arr.optJSONObject(i) ?: JSONObject()
            val createdAt = obj.optLong("createdAt")
            if (createdAt != 0L) {
                val pendStr = prefs.getString(KEY_PENDING, "[]") ?: "[]"
                val pendArr = JSONArray(pendStr)
                pendArr.put(obj)
                prefs.edit().putString(KEY_PENDING, pendArr.toString()).apply()
                moved++
            } else {
                keep.put(obj)
            }
        }
        prefs.edit().putString(KEY, keep.toString()).apply()
        return moved
    }
}
