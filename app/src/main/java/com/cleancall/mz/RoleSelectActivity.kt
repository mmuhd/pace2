package com.cleancall.mz

import android.os.Bundle
import com.google.android.material.card.MaterialCardView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Toast
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class RoleSelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_role_select)

        val tileField: MaterialCardView = findViewById(R.id.tileFieldOperator)
        val tilePicker: MaterialCardView = findViewById(R.id.tilePicker)
        val tileInvite: MaterialCardView = findViewById(R.id.tileInvite)
        val inviteCode: TextView = findViewById(R.id.inviteCodeEdit)
        val continueBtn: TextView = findViewById(R.id.continueBtn)

        val prefs = getSharedPreferences("clean_call", MODE_PRIVATE)

        var selected: MaterialCardView? = null
        fun select(card: MaterialCardView) {
            selected?.strokeColor = getColor(R.color.brand_dark_50)
            selected?.setCardBackgroundColor(getColor(android.R.color.white))
            card.strokeColor = getColor(R.color.brand_green)
            card.setCardBackgroundColor(getColor(android.R.color.white))
            selected = card
        }

        fun performRegister(roleValue: String, code: String?) {
            val name = prefs.getString("signup_name", null)
            val phone = prefs.getString("signup_phone", null)
            val pass = prefs.getString("signup_password", null)
            val email = prefs.getString("signup_email", null)
            val lga = prefs.getString("signup_lga", null)
            if (name.isNullOrEmpty() || phone.isNullOrEmpty() || pass.isNullOrEmpty() || lga.isNullOrEmpty()) {
                Toast.makeText(this, "Incomplete signup details", Toast.LENGTH_SHORT).show()
                return
            }
            tileField.isEnabled = false
            tilePicker.isEnabled = false
            tileInvite.isEnabled = false
            continueBtn.isEnabled = false
            Thread {
                val base = prefs.getString("api_base_url", BuildConfig.BASE_URL) ?: BuildConfig.BASE_URL
                val url = (if (base.endsWith("/")) base.dropLast(1) else base) + "/auth/register"
                val payload = JSONObject().apply {
                    put("name", name)
                    put("phone", phone)
                    put("password", pass)
                    put("lga", lga)
                    if (!email.isNullOrEmpty()) put("email", email)
                    put("role", roleValue)
                    if (!code.isNullOrEmpty()) put("invitation_code", code)
                }.toString()
                val body: RequestBody = payload.toRequestBody("application/json; charset=utf-8".toMediaType())
                val req = Request.Builder().url(url).post(body).addHeader("Accept", "application/json").build()
                val ok = try {
                    val resp = OkHttpClient.Builder().build().newCall(req).execute()
                    val s = resp.body?.string().orEmpty()
                    if (resp.isSuccessful) {
                        val json = JSONObject(s)
                        val token = json.optString("token")
                        val user = json.optJSONObject("user")
                        if (token.isNotEmpty() && user != null) {
                            val name2 = user.optString("name", name)
                            val lga2 = user.optString("lga", lga)
                            val role2 = user.optString("role", roleValue)
                            prefs.edit()
                                .putString("api_token", token)
                                .putString("user_name", name2)
                                .putString("user_lga", lga2)
                                .putString("user_role", role2)
                                .apply()
                            true
                        } else false
                    } else false
                } catch (_: Exception) { false }
                runOnUiThread {
                    tileField.isEnabled = true
                    tilePicker.isEnabled = true
                    tileInvite.isEnabled = true
                    continueBtn.isEnabled = true
                    if (ok) {
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
        }

        tileField.setOnClickListener {
            select(tileField)
            prefs.edit().putString("pending_role", "PENDING_FIELD_OPERATOR").apply()
            performRegister("PENDING_FIELD_OPERATOR", null)
        }

        tilePicker.setOnClickListener {
            select(tilePicker)
            prefs.edit().putString("pending_role", "BENEFICIARY_PICKER").apply()
            performRegister("BENEFICIARY_PICKER", null)
        }

        continueBtn.setOnClickListener {
            val code = inviteCode.text?.toString()?.trim().orEmpty()
            prefs.edit().putString("invitation_code", code).putString("pending_role", "INVITE_PENDING").apply()
            performRegister("INVITE_PENDING", code)
        }

        tileInvite.setOnClickListener { select(tileInvite) }
    }
}
