package com.cleancall.mz

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.util.Base64

class StakeholderCommitmentProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stakeholder_commitment_profile)

        val id = intent.getStringExtra("commit_id") ?: ""
        val item = StakeholderCommitmentStore.find(this, id)

        findViewById<View>(R.id.backButton).setOnClickListener { finish() }

        findViewById<TextView>(R.id.profileTitle).text = item?.stakeholderName ?: "Stakeholder Commitment"
        findViewById<TextView>(R.id.profileSubtitle).text = listOfNotNull(item?.stakeholderType, item?.lga, item?.status).joinToString(" • ")

        findViewById<TextView>(R.id.valueName).text = item?.stakeholderName ?: "—"
        findViewById<TextView>(R.id.valueType).text = item?.stakeholderType ?: "—"
        findViewById<TextView>(R.id.valueLga).text = item?.lga ?: "—"
        findViewById<TextView>(R.id.valueContact).text = item?.contactPerson ?: "—"
        findViewById<TextView>(R.id.valuePhone).text = item?.phone ?: "—"

        findViewById<TextView>(R.id.valueDateEngaged).text = item?.engagementDate ?: "—"
        findViewById<TextView>(R.id.valueEngType).text = item?.engagementType ?: "—"
        findViewById<TextView>(R.id.valueEngagedBy).text = item?.engagedByUserId ?: "—"
        findViewById<TextView>(R.id.valueEngDesc).text = item?.engagementDescription ?: "—"

        findViewById<TextView>(R.id.valueCommitment).text = item?.commitmentText ?: "—"
        findViewById<TextView>(R.id.valueCategory).text = item?.commitmentCategory ?: "—"
        findViewById<TextView>(R.id.valueDueDate).text = item?.dueDate ?: "—"
        findViewById<TextView>(R.id.valuePriority).text = item?.priorityLevel ?: "—"

        findViewById<TextView>(R.id.valueActionTaken).text = item?.actionTaken ?: "—"
        findViewById<TextView>(R.id.valueStatus).text = item?.status ?: "—"
        findViewById<TextView>(R.id.valueFollowReq).text = if (item?.followupRequired == true) "Yes" else "No"
        findViewById<TextView>(R.id.valueFollowType).text = item?.followupType ?: "—"
        findViewById<TextView>(R.id.valueFollowDate).text = item?.followupDate ?: "—"
        findViewById<TextView>(R.id.valueAssigned).text = item?.followupAssignedTo ?: "—"
        findViewById<TextView>(R.id.valueRemarks).text = item?.remarks ?: "—"

        fun setPreview(img: ImageView, b64: String?) {
            if (b64.isNullOrEmpty()) { img.setImageResource(R.drawable.ic_person); return }
            val bytes = Base64.decode(b64, Base64.NO_WRAP)
            try {
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                img.setImageBitmap(bmp)
            } catch (_: Exception) {
                img.setImageResource(R.drawable.ic_person)
            }
        }

        val p1: ImageView = findViewById(R.id.preview1)
        val p2: ImageView = findViewById(R.id.preview2)
        val p3: ImageView = findViewById(R.id.preview3)
        setPreview(p1, item?.evidence?.getOrNull(0))
        setPreview(p2, item?.evidence?.getOrNull(1))
        setPreview(p3, item?.evidence?.getOrNull(2))
    }
}

