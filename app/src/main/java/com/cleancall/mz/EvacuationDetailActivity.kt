package com.cleancall.mz

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class EvacuationDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evacuation_detail)

        findViewById<View>(R.id.backButton).setOnClickListener { finish() }
        val statusSpinner: android.widget.Spinner = findViewById(R.id.statusSpinner)
        val assignedEdit: TextInputEditText = findViewById(R.id.assignedEdit)
        val saveBtn: MaterialButton = findViewById(R.id.saveBtn)
        val id = intent.getStringExtra("evac_id") ?: ""
        val source = intent.getStringExtra("evac_source") ?: ""
        val lga = intent.getStringExtra("evac_lga") ?: ""
        val status = intent.getStringExtra("evac_status") ?: "pending"
        val assigned = intent.getStringExtra("evac_assigned") ?: ""
        findViewById<android.widget.TextView>(R.id.detailTitle).text = "$source • $lga"

        statusSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, listOf("pending","collected","delivered")).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }
        val idx = listOf("pending","collected","delivered").indexOf(status)
        if (idx >= 0) statusSpinner.setSelection(idx)
        assignedEdit.setText(assigned)

        saveBtn.setOnClickListener {
            saveBtn.isEnabled = false
            val newStatus = statusSpinner.selectedItem?.toString() ?: "pending"
            val newAssigned = assignedEdit.text?.toString()?.trim()
            Thread {
                var ok = false
                var code = -1
                var body = ""
                try {
                    val res = ApiClient.updateEvacuationTask(this, id, mapOf("status" to newStatus, "assigned_to" to newAssigned))
                    code = res.first
                    body = res.second
                    ok = code in 200..299
                } catch (_: Exception) {}
                runOnUiThread {
                    saveBtn.isEnabled = true
                    if (ok) {
                        android.widget.Toast.makeText(this, "Updated", android.widget.Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        androidx.appcompat.app.AlertDialog.Builder(this)
                            .setTitle("Update failed")
                            .setMessage("Status: " + code + "\nBody: " + body.take(300))
                            .setPositiveButton("OK", null)
                            .show()
                    }
                }
            }.start()
        }
    }
}
