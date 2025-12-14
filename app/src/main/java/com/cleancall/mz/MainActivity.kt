package com.cleancall.mz

import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<com.google.android.material.button.MaterialButton>(R.id.joinButton).setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        findViewById<android.widget.TextView>(R.id.loginLink).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
