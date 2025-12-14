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
        val name = prefs.getString("signup_name", "User") ?: "User"
        val lga = prefs.getString("signup_lga", "LGA") ?: "LGA"
        val role = prefs.getString("pending_role", "") ?: ""

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

        kpi1.text = "128"
        kpi2.text = "54"
        kpi3.text = "23"
        kpi4.text = "4.2t"

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
