package com.cleancall.mz

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.UUID

class EvacuationEntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evacuation_entry)

        findViewById<View>(R.id.backButton).setOnClickListener { finish() }

        val sourceTypeSpinner: android.widget.Spinner = findViewById(R.id.sourceTypeSpinner)
        val sourceNameEdit: TextInputEditText = findViewById(R.id.sourceNameEdit)
        val lgaSpinner: android.widget.Spinner = findViewById(R.id.lgaSpinner)
        val addressEdit: TextInputEditText = findViewById(R.id.addressEdit)
        val scheduledEdit: TextInputEditText = findViewById(R.id.scheduledEdit)
        val assignedEdit: TextInputEditText = findViewById(R.id.assignedEdit)
        val contamEdit: TextInputEditText = findViewById(R.id.contamEdit)
        val saveBtn: MaterialButton = findViewById(R.id.saveBtn)

        val kgPlastic: TextInputEditText = findViewById(R.id.kgPlastic)
        val kgPaper: TextInputEditText = findViewById(R.id.kgPaper)
        val kgMetal: TextInputEditText = findViewById(R.id.kgMetal)
        val kgGlass: TextInputEditText = findViewById(R.id.kgGlass)
        val kgOrganic: TextInputEditText = findViewById(R.id.kgOrganic)
        val kgOther: TextInputEditText = findViewById(R.id.kgOther)

        sourceTypeSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, listOf("Select","Household","School","Aggregation","Public bin")).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }
        lgaSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, listOf("Select","Dala","Fagge","Gwale","Kano Municipal","Nassarawa","Tarauni","Kumbotso","Ungogo","Other")).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }

        fun parseKg(s: String?): Double {
            return s?.trim()?.toDoubleOrNull() ?: 0.0
        }

        saveBtn.setOnClickListener {
            if (sourceTypeSpinner.selectedItemPosition == 0) {
                android.widget.Toast.makeText(this, "Select source type", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (lgaSpinner.selectedItemPosition == 0) {
                android.widget.Toast.makeText(this, "Select LGA", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val materials = listOf(
                "Plastic" to parseKg(kgPlastic.text?.toString()),
                "Paper/Cardboard" to parseKg(kgPaper.text?.toString()),
                "Metal" to parseKg(kgMetal.text?.toString()),
                "Glass" to parseKg(kgGlass.text?.toString()),
                "Organic" to parseKg(kgOrganic.text?.toString()),
                "Other" to parseKg(kgOther.text?.toString())
            ).filter { it.second > 0.0 }
            val total = materials.sumOf { it.second }.let { if (it <= 0.0) null else it }
            val contam = contamEdit.text?.toString()?.trim()?.toIntOrNull()
            val task = EvacuationTask(
                id = UUID.randomUUID().toString(),
                sourceType = sourceTypeSpinner.selectedItem?.toString().orEmpty(),
                sourceName = sourceNameEdit.text?.toString()?.trim().orEmpty().ifEmpty { null },
                lga = lgaSpinner.selectedItem?.toString().orEmpty(),
                address = addressEdit.text?.toString()?.trim().orEmpty().ifEmpty { null },
                scheduledAt = scheduledEdit.text?.toString()?.trim().orEmpty().ifEmpty { null },
                assignedTo = assignedEdit.text?.toString()?.trim().orEmpty().ifEmpty { null },
                status = "collected",
                totalKg = total,
                breakdown = materials,
                contaminationScore = contam,
                photoBase64s = emptyList(),
                gpsLat = null,
                gpsLong = null,
                createdByUserId = getSharedPreferences("clean_call", MODE_PRIVATE).getString("user_name", null),
                createdAt = System.currentTimeMillis()
            )
            saveBtn.isEnabled = false
            Thread {
                var status = -1
                var body = ""
                val ok = try {
                    val res = ApiClient.postEvacuationTask(this, task)
                    status = res.first
                    body = res.second
                    status in 200..299
                } catch (_: Exception) { false }
                runOnUiThread {
                    saveBtn.isEnabled = true
                    if (ok) {
                        android.widget.Toast.makeText(this, "Pickup saved to backend", android.widget.Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        EvacuationStore.savePending(this, task)
                        androidx.appcompat.app.AlertDialog.Builder(this)
                            .setTitle("Saved locally due to network/auth issue.")
                            .setMessage("Status: " + status + "\nBody: " + body.take(300))
                            .setPositiveButton("OK", null)
                            .show()
                    }
                }
            }.start()
        }
    }
}
