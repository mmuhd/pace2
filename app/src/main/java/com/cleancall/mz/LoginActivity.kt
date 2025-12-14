package com.cleancall.mz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import android.content.Intent
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val client = OkHttpClient.Builder().build()
        val loginBtn = findViewById<MaterialButton>(R.id.loginButton)
        val emailEdit = findViewById<TextInputEditText>(R.id.phoneEdit)
        val passwordEdit = findViewById<TextInputEditText>(R.id.passwordEdit)

        loginBtn.setOnClickListener {
            val email = emailEdit.text?.toString()?.trim().orEmpty()
            val password = passwordEdit.text?.toString()?.trim().orEmpty()
            if (email.isEmpty() || password.isEmpty()) {
                android.widget.Toast.makeText(this, "Enter email and password", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            loginBtn.isEnabled = false
            Thread {
                val prefs = getSharedPreferences("clean_call", MODE_PRIVATE)
                val base = prefs.getString("api_base_url", BuildConfig.BASE_URL) ?: BuildConfig.BASE_URL
                val url = (if (base.endsWith("/")) base.dropLast(1) else base) + "/auth/login"
                val payload = JSONObject(mapOf("email" to email, "password" to password)).toString()
                val body: RequestBody = payload.toRequestBody("application/json; charset=utf-8".toMediaType())
                val req = Request.Builder().url(url).post(body).addHeader("Accept", "application/json").build()
                val ok = try {
                    val resp = client.newCall(req).execute()
                    val s = resp.body?.string().orEmpty()
                    if (resp.isSuccessful) {
                        val json = JSONObject(s)
                        val token = json.optString("token")
                        if (token.isNotEmpty()) {
                            prefs.edit().putString("api_token", token).apply()
                        }
                        true
                    } else false
                } catch (_: Exception) { false }
                runOnUiThread {
                    loginBtn.isEnabled = true
                    if (ok) {
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    } else {
                        android.widget.Toast.makeText(this, "Login failed", android.widget.Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
        }
        findViewById<android.widget.TextView>(R.id.forgotPassword).setOnClickListener {
        }
    }
}
