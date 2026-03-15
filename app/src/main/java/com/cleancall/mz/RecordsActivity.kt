package com.cleancall.mz

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class RecordsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_records)

        findViewById<View>(R.id.backButton).setOnClickListener { finish() }
        findViewById<View>(R.id.cardRegisteredPickers).setOnClickListener {
            startActivity(android.content.Intent(this, PickerListActivity::class.java))
        }
        findViewById<View>(R.id.cardWomenTraining).setOnClickListener {
            startActivity(android.content.Intent(this, WomenTrainingListActivity::class.java))
        }
        findViewById<View>(R.id.cardSchoolWasteBank).setOnClickListener {
            startActivity(android.content.Intent(this, SchoolWasteBankListActivity::class.java))
        }
        findViewById<View>(R.id.cardWasteAggregation).setOnClickListener {
            startActivity(android.content.Intent(this, WasteAggregationListActivity::class.java))
        }
        findViewById<View>(R.id.cardStakeholderCommitment).setOnClickListener {
            startActivity(android.content.Intent(this, StakeholderCommitmentListActivity::class.java))
        }
        findViewById<View>(R.id.cardEvacuation).setOnClickListener {
            startActivity(android.content.Intent(this, EvacuationListActivity::class.java))
        }
    }
}
