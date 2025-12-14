package com.cleancall.mz

import android.os.Bundle
import android.widget.TextView
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class PickerProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker_profile)

        val id = intent.getStringExtra("picker_id") ?: ""
        val picker = PickerStore.find(this, id)

        findViewById<TextView>(R.id.profileName).text = picker?.fullName ?: "Unknown"
        findViewById<TextView>(R.id.profileMeta).text = listOfNotNull(picker?.gender, picker?.ageRange, picker?.lga).joinToString(" • ")

        findViewById<TextView>(R.id.valueNickname).text = picker?.nickname ?: "—"
        findViewById<TextView>(R.id.valuePhone).text = picker?.phone ?: "—"
        findViewById<TextView>(R.id.valueIdNumber).text = picker?.idNumber ?: "—"

        findViewById<TextView>(R.id.valueLga).text = picker?.lga ?: "—"
        findViewById<TextView>(R.id.valueCommunity).text = picker?.community ?: "—"
        findViewById<TextView>(R.id.valueCluster).text = picker?.clusterName ?: "—"
        findViewById<TextView>(R.id.valuePrimaryLocation).text = picker?.primaryLocation ?: "—"

        findViewById<TextView>(R.id.valueWasteTypes).text = picker?.wasteTypes?.joinToString(", ") ?: "—"
        findViewById<TextView>(R.id.valueYears).text = picker?.yearsExperience ?: "—"
        findViewById<TextView>(R.id.valueSellingMode).text = picker?.sellingMode ?: "—"
        findViewById<TextView>(R.id.valueIncomeRange).text = picker?.incomeRange ?: "—"

        findViewById<TextView>(R.id.valuePpeUsage).text = picker?.ppeUsage ?: "—"
        findViewById<TextView>(R.id.valueTraining).text = if (picker?.hadTraining == true) "Yes" else "No"
        findViewById<TextView>(R.id.valueTrainingProvider).text = picker?.trainingProvider ?: "—"
        findViewById<TextView>(R.id.valueWillingJoin).text = picker?.willingToJoin ?: "—"
        findViewById<TextView>(R.id.valueSpecialNeeds).text = picker?.specialNeeds ?: "—"

        findViewById<View>(R.id.backButton).setOnClickListener { finish() }
    }
}
