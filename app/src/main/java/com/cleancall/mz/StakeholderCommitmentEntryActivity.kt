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

class StakeholderCommitmentEntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stakeholder_commitment_entry)

        findViewById<View>(R.id.backButton).setOnClickListener { finish() }

        val prefs = getSharedPreferences("clean_call", MODE_PRIVATE)
        val recordedBy = prefs.getString("signup_name", "User") ?: "User"

        val stakeholderNameEdit: TextInputEditText = findViewById(R.id.stakeholderNameEdit)
        val stakeholderTypeSpinner: Spinner = findViewById(R.id.stakeholderTypeSpinner)
        val lgaSpinner: Spinner = findViewById<Spinner>(com.cleancall.mz.R.id.lgaSpinner)
        val contactPersonEdit: TextInputEditText = findViewById(R.id.contactPersonEdit)
        val phoneEdit: TextInputEditText = findViewById(R.id.phoneEdit)

        val engagementDateText: TextView = findViewById(R.id.engagementDateText)
        val engagementTypeSpinner: Spinner = findViewById(R.id.engagementTypeSpinner)
        val engagedByText: TextView = findViewById(R.id.engagedByText)
        val engagementDescEdit: TextInputEditText = findViewById(R.id.engagementDescEdit)

        val commitmentTextEdit: TextInputEditText = findViewById(R.id.commitmentTextEdit)
        val commitmentCategorySpinner: Spinner = findViewById(R.id.commitmentCategorySpinner)
        val dueDateText: TextView = findViewById(R.id.dueDateText)
        val prioritySpinner: Spinner = findViewById(R.id.prioritySpinner)

        val actionTakenEdit: TextInputEditText = findViewById(R.id.actionTakenEdit)
        val statusSpinner: Spinner = findViewById(R.id.statusSpinner)
        val followYes: RadioButton = findViewById(R.id.followYes)
        val followNo: RadioButton = findViewById(R.id.followNo)
        val followFields: View = findViewById(R.id.followFields)
        val followTypeSpinner: Spinner = findViewById(R.id.followTypeSpinner)
        val followDateText: TextView = findViewById(R.id.followDateText)
        val assignedSpinner: Spinner = findViewById(R.id.assignedSpinner)
        val assignedOtherEdit: TextInputEditText = findViewById(R.id.assignedOtherEdit)

        val remarksEdit: TextInputEditText = findViewById(R.id.remarksEdit)

        val evidencePreview1: ImageView = findViewById(R.id.evidencePreview1)
        val evidencePreview2: ImageView = findViewById(R.id.evidencePreview2)
        val evidencePreview3: ImageView = findViewById(R.id.evidencePreview3)
        val chooseFileBtn1: MaterialButton = findViewById(R.id.chooseFileBtn1)
        val chooseFileBtn2: MaterialButton = findViewById(R.id.chooseFileBtn2)
        val chooseFileBtn3: MaterialButton = findViewById(R.id.chooseFileBtn3)

        val saveBtn: MaterialButton = findViewById(R.id.saveBtn)
        val saveAnotherBtn: MaterialButton = findViewById(R.id.saveAnotherBtn)
        val cancelBtn: TextView = findViewById(R.id.cancelBtn)

        engagedByText.text = recordedBy

        val lgas = listOf("Select", "Dala", "Fagge", "Gwale", "Kano Municipal", "Nassarawa", "Tarauni", "Kumbotso", "Ungogo", "Other")
        lgaSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, lgas).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }

        val types = listOf("Select", "Government", "School", "NGO/CSO", "Informal", "Private Sector", "Community group", "Other")
        stakeholderTypeSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, types).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }

        val engagements = listOf("Select", "Meeting", "Advocacy visit", "Training/Briefing", "Community dialogue", "Site inspection", "Phone call follow-up", "Other")
        engagementTypeSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, engagements).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }

        val categories = listOf("Select", "Policy/Institutional", "Operational", "Infrastructure support", "Training / Capacity building", "Provision of equipment", "Mobilisation", "Other")
        commitmentCategorySpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, categories).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }

        val priorities = listOf("Select", "Low", "Medium", "High")
        prioritySpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, priorities).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }

        val statuses = listOf("Select", "Pending", "Ongoing", "Completed", "Cancelled / Changed")
        statusSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, statuses).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }

        val followTypes = listOf("Select", "Phone call", "Visit", "Reminder message")
        followTypeSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, followTypes).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }

        val assignedOptions = listOf("Me (logged user)", "Other")
        assignedSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, assignedOptions).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }

        val cal = Calendar.getInstance()
        val dateFmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        engagementDateText.setOnClickListener {
            DatePickerDialog(this, { _, y, m, d -> val c = Calendar.getInstance(); c.set(y, m, d); engagementDateText.text = dateFmt.format(c.time) }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        dueDateText.setOnClickListener {
            DatePickerDialog(this, { _, y, m, d -> val c = Calendar.getInstance(); c.set(y, m, d); dueDateText.text = dateFmt.format(c.time) }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        followDateText.setOnClickListener {
            DatePickerDialog(this, { _, y, m, d -> val c = Calendar.getInstance(); c.set(y, m, d); followDateText.text = dateFmt.format(c.time) }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        followYes.setOnCheckedChangeListener { _, checked -> if (checked) followFields.visibility = View.VISIBLE }
        followNo.setOnCheckedChangeListener { _, checked -> if (checked) followFields.visibility = View.GONE }
        assignedSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                assignedOtherEdit.visibility = if (position == 1) View.VISIBLE else View.GONE
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        var ev1: String? = null
        var ev2: String? = null
        var ev3: String? = null
        var t1: String? = null
        var t2: String? = null
        var t3: String? = null

        fun bytesToBase64(b: ByteArray): String = Base64.encodeToString(b, Base64.NO_WRAP)

        val pick1 = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val type = contentResolver.getType(uri)
                val input = contentResolver.openInputStream(uri)
                val bytes = input?.readBytes(); input?.close()
                if (bytes != null) {
                    ev1 = bytesToBase64(bytes); t1 = type
                    try {
                        val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        evidencePreview1.setImageBitmap(bmp)
                    } catch (_: Exception) {
                        evidencePreview1.setImageResource(R.drawable.ic_person)
                    }
                }
            }
        }
        val pick2 = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val type = contentResolver.getType(uri)
                val input = contentResolver.openInputStream(uri)
                val bytes = input?.readBytes(); input?.close()
                if (bytes != null) {
                    ev2 = bytesToBase64(bytes); t2 = type
                    try {
                        val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        evidencePreview2.setImageBitmap(bmp)
                    } catch (_: Exception) {
                        evidencePreview2.setImageResource(R.drawable.ic_person)
                    }
                }
            }
        }
        val pick3 = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val type = contentResolver.getType(uri)
                val input = contentResolver.openInputStream(uri)
                val bytes = input?.readBytes(); input?.close()
                if (bytes != null) {
                    ev3 = bytesToBase64(bytes); t3 = type
                    try {
                        val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        evidencePreview3.setImageBitmap(bmp)
                    } catch (_: Exception) {
                        evidencePreview3.setImageResource(R.drawable.ic_person)
                    }
                }
            }
        }

        chooseFileBtn1.setOnClickListener { pick1.launch("*/*") }
        chooseFileBtn2.setOnClickListener { pick2.launch("*/*") }
        chooseFileBtn3.setOnClickListener { pick3.launch("*/*") }

        fun validateRequired(): Boolean {
            if (stakeholderNameEdit.text?.toString()?.trim().isNullOrEmpty()) { Toast.makeText(this, "Stakeholder Name is required", Toast.LENGTH_SHORT).show(); return false }
            if (stakeholderTypeSpinner.selectedItemPosition == 0) { Toast.makeText(this, "Select stakeholder type", Toast.LENGTH_SHORT).show(); return false }
            if (lgaSpinner.selectedItemPosition == 0) { Toast.makeText(this, "Select LGA", Toast.LENGTH_SHORT).show(); return false }
            if (engagementDateText.text.isNullOrEmpty()) { Toast.makeText(this, "Select engagement date", Toast.LENGTH_SHORT).show(); return false }
            if (engagementTypeSpinner.selectedItemPosition == 0) { Toast.makeText(this, "Select engagement type", Toast.LENGTH_SHORT).show(); return false }
            if (commitmentTextEdit.text?.toString()?.trim().isNullOrEmpty()) { Toast.makeText(this, "Commitment text is required", Toast.LENGTH_SHORT).show(); return false }
            if (statusSpinner.selectedItemPosition == 0) { Toast.makeText(this, "Select status", Toast.LENGTH_SHORT).show(); return false }
            return true
        }

        fun clearForm() {
            stakeholderNameEdit.setText("")
            stakeholderTypeSpinner.setSelection(0)
            lgaSpinner.setSelection(0)
            contactPersonEdit.setText("")
            phoneEdit.setText("")
            engagementDateText.text = ""
            engagementTypeSpinner.setSelection(0)
            engagementDescEdit.setText("")
            commitmentTextEdit.setText("")
            commitmentCategorySpinner.setSelection(0)
            dueDateText.text = ""
            prioritySpinner.setSelection(0)
            actionTakenEdit.setText("")
            statusSpinner.setSelection(0)
            followNo.isChecked = true
            followTypeSpinner.setSelection(0)
            followDateText.text = ""
            assignedSpinner.setSelection(0)
            assignedOtherEdit.setText("")
            remarksEdit.setText("")
            ev1 = null; ev2 = null; ev3 = null; t1 = null; t2 = null; t3 = null
            evidencePreview1.setImageResource(R.drawable.ic_person)
            evidencePreview2.setImageResource(R.drawable.ic_person)
            evidencePreview3.setImageResource(R.drawable.ic_person)
        }

        fun buildItem(): StakeholderCommitment {
            val now = System.currentTimeMillis()
            val evidence = listOfNotNull(ev1, ev2, ev3)
            val types = listOfNotNull(t1, t2, t3)
            val due = dueDateText.text?.toString()?.trim()?.ifEmpty { null }
            val st = statusSpinner.selectedItem?.toString()?.trim().orEmpty()
            val flagged = if (!due.isNullOrEmpty() && st != "Completed") {
                try {
                    val c = Calendar.getInstance()
                    val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                    val dueMs = fmt.parse(due)?.time ?: now
                    dueMs < now
                } catch (_: Exception) { null }
            } else null

            val assignedSelection = assignedSpinner.selectedItemPosition
            val assignedVal = if (assignedSelection == 1) assignedOtherEdit.text?.toString()?.trim()?.ifEmpty { null } else recordedBy

            return StakeholderCommitment(
                id = UUID.randomUUID().toString(),
                stakeholderName = stakeholderNameEdit.text?.toString()?.trim().orEmpty(),
                stakeholderType = stakeholderTypeSpinner.selectedItem?.toString()?.trim().orEmpty(),
                lga = lgaSpinner.selectedItem?.toString()?.trim().orEmpty(),
                contactPerson = contactPersonEdit.text?.toString()?.trim()?.ifEmpty { null },
                phone = phoneEdit.text?.toString()?.trim()?.ifEmpty { null },
                engagementDate = engagementDateText.text?.toString()?.trim().orEmpty(),
                engagementType = engagementTypeSpinner.selectedItem?.toString()?.trim().orEmpty(),
                engagedByUserId = recordedBy,
                engagementDescription = engagementDescEdit.text?.toString()?.trim()?.ifEmpty { null },
                commitmentText = commitmentTextEdit.text?.toString()?.trim().orEmpty(),
                commitmentCategory = commitmentCategorySpinner.selectedItemPosition.takeIf { it > 0 }?.let { commitmentCategorySpinner.selectedItem?.toString() },
                dueDate = due,
                priorityLevel = prioritySpinner.selectedItemPosition.takeIf { it > 0 }?.let { prioritySpinner.selectedItem?.toString() },
                actionTaken = actionTakenEdit.text?.toString()?.trim()?.ifEmpty { null },
                status = st,
                followupRequired = followYes.isChecked,
                followupType = followTypeSpinner.selectedItemPosition.takeIf { it > 0 }?.let { followTypeSpinner.selectedItem?.toString() },
                followupDate = followDateText.text?.toString()?.trim()?.ifEmpty { null },
                followupAssignedTo = assignedVal,
                remarks = remarksEdit.text?.toString()?.trim()?.ifEmpty { null },
                evidence = evidence,
                evidenceTypes = types,
                recordedByUserId = recordedBy,
                isSystemFlagged = flagged,
                createdAt = now,
                updatedAt = now
            )
        }

        saveBtn.setOnClickListener {
            if (!validateRequired()) return@setOnClickListener
            val item = buildItem()
            saveBtn.isEnabled = false
            saveAnotherBtn.isEnabled = false
            Thread {
                val ok = try { ApiClient.postStakeholderCommitment(this, item) } catch (_: Exception) { false }
                runOnUiThread {
                    saveBtn.isEnabled = true
                    saveAnotherBtn.isEnabled = true
                    if (ok) {
                        MaterialAlertDialogBuilder(this)
                            .setTitle("Stakeholder commitment recorded successfully.")
                            .setMessage("Choose next action")
                            .setPositiveButton("View Commitment List") { dlg, _ -> dlg.dismiss(); startActivity(android.content.Intent(this, StakeholderCommitmentListActivity::class.java)); finish() }
                            .setNegativeButton("Add Another") { dlg, _ -> dlg.dismiss(); clearForm() }
                            .show()
                    } else {
                        StakeholderCommitmentStore.save(this, item)
                        MaterialAlertDialogBuilder(this)
                            .setTitle("Saved locally due to network/auth issue.")
                            .setMessage("Choose next action")
                            .setPositiveButton("View Commitment List") { dlg, _ -> dlg.dismiss(); startActivity(android.content.Intent(this, StakeholderCommitmentListActivity::class.java)); finish() }
                            .setNegativeButton("Add Another") { dlg, _ -> dlg.dismiss(); clearForm() }
                            .show()
                    }
                }
            }.start()
        }

        saveAnotherBtn.setOnClickListener {
            if (!validateRequired()) return@setOnClickListener
            val item = buildItem()
            saveBtn.isEnabled = false
            saveAnotherBtn.isEnabled = false
            Thread {
                val ok = try { ApiClient.postStakeholderCommitment(this, item) } catch (_: Exception) { false }
                runOnUiThread {
                    saveBtn.isEnabled = true
                    saveAnotherBtn.isEnabled = true
                    if (ok) {
                        Toast.makeText(this, "Commitment saved successfully.", Toast.LENGTH_SHORT).show()
                        clearForm()
                    } else {
                        StakeholderCommitmentStore.save(this, item)
                        Toast.makeText(this, "Saved locally due to network/auth issue.", Toast.LENGTH_SHORT).show()
                        clearForm()
                    }
                }
            }.start()
        }

        cancelBtn.setOnClickListener { finish() }
    }
}
