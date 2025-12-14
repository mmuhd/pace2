package com.cleancall.mz

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReportsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)

        findViewById<View>(R.id.backButton).setOnClickListener { finish() }

        val dateRangeSpinner: Spinner = findViewById(R.id.dateRangeSpinner)
        val lgaSpinner: Spinner = findViewById(R.id.lgaSpinner)
        val sourceSpinner: Spinner = findViewById(R.id.sourceSpinner)
        val startDateText: TextView = findViewById(R.id.startDateText)
        val endDateText: TextView = findViewById(R.id.endDateText)
        val applyBtn: MaterialButton = findViewById(R.id.applyBtn)

        val lgas = listOf("All", "Dala", "Fagge", "Gwale", "Kano Municipal", "Nassarawa", "Tarauni", "Kumbotso", "Ungogo", "Other")
        lgaSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, lgas).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }

        val dateRanges = listOf("Today", "This Week", "This Month", "Custom")
        dateRangeSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, dateRanges).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }

        val sources = listOf("All", "Schools", "Women", "Dumpsites", "Markets")
        sourceSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, sources).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }

        val cal = Calendar.getInstance()
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        fun pickDate(target: TextView) {
            DatePickerDialog(this, { _, y, m, d ->
                val c = Calendar.getInstance(); c.set(y, m, d)
                target.text = fmt.format(c.time)
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        startDateText.setOnClickListener { pickDate(startDateText) }
        endDateText.setOnClickListener { pickDate(endDateText) }

        fun rangeNow(): Pair<Long, Long> {
            val now = System.currentTimeMillis()
            val c = Calendar.getInstance()
            val end = now
            val start = when (dateRangeSpinner.selectedItem?.toString()) {
                "Today" -> {
                    c.set(Calendar.HOUR_OF_DAY, 0); c.set(Calendar.MINUTE, 0); c.set(Calendar.SECOND, 0); c.set(Calendar.MILLISECOND, 0)
                    c.timeInMillis
                }
                "This Week" -> {
                    c.set(Calendar.DAY_OF_WEEK, c.firstDayOfWeek)
                    c.set(Calendar.HOUR_OF_DAY, 0); c.set(Calendar.MINUTE, 0); c.set(Calendar.SECOND, 0); c.set(Calendar.MILLISECOND, 0)
                    c.timeInMillis
                }
                "This Month" -> {
                    c.set(Calendar.DAY_OF_MONTH, 1)
                    c.set(Calendar.HOUR_OF_DAY, 0); c.set(Calendar.MINUTE, 0); c.set(Calendar.SECOND, 0); c.set(Calendar.MILLISECOND, 0)
                    c.timeInMillis
                }
                "Custom" -> {
                    val s = startDateText.text?.toString()?.trim().orEmpty()
                    val e = endDateText.text?.toString()?.trim().orEmpty()
                    val cs = Calendar.getInstance()
                    val ce = Calendar.getInstance()
                    try { cs.time = fmt.parse(s)!! } catch (_: Exception) {}
                    try { ce.time = fmt.parse(e)!! } catch (_: Exception) {}
                    Pair(cs.timeInMillis, ce.timeInMillis)
                }
                else -> now to now
            }
            return if (start is Pair<*, *>) start as Pair<Long, Long> else (start as Long to end)
        }

        fun applyFiltersAndCompute() {
            val range = rangeNow()
            val startMs = range.first
            val endMs = range.second
            val lgaSel = lgaSpinner.selectedItem?.toString()?.takeIf { it != "All" }
            val srcSel = sourceSpinner.selectedItem?.toString()?.takeIf { it != "All" }

            val pickers = PickerStore.list(this).filter { (it.createdAt in startMs..endMs) && (lgaSel == null || it.lga == lgaSel) }
            val sessions = WomenTrainingStore.list(this).filter { (it.createdAt in startMs..endMs) && (lgaSel == null || it.lga == lgaSel) }
            val schoolsAll = SchoolWasteBankStore.list(this).filter { lgaSel == null || it.lga == lgaSel }
            val agg = WasteAggregationStore.list(this).filter { (it.createdAt in startMs..endMs) && (lgaSel == null || it.lga == lgaSel) }
            val commits = StakeholderCommitmentStore.list(this).filter { (it.createdAt in startMs..endMs) && (lgaSel == null || it.lga == lgaSel) }

            val activeSchools30 = schoolsAll.count { it.updatedAt >= System.currentTimeMillis() - 30L * 24L * 60L * 60L * 1000L }

            val totalAggKg = agg.mapNotNull { it.totalWasteKg }.sum()
            val estRecyclablesKg = agg.mapNotNull { r ->
                val pct = r.recyclablePercentage ?: 0
                val kg = r.totalWasteKg ?: 0.0
                kg * pct / 100.0
            }.sum()

            if (srcSel != null) {
                when (srcSel) {
                    "Schools" -> {
                        findViewById<TextView>(R.id.kpiPickers).text = "—"
                        findViewById<TextView>(R.id.kpiWomen).text = "—"
                        findViewById<TextView>(R.id.kpiSchools).text = activeSchools30.toString()
                        findViewById<TextView>(R.id.kpiAggKg).text = "—"
                        findViewById<TextView>(R.id.kpiRecyclablesKg).text = "—"
                        val statusCounts = commits.groupBy { it.status.lowercase(Locale.ROOT) }.mapValues { it.value.size }
                        findViewById<TextView>(R.id.kpiCommits).text = listOf(
                            "pending" to (statusCounts["pending"] ?: 0),
                            "ongoing" to (statusCounts["ongoing"] ?: 0),
                            "completed" to (statusCounts["completed"] ?: 0)
                        ).joinToString(" • ") { "${it.first}: ${it.second}" }
                    }
                    "Women" -> {
                        findViewById<TextView>(R.id.kpiPickers).text = "—"
                        findViewById<TextView>(R.id.kpiWomen).text = "${sessions.size} sessions • ${sessions.sumOf { it.totalWomen }} women"
                        findViewById<TextView>(R.id.kpiSchools).text = "—"
                        findViewById<TextView>(R.id.kpiAggKg).text = "—"
                        findViewById<TextView>(R.id.kpiRecyclablesKg).text = "—"
                        val statusCounts = commits.groupBy { it.status.lowercase(Locale.ROOT) }.mapValues { it.value.size }
                        findViewById<TextView>(R.id.kpiCommits).text = listOf(
                            "pending" to (statusCounts["pending"] ?: 0),
                            "ongoing" to (statusCounts["ongoing"] ?: 0),
                            "completed" to (statusCounts["completed"] ?: 0)
                        ).joinToString(" • ") { "${it.first}: ${it.second}" }
                    }
                    else -> {
                        findViewById<TextView>(R.id.kpiPickers).text = pickers.size.toString()
                        findViewById<TextView>(R.id.kpiWomen).text = "${sessions.size} sessions • ${sessions.sumOf { it.totalWomen }} women"
                        findViewById<TextView>(R.id.kpiSchools).text = activeSchools30.toString()
                        findViewById<TextView>(R.id.kpiAggKg).text = String.format(Locale.US, "%.1f kg", totalAggKg)
                        findViewById<TextView>(R.id.kpiRecyclablesKg).text = String.format(Locale.US, "%.1f kg", estRecyclablesKg)
                        val statusCounts = commits.groupBy { it.status.lowercase(Locale.ROOT) }.mapValues { it.value.size }
                        findViewById<TextView>(R.id.kpiCommits).text = listOf(
                            "pending" to (statusCounts["pending"] ?: 0),
                            "ongoing" to (statusCounts["ongoing"] ?: 0),
                            "completed" to (statusCounts["completed"] ?: 0)
                        ).joinToString(" • ") { "${it.first}: ${it.second}" }
                    }
                }
            } else {
                findViewById<TextView>(R.id.kpiPickers).text = pickers.size.toString()
                findViewById<TextView>(R.id.kpiWomen).text = "${sessions.size} sessions • ${sessions.sumOf { it.totalWomen }} women"
                findViewById<TextView>(R.id.kpiSchools).text = activeSchools30.toString()
                findViewById<TextView>(R.id.kpiAggKg).text = String.format(Locale.US, "%.1f kg", totalAggKg)
                findViewById<TextView>(R.id.kpiRecyclablesKg).text = String.format(Locale.US, "%.1f kg", estRecyclablesKg)
                val statusCounts = commits.groupBy { it.status.lowercase(Locale.ROOT) }.mapValues { it.value.size }
                findViewById<TextView>(R.id.kpiCommits).text = listOf(
                    "pending" to (statusCounts["pending"] ?: 0),
                    "ongoing" to (statusCounts["ongoing"] ?: 0),
                    "completed" to (statusCounts["completed"] ?: 0)
                ).joinToString(" • ") { "${it.first}: ${it.second}" }
            }

            val lgaAgg = agg.groupBy { it.lga }.mapValues { it.value.mapNotNull { r -> r.totalWasteKg }.sum() }
            val topLgas = lgaAgg.entries.sortedByDescending { it.value }.take(3).joinToString(", ") { "${it.key}: ${String.format(Locale.US, "%.1f", it.value)} kg" }
            findViewById<TextView>(R.id.insightTopLgas).text = topLgas.ifEmpty { "—" }

            val schoolPlastic = schoolsAll.groupBy { it.schoolName }.mapValues { it.value.mapNotNull { r -> r.plasticCollectedKg }.sum() }
            val topSchoolsPlastics = schoolPlastic.entries.sortedByDescending { it.value }.take(3).joinToString(", ") { "${it.key}: ${String.format(Locale.US, "%.1f", it.value)} kg" }
            findViewById<TextView>(R.id.insightTopSchools).text = topSchoolsPlastics.ifEmpty { "—" }

            val overdue = commits.count { it.dueDate != null && it.status.lowercase(Locale.ROOT) != "completed" && try {
                val cDue = Calendar.getInstance(); cDue.time = fmt.parse(it.dueDate)!!; cDue.timeInMillis < System.currentTimeMillis()
            } catch (_: Exception) { false } }
            findViewById<TextView>(R.id.insightOverdue).text = overdue.toString()
        }

        applyBtn.setOnClickListener { applyFiltersAndCompute() }
        applyFiltersAndCompute()

        findViewById<View>(R.id.cardPickersReport).setOnClickListener {
            startActivity(android.content.Intent(this, ReportWastePickersActivity::class.java))
        }
        findViewById<View>(R.id.cardWomenReport).setOnClickListener {
            startActivity(android.content.Intent(this, ReportWomenTrainingActivity::class.java))
        }
        findViewById<View>(R.id.cardSchoolReport).setOnClickListener {
            startActivity(android.content.Intent(this, ReportSchoolWasteBanksActivity::class.java))
        }
        findViewById<View>(R.id.cardAggReport).setOnClickListener {
            startActivity(android.content.Intent(this, ReportWasteAggregationActivity::class.java))
        }
        findViewById<View>(R.id.cardCommitReport).setOnClickListener {
            startActivity(android.content.Intent(this, ReportStakeholderCommitmentsActivity::class.java))
        }
    }
}

