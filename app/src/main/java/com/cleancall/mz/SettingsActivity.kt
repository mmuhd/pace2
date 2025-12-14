package com.cleancall.mz

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<View>(R.id.backButton).setOnClickListener { finish() }

        val prefs = getSharedPreferences("clean_call", MODE_PRIVATE)
        val role = prefs.getString("pending_role", "") ?: ""
        val name = prefs.getString("signup_name", "User") ?: "User"
        val lga = prefs.getString("signup_lga", "LGA") ?: "LGA"
        findViewById<TextView>(R.id.profileName).text = name
        findViewById<TextView>(R.id.profileMeta).text = "$lga"

        val sectionAccount: View = findViewById(R.id.sectionAccount)
        val sectionLanguage: View = findViewById(R.id.sectionLanguage)
        val sectionDataSync: View = findViewById(R.id.sectionDataSync)
        val sectionNotifications: View = findViewById(R.id.sectionNotifications)
        val sectionPrivacy: View = findViewById(R.id.sectionPrivacy)
        val sectionSupport: View = findViewById(R.id.sectionSupport)

        when (role) {
            "BENEFICIARY_PICKER" -> {
                sectionAccount.visibility = View.VISIBLE
                sectionLanguage.visibility = View.VISIBLE
                sectionDataSync.visibility = View.GONE
                sectionNotifications.visibility = View.GONE
                sectionPrivacy.visibility = View.VISIBLE
                sectionSupport.visibility = View.VISIBLE
            }
            "PENDING_FIELD_OPERATOR" -> {
                sectionAccount.visibility = View.VISIBLE
                sectionLanguage.visibility = View.VISIBLE
                sectionDataSync.visibility = View.VISIBLE
                sectionNotifications.visibility = View.VISIBLE
                sectionPrivacy.visibility = View.GONE
                sectionSupport.visibility = View.VISIBLE
            }
            else -> {
                sectionAccount.visibility = View.VISIBLE
                sectionLanguage.visibility = View.VISIBLE
                sectionDataSync.visibility = View.VISIBLE
                sectionNotifications.visibility = View.VISIBLE
                sectionPrivacy.visibility = View.VISIBLE
                sectionSupport.visibility = View.VISIBLE
            }
        }

        findViewById<MaterialButton>(R.id.logoutBtn)?.setOnClickListener {
            prefs.edit().remove("pending_role").apply()
            finish()
        }

        val apiEdit = findViewById<EditText>(R.id.apiBaseUrlEdit)
        val saveApiBtn = findViewById<MaterialButton>(R.id.saveApiBaseUrlBtn)
        if (apiEdit != null && saveApiBtn != null) {
            val current = prefs.getString("api_base_url", BuildConfig.BASE_URL) ?: BuildConfig.BASE_URL
            apiEdit.setText(current)
            saveApiBtn.setOnClickListener {
                val v = apiEdit.text?.toString()?.trim() ?: ""
                val clean = if (v.endsWith("/")) v.dropLast(1) else v
                if (clean.isNotEmpty()) {
                    prefs.edit().putString("api_base_url", clean).apply()
                    android.widget.Toast.makeText(this, "API URL saved", android.widget.Toast.LENGTH_SHORT).show()
                } else {
                    android.widget.Toast.makeText(this, "Enter a valid API URL", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
