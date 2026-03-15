package com.cleancall.mz

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import kotlin.math.abs

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                try {
                    applyFontScale(activity)
                } catch (_: Exception) {
                }
            }
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

    private fun applyFontScale(activity: Activity) {
        val prefs = activity.getSharedPreferences("clean_call", Context.MODE_PRIVATE)
        val size = prefs.getString("pref_text_size", "Medium") ?: "Medium"
        val scale = when (size) {
            "Small" -> 0.85f
            "Large" -> 1.15f
            "Extra Large" -> 1.30f
            else -> 1.0f
        }
        val res = activity.resources
        val current = res.configuration.fontScale
        if (abs(current - scale) > 0.01f) {
            val conf = Configuration(res.configuration)
            conf.fontScale = scale
            res.updateConfiguration(conf, res.displayMetrics)
        }
    }
}
