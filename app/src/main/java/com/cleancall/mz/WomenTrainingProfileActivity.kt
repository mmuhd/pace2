package com.cleancall.mz

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class WomenTrainingProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_women_training_profile)

        val id = intent.getStringExtra("session_id") ?: ""
        val session = WomenTrainingStore.find(this, id)

        findViewById<TextView>(R.id.profileTitle).text = session?.title ?: "Session"
        val meta = listOfNotNull(session?.date, session?.lga, session?.community).joinToString(" • ")
        findViewById<TextView>(R.id.profileMeta).text = meta

        findViewById<TextView>(R.id.valueDate).text = session?.date ?: "—"
        val time = listOfNotNull(session?.startTime, session?.endTime).joinToString(" – ")
        findViewById<TextView>(R.id.valueTime).text = if (time.isNotEmpty()) time else "—"
        findViewById<TextView>(R.id.valueLgaCommunity).text = listOfNotNull(session?.lga, session?.community).joinToString(" • ")
        findViewById<TextView>(R.id.valueVenue).text = session?.venueType ?: "—"
        findViewById<TextView>(R.id.valueFacilitator).text = session?.facilitatorName ?: "—"
        findViewById<TextView>(R.id.valueOrganisation).text = session?.organisation ?: "—"
        findViewById<TextView>(R.id.valueSessionCode).text = session?.sessionCode ?: "—"

        findViewById<TextView>(R.id.valueTotalWomen).text = session?.totalWomen?.toString() ?: "—"
        val ageParts = listOfNotNull(
            session?.age18_25?.let { "18–25: $it" },
            session?.age26_35?.let { "26–35: $it" },
            session?.age36_45?.let { "36–45: $it" },
            session?.age46Plus?.let { "46+: $it" }
        )
        findViewById<TextView>(R.id.valueAgeBreakdown).text = if (ageParts.isNotEmpty()) ageParts.joinToString(", ") else "—"
        findViewById<TextView>(R.id.valueHouseholds).text = session?.householdsRepresented?.let { it.toString() } ?: "—"
        findViewById<TextView>(R.id.valueAttendance).text = session?.attendanceType ?: "—"

        findViewById<TextView>(R.id.valueTopics).text = session?.topics?.joinToString(", ") ?: "—"
        findViewById<TextView>(R.id.valueMethods).text = session?.methods?.joinToString(", ") ?: "—"
        findViewById<TextView>(R.id.valueDuration).text = session?.duration ?: "—"

        val preSection = findViewById<View>(R.id.preSection)
        findViewById<TextView>(R.id.valuePreUsed).text = if (session?.pretestUsed == true) "Yes" else "No"
        if (session?.pretestUsed == true) {
            val know = listOfNotNull(session.knowledgeSortingBefore?.let { "Before: $it" }, session.knowledgeSortingAfter?.let { "After: $it" }).joinToString(" • ")
            findViewById<TextView>(R.id.valueKnow).text = if (know.isNotEmpty()) know else "—"
            val cats = listOfNotNull(session.categoriesCorrectBefore?.let { "Before: $it" }, session.categoriesCorrectAfter?.let { "After: $it" }).joinToString(" • ")
            findViewById<TextView>(R.id.valueCats).text = if (cats.isNotEmpty()) cats else "—"
            val burn = listOfNotNull(session.burnFalseBefore?.let { "Before: $it" }, session.burnFalseAfter?.let { "After: $it" }).joinToString(" • ")
            findViewById<TextView>(R.id.valueBurn).text = if (burn.isNotEmpty()) burn else "—"
            val conf = listOfNotNull(session.confidenceAvgBefore?.let { "Before: $it" }, session.confidenceAvgAfter?.let { "After: $it" }).joinToString(" • ")
            findViewById<TextView>(R.id.valueConfidence).text = if (conf.isNotEmpty()) conf else "—"
            val imp = listOfNotNull(session.importanceAvgBefore?.let { "Before: $it" }, session.importanceAvgAfter?.let { "After: $it" }).joinToString(" • ")
            findViewById<TextView>(R.id.valueImportance).text = if (imp.isNotEmpty()) imp else "—"
            preSection.visibility = View.VISIBLE
        } else {
            preSection.visibility = View.GONE
        }

        findViewById<TextView>(R.id.valueCommitted).text = session?.committedToSorting?.toString() ?: "—"
        findViewById<TextView>(R.id.valueFollowup).text = session?.followupType ?: "—"
        findViewById<TextView>(R.id.valueFollowupDate).text = session?.followupDate ?: "—"
        findViewById<TextView>(R.id.valueNotes).text = session?.notes ?: "—"

        findViewById<View>(R.id.backButton).setOnClickListener { finish() }
    }
}

