package com.cleancall.mz

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        findViewById<View>(R.id.backButton).setOnClickListener { finish() }
        findViewById<View>(R.id.cardRegisterPicker).setOnClickListener {
            startActivity(android.content.Intent(this, RegisterPickerActivity::class.java))
        }
        findViewById<View>(R.id.cardWomenTraining).setOnClickListener {
            startActivity(android.content.Intent(this, RegisterWomenTrainingActivity::class.java))
        }
        findViewById<View>(R.id.cardSchoolWasteBank).setOnClickListener {
            startActivity(android.content.Intent(this, SchoolWasteBankEntryActivity::class.java))
        }
        findViewById<View>(R.id.cardWasteAggregation).setOnClickListener {
            startActivity(android.content.Intent(this, WasteAggregationEntryActivity::class.java))
        }
        findViewById<View>(R.id.cardStakeholderCommitment).setOnClickListener {
            startActivity(android.content.Intent(this, StakeholderCommitmentEntryActivity::class.java))
        }
        findViewById<View>(R.id.cardEvacuation).setOnClickListener {
            startActivity(android.content.Intent(this, WasteAggregationEntryActivity::class.java))
        }
    }
}
