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

class ReportWomenTrainingActivity : AppCompatActivity() {
    private val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_women_training)

        findViewById<View>(R.id.backButton).setOnClickListener { finish() }

        val dateRangeSpinner: Spinner = findViewById(R.id.dateRangeSpinner)
        val lgaSpinner: Spinner = findViewById(R.id.lgaSpinner)
        val venueFilterSpinner: Spinner = findViewById(R.id.venueFilterSpinner)
        val followupFilterSpinner: Spinner = findViewById(R.id.followupFilterSpinner)
        val searchEdit: com.google.android.material.textfield.TextInputEditText = findViewById(R.id.searchEdit)
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

        val allSessionsAll = WomenTrainingStore.list(this)
        val venueOptions = listOf("All") + allSessionsAll.map { it.venueType }.distinct().sorted()
        venueFilterSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, venueOptions).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }
        val followOptions = listOf("All", "None") + allSessionsAll.mapNotNull { it.followupType }.distinct().sorted()
        followupFilterSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, followOptions).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }

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
            val list0 = WomenTrainingStore.list(this).filter { (it.createdAt in startMs..endMs) && (lgaSel == null || it.lga == lgaSel) }
            val venueSel = venueFilterSpinner.selectedItem?.toString()?.takeIf { it != "All" }
            val followSel = followupFilterSpinner.selectedItem?.toString()
            var list = list0.filter { (venueSel == null || it.venueType == venueSel) && (
                when {
                    followSel == null || followSel == "All" -> true
                    followSel == "None" -> it.followupType.isNullOrEmpty()
                    else -> it.followupType == followSel
                }
            ) }
            val q = searchEdit.text?.toString()?.trim()?.lowercase(Locale.ROOT).orEmpty()
            if (q.isNotEmpty()) {
                list = list.filter { s ->
                    listOfNotNull(s.title, s.community, s.facilitatorName, s.sessionCode).any { f -> f.lowercase(Locale.ROOT).contains(q) }
                }
            }

            recycler.adapter = SessionAdapter(list) { s ->
                val i = Intent(this, WomenTrainingProfileActivity::class.java)
                i.putExtra("session_id", s.id)
                startActivity(i)
            }

            findViewById<TextView>(R.id.summary1).text = "Sessions: ${list.size}"
            findViewById<TextView>(R.id.summary2).text = "Total women: ${list.sumOf { it.totalWomen }}"
            findViewById<TextView>(R.id.summary3).text = "Committed to sorting: ${list.sumOf { it.committedToSorting }}"

            exportBtn.setOnClickListener {
                val header = listOf("Title","Date","LGA","Community","Venue","Facilitator","Total Women","Committed","Code")
                val rows = list.map { s ->
                    listOf(
                        s.title,
                        s.date,
                        s.lga,
                        s.community,
                        s.venueType,
                        s.facilitatorName,
                        s.totalWomen.toString(),
                        s.committedToSorting.toString(),
                        s.sessionCode
                    )
                }
                val csv = buildString {
                    append(header.joinToString(",")); append('\n')
                    rows.forEach { append(it.joinToString(",")); append('\n') }
                }
                val share = Intent(Intent.ACTION_SEND).apply {
                    type = "text/csv"
                    putExtra(Intent.EXTRA_SUBJECT, "Women Trainings Report")
                    putExtra(Intent.EXTRA_TEXT, csv)
                }
                startActivity(Intent.createChooser(share, "Export CSV"))
            }
        }

        applyBtn.setOnClickListener { applyFilters() }
        searchEdit.addTextChangedListener(object: android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { applyFilters() }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })
        applyFilters()
    }

    class SessionAdapter(
        private val items: List<WomenTrainingSession>,
        private val onClick: (WomenTrainingSession) -> Unit
    ) : RecyclerView.Adapter<SessionAdapter.VH>() {
        class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val icon: android.widget.ImageView = itemView.findViewById(R.id.itemIcon)
            val title: TextView = itemView.findViewById(R.id.itemTitle)
            val subtitle: TextView = itemView.findViewById(R.id.itemSubtitle)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_women_training, parent, false)
            return VH(v)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val s = items[position]
            holder.icon.setImageResource(R.drawable.ic_person)
            holder.title.text = s.title
            holder.subtitle.text = listOfNotNull(s.date, s.lga, "${s.totalWomen} women", s.sessionCode).joinToString(" • ")
            holder.itemView.setOnClickListener { onClick(s) }
        }

        override fun getItemCount(): Int = items.size
    }
}
