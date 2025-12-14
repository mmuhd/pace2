package com.cleancall.mz

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReportWasteAggregationActivity : AppCompatActivity() {
    private val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_waste_aggregation)

        findViewById<View>(R.id.backButton).setOnClickListener { finish() }

        val dateRangeSpinner: Spinner = findViewById(R.id.dateRangeSpinner)
        val lgaSpinner: Spinner = findViewById(R.id.lgaSpinner)
        val startDateText: TextView = findViewById(R.id.startDateText)
        val endDateText: TextView = findViewById(R.id.endDateText)
        val applyBtn: MaterialButton = findViewById(R.id.applyBtn)
        val exportBtn: MaterialButton = findViewById(R.id.exportBtn)
        val recycler: RecyclerView = findViewById(R.id.reportRecycler)
        recycler.layoutManager = LinearLayoutManager(this)

        val dateRanges = listOf("Today", "This Week", "This Month", "Custom")
        dateRangeSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, dateRanges).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }

        val lgas = listOf("All", "Dala", "Fagge", "Gwale", "Kano Municipal", "Nassarawa", "Tarauni", "Kumbotso", "Ungogo", "Other")
        lgaSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, lgas).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }

        val cal = Calendar.getInstance()
        startDateText.setOnClickListener {
            DatePickerDialog(this, { _, y, m, d ->
                val c = Calendar.getInstance(); c.set(y, m, d, 0, 0, 0); c.set(Calendar.MILLISECOND, 0)
                startDateText.text = fmt.format(c.time)
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        endDateText.setOnClickListener {
            DatePickerDialog(this, { _, y, m, d ->
                val c = Calendar.getInstance(); c.set(y, m, d, 23, 59, 59); c.set(Calendar.MILLISECOND, 999)
                endDateText.text = fmt.format(c.time)
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        fun rangeNow(): Pair<Long, Long> {
            val now = System.currentTimeMillis()
            val c = Calendar.getInstance()
            val end = now
            val start = when (dateRangeSpinner.selectedItem?.toString()) {
                "Today" -> { c.set(Calendar.HOUR_OF_DAY, 0); c.set(Calendar.MINUTE, 0); c.set(Calendar.SECOND, 0); c.set(Calendar.MILLISECOND, 0); c.timeInMillis }
                "This Week" -> { c.set(Calendar.DAY_OF_WEEK, c.firstDayOfWeek); c.set(Calendar.HOUR_OF_DAY, 0); c.set(Calendar.MINUTE, 0); c.set(Calendar.SECOND, 0); c.set(Calendar.MILLISECOND, 0); c.timeInMillis }
                "This Month" -> { c.set(Calendar.DAY_OF_MONTH, 1); c.set(Calendar.HOUR_OF_DAY, 0); c.set(Calendar.MINUTE, 0); c.set(Calendar.SECOND, 0); c.set(Calendar.MILLISECOND, 0); c.timeInMillis }
                "Custom" -> {
                    val s = startDateText.text?.toString()?.trim().orEmpty(); val e = endDateText.text?.toString()?.trim().orEmpty()
                    val cs = Calendar.getInstance(); val ce = Calendar.getInstance()
                    try { cs.time = fmt.parse(s)!! } catch (_: Exception) {}
                    try { ce.time = fmt.parse(e)!! } catch (_: Exception) {}
                    Pair(cs.timeInMillis, ce.timeInMillis)
                }
                else -> now
            }
            return if (start is Pair<*, *>) start as Pair<Long, Long> else (start as Long to end)
        }

        fun applyFilters() {
            val (startMs, endMs) = rangeNow()
            val lgaSel = lgaSpinner.selectedItem?.toString()?.takeIf { it != "All" }
            val list = WasteAggregationStore.list(this).filter { (it.createdAt in startMs..endMs) && (lgaSel == null || it.lga == lgaSel) }

            recycler.adapter = AggAdapter(list) { r ->
                val i = Intent(this, WasteAggregationProfileActivity::class.java)
                i.putExtra("report_id", r.id)
                startActivity(i)
            }

            val totalKg = list.mapNotNull { it.totalWasteKg }.sum()
            val avgPct = list.mapNotNull { it.recyclablePercentage?.toDouble() }.let { if (it.isEmpty()) 0.0 else it.average() }
            val estRecyclables = list.mapNotNull { r ->
                val pct = r.recyclablePercentage ?: 0
                val kg = r.totalWasteKg ?: 0.0
                kg * pct / 100.0
            }.sum()
            findViewById<TextView>(R.id.summary1).text = String.format(Locale.US, "Total collected: %.1f kg", totalKg)
            findViewById<TextView>(R.id.summary2).text = String.format(Locale.US, "Avg recyclable %%: %.1f%%", avgPct)
            findViewById<TextView>(R.id.summary3).text = String.format(Locale.US, "Recyclables est.: %.1f kg", estRecyclables)

            exportBtn.setOnClickListener {
                val header = listOf("LGA","Source","Site","Date","Total(kg)","Recyclable%","Disposal")
                val rows = list.map { r ->
                    listOf(
                        r.lga,
                        r.wasteSource,
                        r.siteName,
                        r.collectionDate,
                        (r.totalWasteKg ?: 0.0).toString(),
                        (r.recyclablePercentage ?: 0).toString(),
                        r.finalDisposalSite
                    )
                }
                val csv = buildString {
                    append(header.joinToString(",")); append('\n')
                    rows.forEach { append(it.joinToString(",")); append('\n') }
                }
                val share = Intent(Intent.ACTION_SEND).apply {
                    type = "text/csv"
                    putExtra(Intent.EXTRA_SUBJECT, "Waste Aggregation Report")
                    putExtra(Intent.EXTRA_TEXT, csv)
                }
                startActivity(Intent.createChooser(share, "Export CSV"))
            }
        }

        applyBtn.setOnClickListener { applyFilters() }
        applyFilters()
    }

    class AggAdapter(
        private val items: List<WasteAggregationReport>,
        private val onClick: (WasteAggregationReport) -> Unit
    ) : RecyclerView.Adapter<AggAdapter.VH>() {
        class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val icon: android.widget.ImageView = itemView.findViewById(R.id.itemIcon)
            val title: TextView = itemView.findViewById(R.id.itemTitle)
            val subtitle: TextView = itemView.findViewById(R.id.itemSubtitle)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_waste_aggregation, parent, false)
            return VH(v)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val r = items[position]
            holder.icon.setImageResource(R.drawable.ic_trash)
            holder.title.text = r.siteName ?: "Aggregation"
            val meta = listOfNotNull(r.lga, r.wasteSource, r.collectionDate, r.totalWasteKg?.let { "${it} kg" }).joinToString(" • ")
            holder.subtitle.text = meta
            holder.itemView.setOnClickListener { onClick(r) }
        }

        override fun getItemCount(): Int = items.size
    }
}
