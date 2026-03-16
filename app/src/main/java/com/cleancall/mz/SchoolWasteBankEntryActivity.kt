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

class SchoolWasteBankEntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_waste_bank_entry)

        val prefs = getSharedPreferences("clean_call", MODE_PRIVATE)
        val recordedBy = prefs.getString("signup_name", "User") ?: "User"

        val schoolNameEdit: TextInputEditText = findViewById(R.id.schoolNameEdit)
        val lgaSpinner: Spinner = findViewById(R.id.lgaSpinner)
        val periodTypeSpinner: Spinner = findViewById(R.id.periodTypeSpinner)
        val periodDailyDateText: TextView = findViewById(R.id.periodDailyDateText)
        val periodWeekRow: View = findViewById(R.id.periodWeekRow)
        val periodWeekStartText: TextView = findViewById(R.id.periodWeekStartText)
        val periodMonthRow: View = findViewById(R.id.periodMonthRow)
        val periodMonthText: TextView = findViewById(R.id.periodMonthText)
        val recordedByText: TextView = findViewById(R.id.recordedByText)
        val statusSpinner: Spinner = findViewById(R.id.statusSpinner)

        val plasticCollectedEdit: TextInputEditText = findViewById(R.id.plasticCollectedEdit)
        val plasticRecycledEdit: TextInputEditText = findViewById(R.id.plasticRecycledEdit)
        val paperCollectedEdit: TextInputEditText = findViewById(R.id.paperCollectedEdit)
        val paperRecycledEdit: TextInputEditText = findViewById(R.id.paperRecycledEdit)
        val metalCollectedEdit: TextInputEditText = findViewById(R.id.metalCollectedEdit)
        val metalRecycledEdit: TextInputEditText = findViewById(R.id.metalRecycledEdit)
        val glassCollectedEdit: TextInputEditText = findViewById(R.id.glassCollectedEdit)
        val glassRecycledEdit: TextInputEditText = findViewById(R.id.glassRecycledEdit)
        val organicCollectedEdit: TextInputEditText = findViewById(R.id.organicCollectedEdit)
        val organicRecycledEdit: TextInputEditText = findViewById(R.id.organicRecycledEdit)
        val otherTypeEdit: TextInputEditText = findViewById(R.id.otherTypeEdit)
        val otherCollectedEdit: TextInputEditText = findViewById(R.id.otherCollectedEdit)
        val otherRecycledEdit: TextInputEditText = findViewById(R.id.otherRecycledEdit)

        val soldYes: RadioButton = findViewById(R.id.soldYes)
        val soldNo: RadioButton = findViewById(R.id.soldNo)
        val incomeRow: View = findViewById(R.id.incomeRow)
        val incomeEdit: TextInputEditText = findViewById(R.id.incomeEdit)
        val buyerNameEdit: TextInputEditText = findViewById(R.id.buyerNameEdit)

        val chNoBuyers: CheckBox = findViewById(R.id.chNoBuyers)
        val chStorage: CheckBox = findViewById(R.id.chStorage)
        val chTransport: CheckBox = findViewById(R.id.chTransport)
        val chSorting: CheckBox = findViewById(R.id.chSorting)
        val chOther: CheckBox = findViewById(R.id.chOther)

        val participationSpinner: Spinner = findViewById(R.id.participationSpinner)
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

        val saveBtn: MaterialButton = findViewById(R.id.saveBtn)
        val saveAnotherBtn: MaterialButton = findViewById(R.id.saveAnotherBtn)
        val cancelBtn: TextView = findViewById(R.id.cancelBtn)

        recordedByText.text = recordedBy

        val lgas = listOf("Select", "Dala", "Fagge", "Gwale", "Kano Municipal", "Nassarawa", "Tarauni", "Kumbotso", "Ungogo", "Other")
        lgaSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, lgas).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }

        val periodTypes = listOf("Select", "Daily", "Weekly", "Monthly")
        periodTypeSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, periodTypes).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }

        val statuses = listOf("Select", "Active", "Inactive", "Paused")
        statusSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, statuses).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }

        val scale = listOf("Select", "1", "2", "3", "4", "5")
        participationSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, scale).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }

        periodWeekRow.visibility = View.GONE
        periodMonthRow.visibility = View.GONE

        periodTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    1 -> {
                        periodDailyDateText.visibility = View.VISIBLE
                        periodWeekRow.visibility = View.GONE
                        periodMonthRow.visibility = View.GONE
                    }
                    2 -> {
                        periodDailyDateText.visibility = View.GONE
                        periodWeekRow.visibility = View.VISIBLE
                        periodMonthRow.visibility = View.GONE
                    }
                    3 -> {
                        periodDailyDateText.visibility = View.GONE
                        periodWeekRow.visibility = View.GONE
                        periodMonthRow.visibility = View.VISIBLE
                    }
                    else -> {
                        periodDailyDateText.visibility = View.VISIBLE
                        periodWeekRow.visibility = View.GONE
                        periodMonthRow.visibility = View.GONE
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val cal = Calendar.getInstance()
        val dateFmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        periodDailyDateText.setOnClickListener {
            DatePickerDialog(this, { _, y, m, d ->
                val c = Calendar.getInstance(); c.set(y, m, d)
                periodDailyDateText.text = dateFmt.format(c.time)
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        periodWeekStartText.setOnClickListener {
            DatePickerDialog(this, { _, y, m, d ->
                val c = Calendar.getInstance(); c.set(y, m, d)
                periodWeekStartText.text = dateFmt.format(c.time)
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        periodMonthText.setOnClickListener {
            DatePickerDialog(this, { _, y, m, _ ->
                val month = String.format("%04d-%02d", y, m + 1)
                periodMonthText.text = month
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        soldYes.setOnCheckedChangeListener { _, checked -> incomeRow.visibility = if (checked) View.VISIBLE else View.GONE }
        soldNo.setOnCheckedChangeListener { _, checked -> if (checked) incomeRow.visibility = View.GONE }

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

        fun validateRequired(): Boolean {
            if (schoolNameEdit.text?.toString()?.trim().isNullOrEmpty()) {
                Toast.makeText(this, "School Name is required", Toast.LENGTH_SHORT).show(); return false
            }
            if (lgaSpinner.selectedItemPosition == 0) {
                Toast.makeText(this, "Please select LGA", Toast.LENGTH_SHORT).show(); return false
            }
            if (periodTypeSpinner.selectedItemPosition == 0) {
                Toast.makeText(this, "Select reporting period type", Toast.LENGTH_SHORT).show(); return false
            }
            when (periodTypeSpinner.selectedItemPosition) {
                1 -> if (periodDailyDateText.text.isNullOrEmpty()) { Toast.makeText(this, "Select date", Toast.LENGTH_SHORT).show(); return false }
                2 -> if (periodWeekStartText.text.isNullOrEmpty()) { Toast.makeText(this, "Select week start", Toast.LENGTH_SHORT).show(); return false }
                3 -> if (periodMonthText.text.isNullOrEmpty()) { Toast.makeText(this, "Select month", Toast.LENGTH_SHORT).show(); return false }
            }
            if (statusSpinner.selectedItemPosition == 0) {
                Toast.makeText(this, "Select waste bank status", Toast.LENGTH_SHORT).show(); return false
            }
            if (!(soldYes.isChecked || soldNo.isChecked)) {
                Toast.makeText(this, "Select sale to recycler", Toast.LENGTH_SHORT).show(); return false
            }
            return true
        }

        fun clearForm() {
            schoolNameEdit.setText("")
            lgaSpinner.setSelection(0)
            periodTypeSpinner.setSelection(0)
            periodDailyDateText.text = ""
            periodWeekStartText.text = ""
            periodMonthText.text = ""
            statusSpinner.setSelection(0)

            plasticCollectedEdit.setText("")
            plasticRecycledEdit.setText("")
            paperCollectedEdit.setText("")
            paperRecycledEdit.setText("")
            metalCollectedEdit.setText("")
            metalRecycledEdit.setText("")
            glassCollectedEdit.setText("")
            glassRecycledEdit.setText("")
            organicCollectedEdit.setText("")
            organicRecycledEdit.setText("")
            otherTypeEdit.setText("")
            otherCollectedEdit.setText("")
            otherRecycledEdit.setText("")

            soldYes.isChecked = false
            soldNo.isChecked = false
            incomeEdit.setText("")
            buyerNameEdit.setText("")

            chNoBuyers.isChecked = false
            chStorage.isChecked = false
            chTransport.isChecked = false
            chSorting.isChecked = false
            chOther.isChecked = false

            participationSpinner.setSelection(0)
            remarksEdit.setText("")

            photo1 = null; photo2 = null; photo3 = null
            photoPreview1.setImageResource(R.drawable.ic_person)
            photoPreview2.setImageResource(R.drawable.ic_person)
            photoPreview3.setImageResource(R.drawable.ic_person)
        }

        fun buildRecord(): SchoolWasteBankRecord {
            val now = System.currentTimeMillis()
            val periodType = periodTypeSpinner.selectedItem?.toString().orEmpty()
            val sold = soldYes.isChecked
            val challenges = mutableListOf<String>().apply {
                if (chNoBuyers.isChecked) add("No buyers")
                if (chStorage.isChecked) add("Storage issues")
                if (chTransport.isChecked) add("Transport")
                if (chSorting.isChecked) add("Sorting challenges")
                if (chOther.isChecked) add("Other")
            }
            val photos = listOfNotNull(photo1, photo2, photo3)
            return SchoolWasteBankRecord(
                id = UUID.randomUUID().toString(),
                schoolId = null,
                schoolName = schoolNameEdit.text?.toString()?.trim().orEmpty(),
                lga = lgaSpinner.selectedItem?.toString().orEmpty(),
                reportingPeriodType = periodType,
                reportingDate = periodDailyDateText.text?.toString()?.trim().orEmpty().ifEmpty { null },
                reportingWeekStart = periodWeekStartText.text?.toString()?.trim().orEmpty().ifEmpty { null },
                reportingMonth = periodMonthText.text?.toString()?.trim().orEmpty().ifEmpty { null },
                status = statusSpinner.selectedItem?.toString().orEmpty(),
                plasticCollectedKg = toD(plasticCollectedEdit.text?.toString()),
                plasticRecycledKg = toD(plasticRecycledEdit.text?.toString()),
                paperCollectedKg = toD(paperCollectedEdit.text?.toString()),
                paperRecycledKg = toD(paperRecycledEdit.text?.toString()),
                metalCollectedKg = toD(metalCollectedEdit.text?.toString()),
                metalRecycledKg = toD(metalRecycledEdit.text?.toString()),
                glassCollectedKg = toD(glassCollectedEdit.text?.toString()),
                glassRecycledKg = toD(glassRecycledEdit.text?.toString()),
                organicCollectedKg = toD(organicCollectedEdit.text?.toString()),
                organicRecycledKg = toD(organicRecycledEdit.text?.toString()),
                otherType = otherTypeEdit.text?.toString()?.trim()?.ifEmpty { null },
                otherCollectedKg = toD(otherCollectedEdit.text?.toString()),
                otherRecycledKg = toD(otherRecycledEdit.text?.toString()),
                soldToRecycler = sold,
                incomeFromSale = if (sold) toD(incomeEdit.text?.toString()) else null,
                buyerName = buyerNameEdit.text?.toString()?.trim()?.ifEmpty { null },
                challenges = challenges,
                studentParticipationLevel = participationSpinner.selectedItemPosition.takeIf { it > 0 }?.let { participationSpinner.selectedItem.toString().toIntOrNull() },
                remarks = remarksEdit.text?.toString()?.trim()?.ifEmpty { null },
                photoBase64s = photos,
                recordedByUserId = recordedBy,
                createdAt = now,
                updatedAt = now
            )
        }

        saveBtn.setOnClickListener {
            if (!validateRequired()) return@setOnClickListener
            val record = buildRecord()
            saveBtn.isEnabled = false
            saveAnotherBtn.isEnabled = false
            Thread {
                val ok = try { ApiClient.postSchoolWasteBankRecord(this, record) } catch (_: Exception) { false }
                runOnUiThread {
                    saveBtn.isEnabled = true
                    saveAnotherBtn.isEnabled = true
                    if (ok) {
                        MaterialAlertDialogBuilder(this)
                            .setTitle("Entry saved successfully.")
                            .setMessage("Choose next action")
                            .setPositiveButton("Close") { dlg, _ -> dlg.dismiss(); finish() }
                            .setNegativeButton("Add Another") { dlg, _ -> dlg.dismiss(); clearForm() }
                            .show()
                    } else {
                        SchoolWasteBankStore.savePending(this, record)
                        MaterialAlertDialogBuilder(this)
                            .setTitle("Saved locally due to network/auth issue.")
                            .setMessage("Choose next action")
                            .setPositiveButton("Close") { dlg, _ -> dlg.dismiss(); finish() }
                            .setNegativeButton("Add Another") { dlg, _ -> dlg.dismiss(); clearForm() }
                            .show()
                    }
                }
            }.start()
        }

        saveAnotherBtn.setOnClickListener {
            if (!validateRequired()) return@setOnClickListener
            val record = buildRecord()
            saveBtn.isEnabled = false
            saveAnotherBtn.isEnabled = false
            Thread {
                val ok = try { ApiClient.postSchoolWasteBankRecord(this, record) } catch (_: Exception) { false }
                runOnUiThread {
                    saveBtn.isEnabled = true
                    saveAnotherBtn.isEnabled = true
                    if (ok) {
                        Toast.makeText(this, "Entry saved successfully.", Toast.LENGTH_SHORT).show()
                        clearForm()
                    } else {
                        SchoolWasteBankStore.savePending(this, record)
                        Toast.makeText(this, "Saved locally due to network/auth issue.", Toast.LENGTH_SHORT).show()
                        clearForm()
                    }
                }
            }.start()
        }

        cancelBtn.setOnClickListener { finish() }
    }
}
