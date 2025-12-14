package com.cleancall.mz

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class RegisterWomenTrainingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_women_training)

        val prefs = getSharedPreferences("clean_call", MODE_PRIVATE)
        val facilitatorName = prefs.getString("signup_name", "User") ?: "User"

        val titleEdit: TextInputEditText = findViewById(R.id.titleEdit)
        val dateText: TextView = findViewById(R.id.dateText)
        val startTimeText: TextView = findViewById(R.id.startTimeText)
        val endTimeText: TextView = findViewById(R.id.endTimeText)
        val lgaSpinner: Spinner = findViewById(R.id.lgaSpinner)
        val communityEdit: TextInputEditText = findViewById(R.id.communityEdit)
        val venueSpinner: Spinner = findViewById(R.id.venueSpinner)
        val facilitatorText: TextView = findViewById(R.id.facilitatorText)
        val organisationEdit: TextInputEditText = findViewById(R.id.organisationEdit)

        val totalWomenEdit: TextInputEditText = findViewById(R.id.totalWomenEdit)
        val age18Edit: TextInputEditText = findViewById(R.id.age18Edit)
        val age26Edit: TextInputEditText = findViewById(R.id.age26Edit)
        val age36Edit: TextInputEditText = findViewById(R.id.age36Edit)
        val age46Edit: TextInputEditText = findViewById(R.id.age46Edit)
        val householdsEdit: TextInputEditText = findViewById(R.id.householdsEdit)
        val attendanceNew: RadioButton = findViewById(R.id.attendanceNew)
        val attendanceMixed: RadioButton = findViewById(R.id.attendanceMixed)

        val cbTopicSorting: CheckBox = findViewById(R.id.cbTopicSorting)
        val cbTopicCategories: CheckBox = findViewById(R.id.cbTopicCategories)
        val cbTopicPlastics: CheckBox = findViewById(R.id.cbTopicPlastics)
        val cbTopicImpacts: CheckBox = findViewById(R.id.cbTopicImpacts)
        val cbTopicSeparate: CheckBox = findViewById(R.id.cbTopicSeparate)
        val cbTopicIncome: CheckBox = findViewById(R.id.cbTopicIncome)

        val cbMethodDiscussion: CheckBox = findViewById(R.id.cbMethodDiscussion)
        val cbMethodDemo: CheckBox = findViewById(R.id.cbMethodDemo)
        val cbMethodPractical: CheckBox = findViewById(R.id.cbMethodPractical)
        val cbMethodVisual: CheckBox = findViewById(R.id.cbMethodVisual)

        val durationSpinner: Spinner = findViewById(R.id.durationSpinner)

        val pretestYes: RadioButton = findViewById(R.id.pretestYes)
        val pretestNo: RadioButton = findViewById(R.id.pretestNo)
        val pretestSection: View = findViewById(R.id.pretestSection)

        val knowBeforeEdit: TextInputEditText = findViewById(R.id.knowBeforeEdit)
        val knowAfterEdit: TextInputEditText = findViewById(R.id.knowAfterEdit)
        val catsBeforeEdit: TextInputEditText = findViewById(R.id.catsBeforeEdit)
        val catsAfterEdit: TextInputEditText = findViewById(R.id.catsAfterEdit)
        val burnBeforeEdit: TextInputEditText = findViewById(R.id.burnBeforeEdit)
        val burnAfterEdit: TextInputEditText = findViewById(R.id.burnAfterEdit)

        val confidenceBeforeSpinner: Spinner = findViewById(R.id.confidenceBeforeSpinner)
        val confidenceAfterSpinner: Spinner = findViewById(R.id.confidenceAfterSpinner)
        val importanceBeforeSpinner: Spinner = findViewById(R.id.importanceBeforeSpinner)
        val importanceAfterSpinner: Spinner = findViewById(R.id.importanceAfterSpinner)

        val committedEdit: TextInputEditText = findViewById(R.id.committedEdit)
        val followupSpinner: Spinner = findViewById(R.id.followupSpinner)
        val followupDateText: TextView = findViewById(R.id.followupDateText)
        val notesEdit: TextInputEditText = findViewById(R.id.notesEdit)

        val saveBtn: MaterialButton = findViewById(R.id.saveBtn)
        val saveAnotherBtn: MaterialButton = findViewById(R.id.saveAnotherBtn)
        val cancelBtn: TextView = findViewById(R.id.cancelBtn)

        facilitatorText.text = facilitatorName

        val lgas = listOf("Select", "Dala", "Fagge", "Gwale", "Kano Municipal", "Nassarawa", "Tarauni", "Kumbotso", "Ungogo", "Other")
        lgaSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, lgas).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }
        val venues = listOf("Select", "Household cluster", "Community hall", "Religious centre", "School", "Other")
        venueSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, venues).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }
        val durations = listOf("Select", "< 1 hr", "1–2 hrs", "> 2 hrs")
        durationSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, durations).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }
        val scale = listOf("Select", "1", "2", "3", "4", "5")
        confidenceBeforeSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, scale).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }
        confidenceAfterSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, scale).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }
        importanceBeforeSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, scale).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }
        importanceAfterSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, scale).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }
        val followups = listOf("Select", "Household revisit", "Community group follow-up", "Phone call", "No follow-up")
        followupSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, followups).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }

        pretestYes.setOnCheckedChangeListener { _, checked -> pretestSection.visibility = if (checked) View.VISIBLE else View.GONE }
        pretestNo.setOnCheckedChangeListener { _, checked -> if (checked) pretestSection.visibility = View.GONE }

        fun pickDate(target: TextView) {
            val cal = Calendar.getInstance()
            DatePickerDialog(this, { _, y, m, d ->
                val s = String.format(Locale.US, "%04d-%02d-%02d", y, m + 1, d)
                target.text = s
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        fun pickTime(target: TextView) {
            val cal = Calendar.getInstance()
            TimePickerDialog(this, { _, h, min ->
                val s = String.format(Locale.US, "%02d:%02d", h, min)
                target.text = s
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        dateText.setOnClickListener { pickDate(dateText) }
        startTimeText.setOnClickListener { pickTime(startTimeText) }
        endTimeText.setOnClickListener { pickTime(endTimeText) }
        followupDateText.setOnClickListener { pickDate(followupDateText) }

        fun validateRequired(): Boolean {
            if (titleEdit.text?.toString()?.trim().isNullOrEmpty()) { Toast.makeText(this, "Session Title is required", Toast.LENGTH_SHORT).show(); return false }
            if (dateText.text?.toString()?.trim().isNullOrEmpty()) { Toast.makeText(this, "Date is required", Toast.LENGTH_SHORT).show(); return false }
            if (lgaSpinner.selectedItemPosition == 0) { Toast.makeText(this, "Select LGA", Toast.LENGTH_SHORT).show(); return false }
            if (communityEdit.text?.toString()?.trim().isNullOrEmpty()) { Toast.makeText(this, "Community/Ward is required", Toast.LENGTH_SHORT).show(); return false }
            if (venueSpinner.selectedItemPosition == 0) { Toast.makeText(this, "Select venue type", Toast.LENGTH_SHORT).show(); return false }
            if (totalWomenEdit.text?.toString()?.trim().isNullOrEmpty()) { Toast.makeText(this, "Total women attending is required", Toast.LENGTH_SHORT).show(); return false }
            val topicsChecked = listOf(cbTopicSorting, cbTopicCategories, cbTopicPlastics, cbTopicImpacts, cbTopicSeparate, cbTopicIncome).any { it.isChecked }
            if (!topicsChecked) { Toast.makeText(this, "Select at least one topic", Toast.LENGTH_SHORT).show(); return false }
            val methodsChecked = listOf(cbMethodDiscussion, cbMethodDemo, cbMethodPractical, cbMethodVisual).any { it.isChecked }
            if (!methodsChecked) { Toast.makeText(this, "Select at least one method", Toast.LENGTH_SHORT).show(); return false }
            if (durationSpinner.selectedItemPosition == 0) { Toast.makeText(this, "Select duration", Toast.LENGTH_SHORT).show(); return false }
            if (!(pretestYes.isChecked || pretestNo.isChecked)) { Toast.makeText(this, "Indicate pre/post test usage", Toast.LENGTH_SHORT).show(); return false }
            if (committedEdit.text?.toString()?.trim().isNullOrEmpty()) { Toast.makeText(this, "Committed to sorting is required", Toast.LENGTH_SHORT).show(); return false }
            if (followupSpinner.selectedItemPosition == 0) { Toast.makeText(this, "Select follow-up plan", Toast.LENGTH_SHORT).show(); return false }
            return true
        }

        fun toIntOrNull(s: String?): Int? = s?.trim()?.takeIf { it.isNotEmpty() }?.toIntOrNull()

        fun buildSession(): WomenTrainingSession {
            val topics = mutableListOf<String>().apply {
                if (cbTopicSorting.isChecked) add("What is waste sorting")
                if (cbTopicCategories.isChecked) add("Categories of household waste")
                if (cbTopicPlastics.isChecked) add("Proper disposal of plastics")
                if (cbTopicImpacts.isChecked) add("Health & environmental impacts")
                if (cbTopicSeparate.isChecked) add("How to separate at home")
                if (cbTopicIncome.isChecked) add("Income opportunities from sorted waste")
            }
            val methods = mutableListOf<String>().apply {
                if (cbMethodDiscussion.isChecked) add("Group discussion")
                if (cbMethodDemo.isChecked) add("Demonstration")
                if (cbMethodPractical.isChecked) add("Practical sorting exercise")
                if (cbMethodVisual.isChecked) add("Use of visual aids")
            }
            val attendanceType = when {
                attendanceNew.isChecked -> "New participants"
                attendanceMixed.isChecked -> "Mixed"
                else -> null
            }
            val preUsed = pretestYes.isChecked
            val now = System.currentTimeMillis()
            val code = "WT-" + SimpleDateFormat("yyyyMMdd", Locale.US).format(Calendar.getInstance().time) + "-" + UUID.randomUUID().toString().take(6)
            return WomenTrainingSession(
                id = UUID.randomUUID().toString(),
                title = titleEdit.text?.toString()?.trim().orEmpty(),
                date = dateText.text?.toString()?.trim().orEmpty(),
                startTime = startTimeText.text?.toString()?.trim().orEmpty().ifEmpty { null },
                endTime = endTimeText.text?.toString()?.trim().orEmpty().ifEmpty { null },
                lga = lgaSpinner.selectedItem?.toString().orEmpty(),
                community = communityEdit.text?.toString()?.trim().orEmpty(),
                venueType = venueSpinner.selectedItem?.toString().orEmpty(),
                facilitatorName = facilitatorName,
                organisation = organisationEdit.text?.toString()?.trim().orEmpty().ifEmpty { null },
                totalWomen = totalWomenEdit.text?.toString()?.trim()?.toIntOrNull() ?: 0,
                age18_25 = toIntOrNull(age18Edit.text?.toString()),
                age26_35 = toIntOrNull(age26Edit.text?.toString()),
                age36_45 = toIntOrNull(age36Edit.text?.toString()),
                age46Plus = toIntOrNull(age46Edit.text?.toString()),
                householdsRepresented = toIntOrNull(householdsEdit.text?.toString()),
                attendanceType = attendanceType,
                topics = topics,
                methods = methods,
                duration = durationSpinner.selectedItem?.toString().orEmpty(),
                pretestUsed = preUsed,
                knowledgeSortingBefore = toIntOrNull(knowBeforeEdit.text?.toString()),
                knowledgeSortingAfter = toIntOrNull(knowAfterEdit.text?.toString()),
                categoriesCorrectBefore = toIntOrNull(catsBeforeEdit.text?.toString()),
                categoriesCorrectAfter = toIntOrNull(catsAfterEdit.text?.toString()),
                burnFalseBefore = toIntOrNull(burnBeforeEdit.text?.toString()),
                burnFalseAfter = toIntOrNull(burnAfterEdit.text?.toString()),
                confidenceAvgBefore = confidenceBeforeSpinner.selectedItemPosition.takeIf { it > 0 }?.let { confidenceBeforeSpinner.selectedItem.toString().toIntOrNull() },
                confidenceAvgAfter = confidenceAfterSpinner.selectedItemPosition.takeIf { it > 0 }?.let { confidenceAfterSpinner.selectedItem.toString().toIntOrNull() },
                importanceAvgBefore = importanceBeforeSpinner.selectedItemPosition.takeIf { it > 0 }?.let { importanceBeforeSpinner.selectedItem.toString().toIntOrNull() },
                importanceAvgAfter = importanceAfterSpinner.selectedItemPosition.takeIf { it > 0 }?.let { importanceAfterSpinner.selectedItem.toString().toIntOrNull() },
                committedToSorting = committedEdit.text?.toString()?.trim()?.toIntOrNull() ?: 0,
                followupType = followupSpinner.selectedItem?.toString().orEmpty(),
                followupDate = followupDateText.text?.toString()?.trim().orEmpty().ifEmpty { null },
                notes = notesEdit.text?.toString()?.trim().orEmpty().ifEmpty { null },
                createdByUserId = facilitatorName,
                sessionCode = code,
                createdAt = now,
                updatedAt = now
            )
        }

        fun clearForm() {
            titleEdit.setText("")
            dateText.text = ""
            startTimeText.text = ""
            endTimeText.text = ""
            lgaSpinner.setSelection(0)
            communityEdit.setText("")
            venueSpinner.setSelection(0)
            organisationEdit.setText("")

            totalWomenEdit.setText("")
            age18Edit.setText("")
            age26Edit.setText("")
            age36Edit.setText("")
            age46Edit.setText("")
            householdsEdit.setText("")
            attendanceNew.isChecked = false
            attendanceMixed.isChecked = false

            cbTopicSorting.isChecked = false
            cbTopicCategories.isChecked = false
            cbTopicPlastics.isChecked = false
            cbTopicImpacts.isChecked = false
            cbTopicSeparate.isChecked = false
            cbTopicIncome.isChecked = false

            cbMethodDiscussion.isChecked = false
            cbMethodDemo.isChecked = false
            cbMethodPractical.isChecked = false
            cbMethodVisual.isChecked = false

            durationSpinner.setSelection(0)

            pretestYes.isChecked = false
            pretestNo.isChecked = false
            pretestSection.visibility = View.GONE
            knowBeforeEdit.setText("")
            knowAfterEdit.setText("")
            catsBeforeEdit.setText("")
            catsAfterEdit.setText("")
            burnBeforeEdit.setText("")
            burnAfterEdit.setText("")
            confidenceBeforeSpinner.setSelection(0)
            confidenceAfterSpinner.setSelection(0)
            importanceBeforeSpinner.setSelection(0)
            importanceAfterSpinner.setSelection(0)

            committedEdit.setText("")
            followupSpinner.setSelection(0)
            followupDateText.text = ""
            notesEdit.setText("")
        }

        saveBtn.setOnClickListener {
            if (!validateRequired()) return@setOnClickListener
            val session = buildSession()
            saveBtn.isEnabled = false
            saveAnotherBtn.isEnabled = false
            Thread {
                val ok = try { ApiClient.postWomenTrainingSession(this, session) } catch (_: Exception) { false }
                runOnUiThread {
                    saveBtn.isEnabled = true
                    saveAnotherBtn.isEnabled = true
                    if (ok) {
                        MaterialAlertDialogBuilder(this)
                            .setTitle("Training session saved.")
                            .setMessage("Choose next action")
                            .setPositiveButton("View Session Details") { dlg, _ ->
                                dlg.dismiss()
                                val intent = android.content.Intent(this, WomenTrainingProfileActivity::class.java)
                                intent.putExtra("session_id", session.id)
                                startActivity(intent)
                            }
                            .setNegativeButton("Add Another") { dlg, _ -> dlg.dismiss(); clearForm() }
                            .show()
                    } else {
                        WomenTrainingStore.save(this, session)
                        MaterialAlertDialogBuilder(this)
                            .setTitle("Saved locally due to network/auth issue.")
                            .setMessage("Choose next action")
                            .setPositiveButton("View Session Details") { dlg, _ ->
                                dlg.dismiss()
                                val intent = android.content.Intent(this, WomenTrainingProfileActivity::class.java)
                                intent.putExtra("session_id", session.id)
                                startActivity(intent)
                            }
                            .setNegativeButton("Add Another") { dlg, _ -> dlg.dismiss(); clearForm() }
                            .show()
                    }
                }
            }.start()
        }

        saveAnotherBtn.setOnClickListener {
            if (!validateRequired()) return@setOnClickListener
            val session = buildSession()
            saveBtn.isEnabled = false
            saveAnotherBtn.isEnabled = false
            Thread {
                val ok = try { ApiClient.postWomenTrainingSession(this, session) } catch (_: Exception) { false }
                runOnUiThread {
                    saveBtn.isEnabled = true
                    saveAnotherBtn.isEnabled = true
                    if (ok) {
                        Toast.makeText(this, "Training session saved.", Toast.LENGTH_SHORT).show()
                        clearForm()
                    } else {
                        WomenTrainingStore.save(this, session)
                        Toast.makeText(this, "Saved locally due to network/auth issue.", Toast.LENGTH_SHORT).show()
                        clearForm()
                    }
                }
            }.start()
        }

        cancelBtn.setOnClickListener { finish() }
    }
}
