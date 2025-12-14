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

class ReportSchoolWasteBanksActivity : AppCompatActivity() {
    private val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_school_waste_banks)

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
            val listAll = SchoolWasteBankStore.list(this).filter { lgaSel == null || it.lga == lgaSel }
            val list = listAll.filter { it.createdAt in startMs..endMs }

            recycler.adapter = SchoolAdapter(list) { rec ->
                val i = Intent(this, SchoolWasteBankProfileActivity::class.java)
                i.putExtra("record_id", rec.id)
                startActivity(i)
            }

            val active = listAll.count { it.updatedAt >= System.currentTimeMillis() - 30L * 24L * 60L * 60L * 1000L }
            val plasticsKg = list.mapNotNull { it.plasticCollectedKg }.sum()
            val recycledKg = list.mapNotNull { it.plasticRecycledKg }.sum()
            findViewById<TextView>(R.id.summary1).text = "Active schools (30d): $active"
            findViewById<TextView>(R.id.summary2).text = String.format(Locale.US, "Plastics collected: %.1f kg", plasticsKg)
            findViewById<TextView>(R.id.summary3).text = String.format(Locale.US, "Plastics recycled: %.1f kg", recycledKg)

            exportBtn.setOnClickListener {
                val header = listOf("School","LGA","Period","Status","Plastics(kg)","Recycled(kg)")
                val rows = list.map { s ->
                    listOf(
                        s.schoolName,
                        s.lga,
                        s.reportingPeriodType,
                        s.status,
                        (s.plasticCollectedKg ?: 0.0).toString(),
                        (s.plasticRecycledKg ?: 0.0).toString()
                    )
                }
                val csv = buildString {
                    append(header.joinToString(",")); append('\n')
                    rows.forEach { append(it.joinToString(",")); append('\n') }
                }
                val share = Intent(Intent.ACTION_SEND).apply {
                    type = "text/csv"
                    putExtra(Intent.EXTRA_SUBJECT, "School Waste Banks Report")
                    putExtra(Intent.EXTRA_TEXT, csv)
                }
                startActivity(Intent.createChooser(share, "Export CSV"))
            }
        }

        applyBtn.setOnClickListener { applyFilters() }
        applyFilters()
    }

    class SchoolAdapter(
        private val items: List<SchoolWasteBankRecord>,
        private val onClick: (SchoolWasteBankRecord) -> Unit
    ) : RecyclerView.Adapter<SchoolAdapter.VH>() {
        class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val icon: android.widget.ImageView = itemView.findViewById(R.id.itemIcon)
            val title: TextView = itemView.findViewById(R.id.itemTitle)
            val subtitle: TextView = itemView.findViewById(R.id.itemSubtitle)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_school_waste_bank, parent, false)
            return VH(v)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val s = items[position]
            holder.icon.setImageResource(R.drawable.ic_person)
            holder.title.text = s.schoolName
            val meta = listOfNotNull(s.lga, s.status, s.reportingPeriodType).joinToString(" • ")
            holder.subtitle.text = meta
            holder.itemView.setOnClickListener { onClick(s) }
        }

        override fun getItemCount(): Int = items.size
    }
}
