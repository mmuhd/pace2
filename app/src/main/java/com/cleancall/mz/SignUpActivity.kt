package com.cleancall.mz

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val back: ImageButton = findViewById(R.id.backButton)
        val spinnerLga: Spinner = findViewById(R.id.spinnerLga)
        val proceed: MaterialButton = findViewById(R.id.proceedButton)
        val nameEdit: TextView = findViewById(R.id.nameEdit)
        val emailEdit: TextView = findViewById(R.id.emailEdit)
        val phoneEdit: TextView = findViewById(R.id.phoneEdit)
        val passwordEdit: TextView = findViewById(R.id.passwordEdit)
        val confirmEdit: TextView = findViewById(R.id.confirmEdit)

        val lgas = listOf(
            "Select LGA",
            "Dala",
            "Fagge",
            "Gwale",
            "Kumbotso",
            "Municipal",
            "Nasarawa",
            "Tarauni",
            "Tofa",
            "Ungogo",
            "Dambatta",
            "Gezawa",
            "Kura",
            "Rano",
            "Wudil"
        )
        val lgaAdapter = ArrayAdapter(this, R.layout.spinner_item, lgas)
        lgaAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerLga.adapter = lgaAdapter

        back.setOnClickListener { finish() }
        proceed.setOnClickListener {
            val name = nameEdit.text?.toString()?.trim().orEmpty()
            val phone = phoneEdit.text?.toString()?.trim().orEmpty()
            val pass = passwordEdit.text?.toString()?.trim().orEmpty()
            val confirm = confirmEdit.text?.toString()?.trim().orEmpty()
            val email = emailEdit.text?.toString()?.trim().orEmpty()

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter full name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (phone.length < 10) {
                Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pass.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pass != confirm) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val lga = spinnerLga.selectedItem?.toString().orEmpty()
            if (spinnerLga.selectedItemPosition == 0) {
                Toast.makeText(this, "Please select LGA", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val prefs = getSharedPreferences("clean_call", MODE_PRIVATE)
            prefs.edit()
                .putString("signup_name", name)
                .putString("signup_phone", phone)
                .putString("signup_email", email)
                .putString("signup_lga", lga)
                .apply()

            startActivity(android.content.Intent(this, RoleSelectActivity::class.java))
        }
    }
}
