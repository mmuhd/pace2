package com.cleancall.mz

import android.os.Bundle
import com.google.android.material.card.MaterialCardView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

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

        tileField.setOnClickListener {
            select(tileField)
            prefs.edit().putString("pending_role", "PENDING_FIELD_OPERATOR").apply()
            startActivity(Intent(this, HomeActivity::class.java))
        }

        tilePicker.setOnClickListener {
            select(tilePicker)
            prefs.edit().putString("pending_role", "BENEFICIARY_PICKER").apply()
            startActivity(Intent(this, HomeActivity::class.java))
        }

        continueBtn.setOnClickListener {
            val code = inviteCode.text?.toString()?.trim().orEmpty()
            prefs.edit().putString("invitation_code", code).putString("pending_role", "INVITE_PENDING").apply()
            startActivity(Intent(this, HomeActivity::class.java))
        }

        tileInvite.setOnClickListener { select(tileInvite) }
    }
}
