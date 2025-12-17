package com.cleancall.mz

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.UUID

class RegisterPickerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_picker)

        val prefs = getSharedPreferences("clean_call", MODE_PRIVATE)
        val role = prefs.getString("pending_role", "") ?: ""

        val fullName: TextInputEditText = findViewById(R.id.fullNameEdit)
        val nickname: TextInputEditText = findViewById(R.id.nicknameEdit)
        val genderMale: RadioButton = findViewById(R.id.genderMale)
        val genderFemale: RadioButton = findViewById(R.id.genderFemale)
        val genderOther: RadioButton = findViewById(R.id.genderOther)
        val ageSpinner: Spinner = findViewById(R.id.ageRangeSpinner)
        val phoneEdit: TextInputEditText = findViewById(R.id.phoneEdit)
        val idEdit: TextInputEditText = findViewById(R.id.idEdit)

        val lgaSpinner: Spinner = findViewById(R.id.lgaSpinner)
        val communityEdit: TextInputEditText = findViewById(R.id.communityEdit)
        val clusterEdit: TextInputEditText = findViewById(R.id.clusterEdit)
        val primaryLocationSpinner: Spinner = findViewById(R.id.primaryLocationSpinner)

        val cbPlastic: CheckBox = findViewById(R.id.cbPlastic)
        val cbMetal: CheckBox = findViewById(R.id.cbMetal)
        val cbPaper: CheckBox = findViewById(R.id.cbPaper)
        val cbGlass: CheckBox = findViewById(R.id.cbGlass)
        val cbOrganic: CheckBox = findViewById(R.id.cbOrganic)
        val cbElectronic: CheckBox = findViewById(R.id.cbElectronic)
        val cbOtherWaste: CheckBox = findViewById(R.id.cbOtherWaste)

        val yearsSpinner: Spinner = findViewById(R.id.yearsSpinner)
        val sellDirect: RadioButton = findViewById(R.id.sellDirect)
        val sellMiddlemen: RadioButton = findViewById(R.id.sellMiddlemen)
        val sellBoth: RadioButton = findViewById(R.id.sellBoth)
        val sellOther: RadioButton = findViewById(R.id.sellOther)
        val incomeSpinner: Spinner = findViewById(R.id.incomeSpinner)

        val ppeAlways: RadioButton = findViewById(R.id.ppeAlways)
        val ppeSometimes: RadioButton = findViewById(R.id.ppeSometimes)
        val ppeNever: RadioButton = findViewById(R.id.ppeNever)
        val trainingYes: RadioButton = findViewById(R.id.trainingYes)
        val trainingNo: RadioButton = findViewById(R.id.trainingNo)
        val trainingProviderSpinner: Spinner = findViewById(R.id.trainingProviderSpinner)
        val trainingProviderRow: View = findViewById(R.id.trainingProviderRow)
        val joinYes: RadioButton = findViewById(R.id.joinYes)
        val joinNo: RadioButton = findViewById(R.id.joinNo)
        val joinNotSure: RadioButton = findViewById(R.id.joinNotSure)
        val specialNeedsEdit: TextInputEditText = findViewById(R.id.specialNeedsEdit)

        val saveBtn: MaterialButton = findViewById(R.id.saveBtn)
        val saveAnotherBtn: MaterialButton = findViewById(R.id.saveAnotherBtn)
        val cancelBtn: TextView = findViewById(R.id.cancelBtn)
        val photoPreview: ImageView = findViewById(R.id.photoPreview)
        val choosePhotoBtn: MaterialButton = findViewById(R.id.choosePhotoBtn)
        val capturePhotoBtn: MaterialButton = findViewById(R.id.capturePhotoBtn)

        var photoBase64: String? = null

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val input = contentResolver.openInputStream(uri)
                val bytes = input?.readBytes()
                input?.close()
                if (bytes != null) {
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    val out = ByteArrayOutputStream()
                    bmp.compress(Bitmap.CompressFormat.JPEG, 80, out)
                    val b = out.toByteArray()
                    photoBase64 = Base64.encodeToString(b, Base64.NO_WRAP)
                    photoPreview.setImageBitmap(bmp)
                }
            }
        }

        val takePreview = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp ->
            if (bmp != null) {
                val out = ByteArrayOutputStream()
                bmp.compress(Bitmap.CompressFormat.JPEG, 80, out)
                val b = out.toByteArray()
                photoBase64 = Base64.encodeToString(b, Base64.NO_WRAP)
                photoPreview.setImageBitmap(bmp)
            }
        }

        choosePhotoBtn.setOnClickListener { pickImage.launch("image/*") }
        capturePhotoBtn.setOnClickListener { takePreview.launch(null) }

        trainingYes.setOnCheckedChangeListener { _, checked ->
            trainingProviderRow.visibility = if (checked) View.VISIBLE else View.GONE
        }
        trainingNo.setOnCheckedChangeListener { _, checked ->
            if (checked) trainingProviderRow.visibility = View.GONE
        }

        val ages = listOf("Select", "Under 18", "18–25", "26–35", "36–45", "46–60", "Above 60")
        ageSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, ages).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }

        val lgas = listOf("Select", "Dala", "Fagge", "Gwale", "Kano Municipal", "Nassarawa", "Tarauni", "Kumbotso", "Ungogo", "Other")
        lgaSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, lgas).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }

        val primaryLocations = listOf("Select", "Dumpsite", "Street", "Market", "Household", "Business premises", "Other")
        primaryLocationSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, primaryLocations).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }

        val years = listOf("Select", "Less than 1 year", "1–3 years", "4–6 years", "More than 6 years")
        yearsSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, years).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }

        val incomes = listOf("Select", "Below ₦5,000", "₦5,000–₦10,000", "₦10,001–₦20,000", "Above ₦20,000", "Prefer not to say")
        incomeSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, incomes).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }

        val providers = listOf("Select", "Government", "NGO", "Private", "Community", "Other")
        trainingProviderSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, providers).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }

        if (role == "BENEFICIARY_PICKER") {
            findViewById<View>(R.id.sectionB).visibility = View.VISIBLE
            findViewById<View>(R.id.sectionC).visibility = View.VISIBLE
            findViewById<View>(R.id.sectionD).visibility = View.VISIBLE
            nickname.visibility = View.GONE
            idEdit.visibility = View.GONE
            clusterEdit.visibility = View.GONE
            incomeSpinner.visibility = View.GONE
            sellDirect.visibility = View.GONE
            sellMiddlemen.visibility = View.GONE
            sellBoth.visibility = View.GONE
            sellOther.visibility = View.GONE
        }

        fun validateRequired(): Boolean {
            if (fullName.text?.toString()?.trim().isNullOrEmpty()) {
                Toast.makeText(this, "Full Name is required", Toast.LENGTH_SHORT).show()
                return false
            }
            if (!(genderMale.isChecked || genderFemale.isChecked || genderOther.isChecked)) {
                Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show()
                return false
            }
            if (ageSpinner.selectedItemPosition == 0) {
                Toast.makeText(this, "Please select age range", Toast.LENGTH_SHORT).show()
                return false
            }
            if (lgaSpinner.selectedItemPosition == 0) {
                Toast.makeText(this, "Please select LGA", Toast.LENGTH_SHORT).show()
                return false
            }
            if (communityEdit.text?.toString()?.trim().isNullOrEmpty()) {
                Toast.makeText(this, "Community/Area is required", Toast.LENGTH_SHORT).show()
                return false
            }
            if (primaryLocationSpinner.selectedItemPosition == 0) {
                Toast.makeText(this, "Select primary collection location", Toast.LENGTH_SHORT).show()
                return false
            }
            if (!(cbPlastic.isChecked || cbMetal.isChecked || cbPaper.isChecked || cbGlass.isChecked || cbOrganic.isChecked || cbElectronic.isChecked || cbOtherWaste.isChecked)) {
                Toast.makeText(this, "Select at least one waste type", Toast.LENGTH_SHORT).show()
                return false
            }
            if (yearsSpinner.selectedItemPosition == 0) {
                Toast.makeText(this, "Select years of experience", Toast.LENGTH_SHORT).show()
                return false
            }
            if (!(ppeAlways.isChecked || ppeSometimes.isChecked || ppeNever.isChecked)) {
                Toast.makeText(this, "Select PPE usage", Toast.LENGTH_SHORT).show()
                return false
            }
            if (!(joinYes.isChecked || joinNo.isChecked || joinNotSure.isChecked)) {
                Toast.makeText(this, "Select willingness to join formal system", Toast.LENGTH_SHORT).show()
                return false
            }
            return true
        }

        fun clearForm() {
            fullName.setText("")
            nickname.setText("")
            genderMale.isChecked = false
            genderFemale.isChecked = false
            genderOther.isChecked = false
            ageSpinner.setSelection(0)
            phoneEdit.setText("")
            idEdit.setText("")
            lgaSpinner.setSelection(0)
            communityEdit.setText("")
            clusterEdit.setText("")
            primaryLocationSpinner.setSelection(0)
            cbPlastic.isChecked = false
            cbMetal.isChecked = false
            cbPaper.isChecked = false
            cbGlass.isChecked = false
            cbOrganic.isChecked = false
            cbElectronic.isChecked = false
            cbOtherWaste.isChecked = false
            yearsSpinner.setSelection(0)
            sellDirect.isChecked = false
            sellMiddlemen.isChecked = false
            sellBoth.isChecked = false
            sellOther.isChecked = false
            incomeSpinner.setSelection(0)
            ppeAlways.isChecked = false
            ppeSometimes.isChecked = false
            ppeNever.isChecked = false
            trainingYes.isChecked = false
            trainingNo.isChecked = false
            trainingProviderSpinner.setSelection(0)
            specialNeedsEdit.setText("")
            joinYes.isChecked = false
            joinNo.isChecked = false
            joinNotSure.isChecked = false
            photoBase64 = null
            photoPreview.setImageResource(R.drawable.ic_person)
        }

        saveBtn.setOnClickListener {
            if (!validateRequired()) return@setOnClickListener
            val gender = when {
                genderMale.isChecked -> "Male"
                genderFemale.isChecked -> "Female"
                genderOther.isChecked -> "Other"
                else -> ""
            }
            val selling = when {
                sellDirect.isChecked -> "Directly to recycler"
                sellMiddlemen.isChecked -> "Through middlemen"
                sellBoth.isChecked -> "Both"
                sellOther.isChecked -> "Other"
                else -> null
            }
            val ppe = when {
                ppeAlways.isChecked -> "Always"
                ppeSometimes.isChecked -> "Sometimes"
                ppeNever.isChecked -> "Never"
                else -> ""
            }
            val hadTrainingBool = trainingYes.isChecked
            val join = when {
                joinYes.isChecked -> "Yes"
                joinNo.isChecked -> "No"
                joinNotSure.isChecked -> "Not sure"
                else -> ""
            }
            val wastes = mutableListOf<String>().apply {
                if (cbPlastic.isChecked) add("Plastic")
                if (cbMetal.isChecked) add("Metal")
                if (cbPaper.isChecked) add("Paper/Cardboard")
                if (cbGlass.isChecked) add("Glass")
                if (cbOrganic.isChecked) add("Organic")
                if (cbElectronic.isChecked) add("Electronic waste")
                if (cbOtherWaste.isChecked) add("Other")
            }

            val picker = WastePicker(
                id = UUID.randomUUID().toString(),
                fullName = fullName.text?.toString()?.trim().orEmpty(),
                nickname = nickname.text?.toString()?.trim().orEmpty().ifEmpty { null },
                gender = gender,
                ageRange = ageSpinner.selectedItem?.toString().orEmpty(),
                phone = phoneEdit.text?.toString()?.trim().orEmpty().ifEmpty { null },
                idNumber = idEdit.text?.toString()?.trim().orEmpty().ifEmpty { null },
                lga = lgaSpinner.selectedItem?.toString().orEmpty(),
                community = communityEdit.text?.toString()?.trim().orEmpty(),
                clusterName = clusterEdit.text?.toString()?.trim().orEmpty().ifEmpty { null },
                primaryLocation = primaryLocationSpinner.selectedItem?.toString().orEmpty(),
                wasteTypes = wastes,
                yearsExperience = yearsSpinner.selectedItem?.toString().orEmpty(),
                sellingMode = selling,
                incomeRange = if (incomeSpinner.visibility == View.VISIBLE) incomeSpinner.selectedItem?.toString() else null,
                ppeUsage = ppe,
                hadTraining = hadTrainingBool,
                trainingProvider = if (trainingYes.isChecked) trainingProviderSpinner.selectedItem?.toString() else null,
                willingToJoin = join,
                specialNeeds = specialNeedsEdit.text?.toString()?.trim().orEmpty().ifEmpty { null },
                photoBase64 = photoBase64,
                createdAt = System.currentTimeMillis()
            )

            saveBtn.isEnabled = false
            saveAnotherBtn.isEnabled = false
            Thread {
                var status = -1
                var body = ""
                var error = ""
                val prefs2 = getSharedPreferences("clean_call", MODE_PRIVATE)
                val base = prefs2.getString("api_base_url", BuildConfig.BASE_URL) ?: BuildConfig.BASE_URL
                val token = prefs2.getString("api_token", null)
                val ok = try {
                    val res = ApiClient.postWastePickerResponse(this, picker)
                    status = res.first
                    body = res.second
                    status in 200..299
                } catch (e: Exception) { error = e.message ?: ""; false }
                runOnUiThread {
                    saveBtn.isEnabled = true
                    saveAnotherBtn.isEnabled = true
                    if (ok) {
                        MaterialAlertDialogBuilder(this)
                            .setTitle("Waste picker registered successfully.")
                            .setMessage("Choose next action")
                            .setPositiveButton("View Profile") { dlg, _ ->
                                dlg.dismiss()
                                val intent = android.content.Intent(this, PickerProfileActivity::class.java)
                                intent.putExtra("picker_id", picker.id)
                                startActivity(intent)
                            }
                            .setNegativeButton("Register Another") { dlg, _ -> dlg.dismiss(); clearForm() }
                            .show()
                    } else {
                        PickerStore.savePending(this, picker)
                        MaterialAlertDialogBuilder(this)
                            .setTitle("Saved locally due to network/auth issue.")
                            .setMessage("Status: " + status + "\nToken present: " + (!token.isNullOrEmpty()) + "\nBase: " + base + "\nBody: " + body.take(300) + "\nError: " + error)
                            .setPositiveButton("View Profile") { dlg, _ ->
                                dlg.dismiss()
                                val intent = android.content.Intent(this, PickerProfileActivity::class.java)
                                intent.putExtra("picker_id", picker.id)
                                startActivity(intent)
                            }
                            .setNegativeButton("Register Another") { dlg, _ -> dlg.dismiss(); clearForm() }
                            .show()
                    }
                }
            }.start()
        }

        saveAnotherBtn.setOnClickListener {
            if (!validateRequired()) return@setOnClickListener
            val gender = when {
                genderMale.isChecked -> "Male"
                genderFemale.isChecked -> "Female"
                genderOther.isChecked -> "Other"
                else -> ""
            }
            val selling = when {
                sellDirect.isChecked -> "Directly to recycler"
                sellMiddlemen.isChecked -> "Through middlemen"
                sellBoth.isChecked -> "Both"
                sellOther.isChecked -> "Other"
                else -> null
            }
            val ppe = when {
                ppeAlways.isChecked -> "Always"
                ppeSometimes.isChecked -> "Sometimes"
                ppeNever.isChecked -> "Never"
                else -> ""
            }
            val hadTrainingBool = trainingYes.isChecked
            val join = when {
                joinYes.isChecked -> "Yes"
                joinNo.isChecked -> "No"
                joinNotSure.isChecked -> "Not sure"
                else -> ""
            }
            val wastes = mutableListOf<String>().apply {
                if (cbPlastic.isChecked) add("Plastic")
                if (cbMetal.isChecked) add("Metal")
                if (cbPaper.isChecked) add("Paper/Cardboard")
                if (cbGlass.isChecked) add("Glass")
                if (cbOrganic.isChecked) add("Organic")
                if (cbElectronic.isChecked) add("Electronic waste")
                if (cbOtherWaste.isChecked) add("Other")
            }
            val picker = WastePicker(
                id = UUID.randomUUID().toString(),
                fullName = fullName.text?.toString()?.trim().orEmpty(),
                nickname = nickname.text?.toString()?.trim().orEmpty().ifEmpty { null },
                gender = gender,
                ageRange = ageSpinner.selectedItem?.toString().orEmpty(),
                phone = phoneEdit.text?.toString()?.trim().orEmpty().ifEmpty { null },
                idNumber = idEdit.text?.toString()?.trim().orEmpty().ifEmpty { null },
                lga = lgaSpinner.selectedItem?.toString().orEmpty(),
                community = communityEdit.text?.toString()?.trim().orEmpty(),
                clusterName = clusterEdit.text?.toString()?.trim().orEmpty().ifEmpty { null },
                primaryLocation = primaryLocationSpinner.selectedItem?.toString().orEmpty(),
                wasteTypes = wastes,
                yearsExperience = yearsSpinner.selectedItem?.toString().orEmpty(),
                sellingMode = selling,
                incomeRange = if (incomeSpinner.visibility == View.VISIBLE) incomeSpinner.selectedItem?.toString() else null,
                ppeUsage = ppe,
                hadTraining = hadTrainingBool,
                trainingProvider = if (trainingYes.isChecked) trainingProviderSpinner.selectedItem?.toString() else null,
                willingToJoin = join,
                specialNeeds = specialNeedsEdit.text?.toString()?.trim().orEmpty().ifEmpty { null },
                photoBase64 = photoBase64,
                createdAt = System.currentTimeMillis()
            )
            saveBtn.isEnabled = false
            saveAnotherBtn.isEnabled = false
            Thread {
                var status = -1
                var body = ""
                var error = ""
                val prefs2 = getSharedPreferences("clean_call", MODE_PRIVATE)
                val base = prefs2.getString("api_base_url", BuildConfig.BASE_URL) ?: BuildConfig.BASE_URL
                val token = prefs2.getString("api_token", null)
                val ok = try {
                    val res = ApiClient.postWastePickerResponse(this, picker)
                    status = res.first
                    body = res.second
                    status in 200..299
                } catch (e: Exception) { error = e.message ?: ""; false }
                runOnUiThread {
                    saveBtn.isEnabled = true
                    saveAnotherBtn.isEnabled = true
                    if (ok) {
                        MaterialAlertDialogBuilder(this)
                            .setTitle("Waste picker registered successfully.")
                            .setPositiveButton("OK") { dlg, _ -> dlg.dismiss(); clearForm() }
                            .show()
                    } else {
                        PickerStore.savePending(this, picker)
                        MaterialAlertDialogBuilder(this)
                            .setTitle("Saved locally due to network/auth issue.")
                            .setMessage("Status: " + status + "\nToken present: " + (!token.isNullOrEmpty()) + "\nBase: " + base + "\nBody: " + body.take(300) + "\nError: " + error)
                            .setPositiveButton("OK") { dlg, _ -> dlg.dismiss(); clearForm() }
                            .show()
                    }
                }
            }.start()
        }

        cancelBtn.setOnClickListener { finish() }
    }
}
