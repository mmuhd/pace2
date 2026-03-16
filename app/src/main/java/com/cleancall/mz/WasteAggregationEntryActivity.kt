package com.cleancall.mz

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import androidx.activity.result.contract.ActivityResultContracts
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class WasteAggregationEntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waste_aggregation_entry)

        val prefs = getSharedPreferences("clean_call", MODE_PRIVATE)
        val recordedBy = prefs.getString("signup_name", "User") ?: "User"

        val lgaSpinner: Spinner = findViewById(R.id.lgaSpinner)
        val sourceSpinner: Spinner = findViewById(R.id.sourceSpinner)
        val siteNameEdit: TextInputEditText = findViewById(R.id.siteNameEdit)
        val collectionDateText: TextView = findViewById(R.id.collectionDateText)
        val teamEdit: TextInputEditText = findViewById(R.id.teamEdit)

        val totalWasteEdit: TextInputEditText = findViewById(R.id.totalWasteEdit)
        val recyclablePctEdit: TextInputEditText = findViewById(R.id.recyclablePctEdit)
        val plasticEdit: TextInputEditText = findViewById(R.id.plasticEdit)
        val paperEdit: TextInputEditText = findViewById(R.id.paperEdit)
        val metalEdit: TextInputEditText = findViewById(R.id.metalEdit)
        val glassEdit: TextInputEditText = findViewById(R.id.glassEdit)
        val organicEdit: TextInputEditText = findViewById(R.id.organicEdit)
        val otherEdit: TextInputEditText = findViewById(R.id.otherEdit)

        val finalDisposalSpinner: Spinner = findViewById(R.id.finalDisposalSpinner)
        val transportSpinner: Spinner = findViewById(R.id.transportSpinner)
        val recyclersEdit: TextInputEditText = findViewById(R.id.recyclersEdit)

        val simpleSwitch: Switch = findViewById(R.id.simpleSwitch)
        val simpleMetricsCard: View = findViewById(R.id.simpleMetricsCard)
        val recyclablePctLabel: View = findViewById(R.id.recyclablePctLabel)
        val recyclablePctTil: View = findViewById(R.id.recyclablePctTil)
        val breakdownLabel: View = findViewById(R.id.breakdownLabel)
        val rowPlastic: View = findViewById(R.id.rowPlastic)
        val rowPaper: View = findViewById(R.id.rowPaper)
        val rowMetal: View = findViewById(R.id.rowMetal)
        val rowGlass: View = findViewById(R.id.rowGlass)
        val rowOrganic: View = findViewById(R.id.rowOrganic)
        val rowOther: View = findViewById(R.id.rowOther)

        val tripCountEdit: TextInputEditText = findViewById(R.id.tripCountEdit)
        val vehicleCountEdit: TextInputEditText = findViewById(R.id.vehicleCountEdit)
        val hoursWorkedEdit: TextInputEditText = findViewById(R.id.hoursWorkedEdit)
        val avgLoadKgEdit: TextInputEditText = findViewById(R.id.avgLoadKgEdit)
        val staffCountEdit: TextInputEditText = findViewById(R.id.staffCountEdit)
        val moisturePctSimpleEdit: TextInputEditText = findViewById(R.id.moisturePctSimpleEdit)
        val contaminationPctSimpleEdit: TextInputEditText = findViewById(R.id.contaminationPctSimpleEdit)
        val weatherEdit: TextInputEditText = findViewById(R.id.weatherEdit)

        val hazardYes: RadioButton = findViewById(R.id.hazardYes)
        val hazardNo: RadioButton = findViewById(R.id.hazardNo)
        val hazardDescRow: View = findViewById(R.id.hazardDescRow)
        val hazardDescEdit: TextInputEditText = findViewById(R.id.hazardDescEdit)

        val chAccess: CheckBox = findViewById(R.id.chAccess)
        val chNoPpe: CheckBox = findViewById(R.id.chNoPpe)
        val chLowTurnout: CheckBox = findViewById(R.id.chLowTurnout)
        val chDispersed: CheckBox = findViewById(R.id.chDispersed)
        val chManpower: CheckBox = findViewById(R.id.chManpower)
        val chNoScale: CheckBox = findViewById(R.id.chNoScale)
        val chOther: CheckBox = findViewById(R.id.chOther)

        val remarksEdit: TextInputEditText = findViewById(R.id.remarksEdit)

        val photoPreview1: ImageView = findViewById(R.id.photoPreview1)
        val photoPreview2: ImageView = findViewById(R.id.photoPreview2)
        val photoPreview3: ImageView = findViewById(R.id.photoPreview3)
        val choosePhotoBtn1: MaterialButton = findViewById(R.id.choosePhotoBtn1)
        val capturePhotoBtn1: MaterialButton = findViewById(R.id.capturePhotoBtn1)
        val choosePhotoBtn2: MaterialButton = findViewById(R.id.choosePhotoBtn2)
        val capturePhotoBtn2: MaterialButton = findViewById(R.id.capturePhotoBtn2)
        val choosePhotoBtn3: MaterialButton = findViewById(R.id.choosePhotoBtn3)
        val capturePhotoBtn3: MaterialButton = findViewById(R.id.capturePhotoBtn3)

        val recordedByText: TextView = findViewById(R.id.recordedByText)
        recordedByText.text = recordedBy

        val saveBtn: MaterialButton = findViewById(R.id.saveBtn)
        val saveAnotherBtn: MaterialButton = findViewById(R.id.saveAnotherBtn)
        val cancelBtn: TextView = findViewById(R.id.cancelBtn)

        val lgas = listOf("Select", "Dala", "Fagge", "Gwale", "Kano Municipal", "Nassarawa", "Tarauni", "Kumbotso", "Ungogo", "Other")
        lgaSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, lgas).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }

        val sources = listOf("Select", "Dumpsite", "Street collection", "Households", "Schools", "Markets", "Business areas", "Community clean-up", "Other")
        sourceSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, sources).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }

        val disposals = listOf("Select", "Government Dumpsite", "Temporary Community Dump", "Recycling Plant", "Waste Transfer Station", "Other")
        finalDisposalSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, disposals).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }

        val transports = listOf("Select", "REMASAB compactor", "Cart pushers", "Tricycle", "Truck", "Other")
        transportSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, transports).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }

        val cal = Calendar.getInstance()
        val dateFmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        collectionDateText.setOnClickListener {
            DatePickerDialog(this, { _, y, m, d ->
                val c = Calendar.getInstance(); c.set(y, m, d)
                collectionDateText.text = dateFmt.format(c.time)
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        hazardYes.setOnCheckedChangeListener { _, checked -> hazardDescRow.visibility = if (checked) View.VISIBLE else View.GONE }
        hazardNo.setOnCheckedChangeListener { _, checked -> if (checked) hazardDescRow.visibility = View.GONE }

        fun applyMode(isSimple: Boolean) {
            simpleMetricsCard.visibility = if (isSimple) View.VISIBLE else View.GONE
            val detailedVis = if (isSimple) View.GONE else View.VISIBLE
            recyclablePctLabel.visibility = detailedVis
            recyclablePctTil.visibility = detailedVis
            breakdownLabel.visibility = detailedVis
            rowPlastic.visibility = detailedVis
            rowPaper.visibility = detailedVis
            rowMetal.visibility = detailedVis
            rowGlass.visibility = detailedVis
            rowOrganic.visibility = detailedVis
            rowOther.visibility = detailedVis
        }
        applyMode(false)
        simpleSwitch.setOnCheckedChangeListener { _, isChecked -> applyMode(isChecked) }

        var photo1: String? = null
        var photo2: String? = null
        var photo3: String? = null

        fun bmpToBase64(bmp: Bitmap): String {
            val out = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, out)
            val b = out.toByteArray()
            return Base64.encodeToString(b, Base64.NO_WRAP)
        }

        val pick1 = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val input = contentResolver.openInputStream(uri)
                val bytes = input?.readBytes(); input?.close()
                if (bytes != null) {
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    photo1 = bmpToBase64(bmp)
                    photoPreview1.setImageBitmap(bmp)
                }
            }
        }
        val pick2 = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val input = contentResolver.openInputStream(uri)
                val bytes = input?.readBytes(); input?.close()
                if (bytes != null) {
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    photo2 = bmpToBase64(bmp)
                    photoPreview2.setImageBitmap(bmp)
                }
            }
        }
        val pick3 = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val input = contentResolver.openInputStream(uri)
                val bytes = input?.readBytes(); input?.close()
                if (bytes != null) {
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    photo3 = bmpToBase64(bmp)
                    photoPreview3.setImageBitmap(bmp)
                }
            }
        }

        val cap1 = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp ->
            if (bmp != null) { photo1 = bmpToBase64(bmp); photoPreview1.setImageBitmap(bmp) }
        }
        val cap2 = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp ->
            if (bmp != null) { photo2 = bmpToBase64(bmp); photoPreview2.setImageBitmap(bmp) }
        }
        val cap3 = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp ->
            if (bmp != null) { photo3 = bmpToBase64(bmp); photoPreview3.setImageBitmap(bmp) }
        }

        choosePhotoBtn1.setOnClickListener { pick1.launch("image/*") }
        capturePhotoBtn1.setOnClickListener { cap1.launch(null) }
        choosePhotoBtn2.setOnClickListener { pick2.launch("image/*") }
        capturePhotoBtn2.setOnClickListener { cap2.launch(null) }
        choosePhotoBtn3.setOnClickListener { pick3.launch("image/*") }
        capturePhotoBtn3.setOnClickListener { cap3.launch(null) }

        fun toD(s: String?): Double? = s?.trim()?.toDoubleOrNull()
        fun toI(s: String?): Int? = s?.trim()?.toIntOrNull()

        fun validateRequired(): Boolean {
            if (lgaSpinner.selectedItemPosition == 0) {
                Toast.makeText(this, "Please select LGA", Toast.LENGTH_SHORT).show(); return false
            }
            if (sourceSpinner.selectedItemPosition == 0) {
                Toast.makeText(this, "Please select waste source", Toast.LENGTH_SHORT).show(); return false
            }
            if (siteNameEdit.text?.toString()?.trim().isNullOrEmpty()) {
                Toast.makeText(this, "Site / Community Name is required", Toast.LENGTH_SHORT).show(); return false
            }
            if (collectionDateText.text.isNullOrEmpty()) {
                Toast.makeText(this, "Select collection date", Toast.LENGTH_SHORT).show(); return false
            }
            val total = toD(totalWasteEdit.text?.toString())
            if (total == null) { Toast.makeText(this, "Enter total waste (kg)", Toast.LENGTH_SHORT).show(); return false }
            if (!simpleSwitch.isChecked) {
                val pct = toI(recyclablePctEdit.text?.toString())
                if (pct == null || pct !in 0..100) { Toast.makeText(this, "Enter recyclable % (0–100)", Toast.LENGTH_SHORT).show(); return false }
            }
            if (finalDisposalSpinner.selectedItemPosition == 0) {
                Toast.makeText(this, "Select final disposal site", Toast.LENGTH_SHORT).show(); return false
            }
            return true
        }

        fun clearForm() {
            lgaSpinner.setSelection(0)
            sourceSpinner.setSelection(0)
            siteNameEdit.setText("")
            collectionDateText.text = ""
            teamEdit.setText("")
            totalWasteEdit.setText("")
            recyclablePctEdit.setText("")
            plasticEdit.setText("")
            paperEdit.setText("")
            metalEdit.setText("")
            glassEdit.setText("")
            organicEdit.setText("")
            otherEdit.setText("")
            finalDisposalSpinner.setSelection(0)
            transportSpinner.setSelection(0)
            recyclersEdit.setText("")
            tripCountEdit.setText("")
            vehicleCountEdit.setText("")
            hoursWorkedEdit.setText("")
            avgLoadKgEdit.setText("")
            staffCountEdit.setText("")
            moisturePctSimpleEdit.setText("")
            contaminationPctSimpleEdit.setText("")
            weatherEdit.setText("")
            simpleSwitch.isChecked = false
            hazardYes.isChecked = false
            hazardNo.isChecked = false
            hazardDescEdit.setText("")
            chAccess.isChecked = false
            chNoPpe.isChecked = false
            chLowTurnout.isChecked = false
            chDispersed.isChecked = false
            chManpower.isChecked = false
            chNoScale.isChecked = false
            chOther.isChecked = false
            remarksEdit.setText("")
            photo1 = null; photo2 = null; photo3 = null
            photoPreview1.setImageResource(R.drawable.ic_person)
            photoPreview2.setImageResource(R.drawable.ic_person)
            photoPreview3.setImageResource(R.drawable.ic_person)
        }

        fun buildReport(): WasteAggregationReport {
            val now = System.currentTimeMillis()
            val hazards = hazardYes.isChecked
            val challenges = mutableListOf<String>().apply {
                if (chAccess.isChecked) add("Poor access road")
                if (chNoPpe.isChecked) add("No PPE")
                if (chLowTurnout.isChecked) add("Low turnout")
                if (chDispersed.isChecked) add("Waste too dispersed")
                if (chManpower.isChecked) add("Insufficient manpower")
                if (chNoScale.isChecked) add("No weighing scale")
                if (chOther.isChecked) add("Other")
            }
            val photos = listOfNotNull(photo1, photo2, photo3)
            return WasteAggregationReport(
                id = UUID.randomUUID().toString(),
                lga = lgaSpinner.selectedItem?.toString().orEmpty(),
                wasteSource = sourceSpinner.selectedItem?.toString().orEmpty(),
                siteName = siteNameEdit.text?.toString()?.trim().orEmpty(),
                collectionDate = collectionDateText.text?.toString()?.trim().orEmpty(),
                team = teamEdit.text?.toString()?.trim()?.ifEmpty { null },
                totalWasteKg = toD(totalWasteEdit.text?.toString()) ?: 0.0,
                recyclablePercentage = toI(recyclablePctEdit.text?.toString()) ?: 0,
                plasticKg = toD(plasticEdit.text?.toString()),
                paperKg = toD(paperEdit.text?.toString()),
                metalKg = toD(metalEdit.text?.toString()),
                glassKg = toD(glassEdit.text?.toString()),
                organicKg = toD(organicEdit.text?.toString()),
                otherKg = toD(otherEdit.text?.toString()),
                finalDisposalSite = finalDisposalSpinner.selectedItem?.toString().orEmpty(),
                transportUsed = transportSpinner.selectedItemPosition.takeIf { it > 0 }?.let { transportSpinner.selectedItem?.toString() },
                recyclersInvolved = recyclersEdit.text?.toString()?.trim()?.ifEmpty { null },
                tripCount = toI(tripCountEdit.text?.toString()),
                vehicleCount = toI(vehicleCountEdit.text?.toString()),
                hoursWorked = toD(hoursWorkedEdit.text?.toString()),
                avgLoadKg = toD(avgLoadKgEdit.text?.toString()),
                staffCount = toI(staffCountEdit.text?.toString()),
                moisturePercent = toI(moisturePctSimpleEdit.text?.toString()),
                contaminationPercent = toI(contaminationPctSimpleEdit.text?.toString()),
                weather = weatherEdit.text?.toString()?.trim()?.ifEmpty { null },
                hazardousFound = hazards,
                hazardousDescription = hazardDescEdit.text?.toString()?.trim()?.ifEmpty { null },
                challenges = challenges,
                remarks = remarksEdit.text?.toString()?.trim()?.ifEmpty { null },
                photos = photos,
                recordedByUserId = recordedBy,
                gpsLat = null,
                gpsLong = null,
                createdAt = now,
                updatedAt = now
            )
        }

        saveBtn.setOnClickListener {
            if (!validateRequired()) return@setOnClickListener
            val report = buildReport()
            saveBtn.isEnabled = false
            saveAnotherBtn.isEnabled = false
            Thread {
                val ok = try { ApiClient.postWasteAggregation(this, report) } catch (_: Exception) { false }
                runOnUiThread {
                    saveBtn.isEnabled = true
                    saveAnotherBtn.isEnabled = true
                    if (ok) {
                        MaterialAlertDialogBuilder(this)
                            .setTitle("Report saved successfully.")
                            .setMessage("Choose next action")
                            .setPositiveButton("View Reports") { dlg, _ -> dlg.dismiss(); startActivity(android.content.Intent(this, WasteAggregationListActivity::class.java)); finish() }
                            .setNegativeButton("Add Another") { dlg, _ -> dlg.dismiss(); clearForm() }
                            .show()
                    } else {
                        WasteAggregationStore.savePending(this, report)
                        MaterialAlertDialogBuilder(this)
                            .setTitle("Saved locally due to network/auth issue.")
                            .setMessage("Choose next action")
                            .setPositiveButton("View Reports") { dlg, _ -> dlg.dismiss(); startActivity(android.content.Intent(this, WasteAggregationListActivity::class.java)); finish() }
                            .setNegativeButton("Add Another") { dlg, _ -> dlg.dismiss(); clearForm() }
                            .show()
                    }
                }
            }.start()
        }

        saveAnotherBtn.setOnClickListener {
            if (!validateRequired()) return@setOnClickListener
            val report = buildReport()
            saveBtn.isEnabled = false
            saveAnotherBtn.isEnabled = false
            Thread {
                val ok = try { ApiClient.postWasteAggregation(this, report) } catch (_: Exception) { false }
                runOnUiThread {
                    saveBtn.isEnabled = true
                    saveAnotherBtn.isEnabled = true
                    if (ok) {
                        Toast.makeText(this, "Report saved successfully.", Toast.LENGTH_SHORT).show()
                        clearForm()
                    } else {
                        WasteAggregationStore.savePending(this, report)
                        Toast.makeText(this, "Saved locally due to network/auth issue.", Toast.LENGTH_SHORT).show()
                        clearForm()
                    }
                }
            }.start()
        }

        cancelBtn.setOnClickListener { finish() }
    }
}
