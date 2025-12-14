package com.cleancall.mz

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val prefs = getSharedPreferences("clean_call", MODE_PRIVATE)
        val token = prefs.getString("api_token", null)
        if (token.isNullOrEmpty()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        val name = prefs.getString("user_name", prefs.getString("signup_name", "User")) ?: "User"
        val lga = prefs.getString("user_lga", prefs.getString("signup_lga", "LGA")) ?: "LGA"
        val role = prefs.getString("user_role", prefs.getString("pending_role", "")) ?: ""

        val roleLabel = when (role) {
            "PENDING_FIELD_OPERATOR" -> "Field Operator (Pending)"
            "BENEFICIARY_PICKER" -> "Waste Picker"
            "INVITE_PENDING" -> "Invite Pending"
            else -> "User"
        }

        findViewById<TextView>(R.id.userHeader).text = "$name ($roleLabel) – $lga"

        val kpi1 = findViewById<TextView>(R.id.kpi1Value)
        val kpi2 = findViewById<TextView>(R.id.kpi2Value)
        val kpi3 = findViewById<TextView>(R.id.kpi3Value)
        val kpi4 = findViewById<TextView>(R.id.kpi4Value)

        Thread {
            try {
                val client = okhttp3.OkHttpClient.Builder().build()
                val base = prefs.getString("api_base_url", BuildConfig.BASE_URL) ?: BuildConfig.BASE_URL
                val url = (if (base.endsWith("/")) base.dropLast(1) else base) + "/reports/overview"
                val req = okhttp3.Request.Builder().url(url).get().addHeader("Accept","application/json").build()
                val resp = client.newCall(req).execute()
                if (resp.isSuccessful) {
                    val s = resp.body?.string().orEmpty()
                    val obj = org.json.JSONObject(s)
                    val kpis = obj.optJSONObject("kpis")
                    val wastePickers = kpis?.optInt("waste_pickers") ?: 0
                    val womenSessions = kpis?.optInt("women_sessions") ?: 0
                    val activeSchools = kpis?.optInt("active_schools_30d") ?: 0
                    val totalAggKg = kpis?.optDouble("total_agg_kg", 0.0) ?: 0.0
                    runOnUiThread {
                        kpi1.text = wastePickers.toString()
                        kpi2.text = womenSessions.toString()
                        kpi3.text = activeSchools.toString()
                        kpi4.text = String.format("%.1ft", totalAggKg / 1000.0)
                    }
                }
            } catch (_: Exception) {}
        }.start()

        val kpiRow2 = findViewById<View>(R.id.kpiRow2)
        val navRecords = findViewById<View>(R.id.navRecords)
        val navAdd = findViewById<View>(R.id.navAdd)
        val navReports = findViewById<View>(R.id.navReports)
        val navSettings = findViewById<View>(R.id.navSettings)

        when (role) {
            "BENEFICIARY_PICKER" -> {
                kpiRow2.visibility = View.GONE
            }
            else -> {}
        }

        navRecords.setOnClickListener {
            startActivity(Intent(this, RecordsActivity::class.java))
        }

        navAdd.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }

        navReports.setOnClickListener {
            startActivity(Intent(this, ReportsActivity::class.java))
        }

        navSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}
