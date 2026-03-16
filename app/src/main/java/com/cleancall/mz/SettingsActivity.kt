package com.cleancall.mz

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.EditText
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import okhttp3.OkHttpClient
import okhttp3.Request

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<View>(R.id.backButton).setOnClickListener { finish() }

        val prefs = getSharedPreferences("clean_call", MODE_PRIVATE)
        val role = prefs.getString("user_role", prefs.getString("pending_role", "") ?: "") ?: ""
        val name = prefs.getString("user_name", prefs.getString("signup_name", "User") ?: "User") ?: "User"
        val lga = prefs.getString("user_lga", prefs.getString("signup_lga", "LGA") ?: "LGA") ?: "LGA"
        findViewById<TextView>(R.id.profileName).text = name
        findViewById<TextView>(R.id.profileMeta).text = "$lga • $role"

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
            val btn = it as MaterialButton
            btn.isEnabled = false
            Thread {
                val token = prefs.getString("api_token", null)
                if (!token.isNullOrEmpty()) {
                    val client = OkHttpClient.Builder().build()
                    val base = prefs.getString("api_base_url", BuildConfig.BASE_URL) ?: BuildConfig.BASE_URL
                    val url = (if (base.endsWith("/")) base.dropLast(1) else base) + "/auth/logout"
                    val req = Request.Builder()
                        .url(url)
                        .post(okhttp3.RequestBody.create(null, ByteArray(0)))
                        .addHeader("Accept","application/json")
                        .addHeader("Authorization","Bearer $token")
                        .build()
                    try { client.newCall(req).execute() } catch (_: Exception) {}
                }
                runOnUiThread {
                    prefs.edit()
                        .remove("api_token")
                        .remove("user_name")
                        .remove("user_lga")
                        .remove("user_role")
                        .remove("pending_role")
                        .remove("signup_name")
                        .remove("signup_lga")
                        .remove("signup_email")
                        .remove("signup_phone")
                        .remove("signup_password")
                        .remove("invitation_code")
                        .apply()
                    val intent = android.content.Intent(this, MainActivity::class.java)
                    intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }
            }.start()
        }

        val langSpinner = findViewById<Spinner>(R.id.languageSpinner)
        val sizeSpinner = findViewById<Spinner>(R.id.textSizeSpinner)
        if (langSpinner != null) {
            val langs = listOf("English")
            val adapter = ArrayAdapter(this, R.layout.spinner_item, langs)
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            langSpinner.adapter = adapter
            langSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    prefs.edit().putString("pref_lang", langs[position]).apply()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }
        if (sizeSpinner != null) {
            val sizes = listOf("Small", "Medium", "Large", "Extra Large")
            val adapter = ArrayAdapter(this, R.layout.spinner_item, sizes)
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            sizeSpinner.adapter = adapter
            val currentSize = prefs.getString("pref_text_size", "Medium")
            val idx = sizes.indexOf(currentSize)
            var initializingSize = true
            if (idx >= 0) sizeSpinner.setSelection(idx)
            sizeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    if (initializingSize) { initializingSize = false; return }
                    prefs.edit().putString("pref_text_size", sizes[position]).apply()
                    val scale = when (sizes[position]) { "Small" -> 0.85f; "Large" -> 1.15f; "Extra Large" -> 1.30f; else -> 1.0f }
                    val res = resources
                    val config = android.content.res.Configuration(res.configuration)
                    config.fontScale = scale
                    res.updateConfiguration(config, res.displayMetrics)
                    recreate()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }

        findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.accountNameEdit)?.setText(name)

        val syncBtn = findViewById<com.google.android.material.button.MaterialButton>(R.id.syncNowBtn)
        val syncProgress = findViewById<View>(R.id.syncProgress)
        if (syncBtn != null && syncProgress != null) {
            syncBtn.setOnClickListener {
                syncBtn.isEnabled = false
                syncProgress.visibility = View.VISIBLE
                Thread {
                    val prefs2 = getSharedPreferences("clean_call", MODE_PRIVATE)
                    val token = prefs2.getString("api_token", null)
                    if (token.isNullOrEmpty()) {
                        runOnUiThread {
                            syncProgress.visibility = View.GONE
                            syncBtn.isEnabled = true
                            Toast.makeText(this, "Login required to sync", Toast.LENGTH_SHORT).show()
                        }
                        return@Thread
                    }
                    try {
                        val base = prefs2.getString("api_base_url", BuildConfig.BASE_URL) ?: BuildConfig.BASE_URL
                        val url = (if (base.endsWith("/")) base.dropLast(1) else base) + "/auth/me"
                        val req = Request.Builder().url(url).get().addHeader("Accept","application/json").addHeader("Authorization","Bearer $token").build()
                        val client = OkHttpClient.Builder().build()
                        val resp = client.newCall(req).execute()
                        if (!resp.isSuccessful) {
                            runOnUiThread {
                                syncProgress.visibility = View.GONE
                                syncBtn.isEnabled = true
                                Toast.makeText(this, "Login required to sync", Toast.LENGTH_SHORT).show()
                            }
                            return@Thread
                        }
                    } catch (_: Exception) {}
                    var count = 0
                    try { PickerStore.migrateLegacyLocalToPending(this) } catch (_: Exception) {}
                    try { count = PickerStore.syncPending(this) } catch (_: Exception) {}
                    var evacCount = 0
                    try { evacCount = EvacuationStore.syncPending(this) } catch (_: Exception) {}
                    var aggCount = 0
                    try { aggCount = WasteAggregationStore.syncPending(this) } catch (_: Exception) {}
                    var schoolCount = 0
                    try { schoolCount = SchoolWasteBankStore.syncPending(this) } catch (_: Exception) {}
                    var commCount = 0
                    try { commCount = StakeholderCommitmentStore.syncPending(this) } catch (_: Exception) {}
                    val log = PickerStore.getSyncLog(this)
                    runOnUiThread {
                        syncProgress.visibility = View.GONE
                        syncBtn.isEnabled = true
                        if ((count + evacCount + aggCount + schoolCount + commCount) == 0 && log.isNotBlank()) {
                            androidx.appcompat.app.AlertDialog.Builder(this)
                                .setTitle("Sync details")
                                .setMessage(log)
                                .setPositiveButton("OK", null)
                                .show()
                        } else {
                            Toast.makeText(this, "Synced " + (count + evacCount + aggCount + schoolCount + commCount) + " pending records", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.start()
            }
        }
    }
}
