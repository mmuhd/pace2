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

class ReportWastePickersActivity : AppCompatActivity() {
    private val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_waste_pickers)

        findViewById<View>(R.id.backButton).setOnClickListener { finish() }

        val dateRangeSpinner: Spinner = findViewById(R.id.dateRangeSpinner)
        val lgaSpinner: Spinner = findViewById(R.id.lgaSpinner)
        val clusterSpinner: Spinner = findViewById(R.id.clusterSpinner)
        val ppeSpinner: Spinner = findViewById(R.id.ppeSpinner)
        val willingSpinner: Spinner = findViewById(R.id.willingSpinner)
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

        val allPickersAll = PickerStore.list(this)
        val clusters = listOf("All") + allPickersAll.mapNotNull { it.clusterName }.distinct().sorted()
        clusterSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, clusters).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }
        val ppeOptions = listOf("All") + allPickersAll.map { it.ppeUsage }.distinct().sorted()
        ppeSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, ppeOptions).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }
        val willingOptions = listOf("All") + allPickersAll.map { it.willingToJoin }.distinct().sorted()
        willingSpinner.adapter = ArrayAdapter(this, R.layout.spinner_item, willingOptions).apply { setDropDownViewResource(R.layout.spinner_dropdown_item) }

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
            val list0 = PickerStore.list(this).filter { (it.createdAt in startMs..endMs) && (lgaSel == null || it.lga == lgaSel) }
            val clusterSel = clusterSpinner.selectedItem?.toString()?.takeIf { it != "All" }
            val ppeSel = ppeSpinner.selectedItem?.toString()?.takeIf { it != "All" }
            val willingSel = willingSpinner.selectedItem?.toString()?.takeIf { it != "All" }
            var list = list0.filter { (clusterSel == null || it.clusterName == clusterSel) && (ppeSel == null || it.ppeUsage == ppeSel) && (willingSel == null || it.willingToJoin == willingSel) }
            val q = searchEdit.text?.toString()?.trim()?.lowercase(Locale.ROOT).orEmpty()
            if (q.isNotEmpty()) {
                list = list.filter { rec ->
                    listOfNotNull(rec.fullName, rec.community, rec.clusterName, rec.lga).any { f -> f.lowercase(Locale.ROOT).contains(q) }
                }
            }

            recycler.adapter = PickerAdapter(list) { picker ->
                val i = Intent(this, PickerProfileActivity::class.java)
                i.putExtra("picker_id", picker.id)
                startActivity(i)
            }

            findViewById<TextView>(R.id.summary1).text = "Total registered: ${list.size}"
            val ppeDist = list.groupBy { it.ppeUsage }.mapValues { it.value.size }.entries.joinToString(", ") { "${it.key}: ${it.value}" }
            findViewById<TextView>(R.id.summary2).text = "PPE usage: ${if (ppeDist.isEmpty()) "—" else ppeDist}"
            val willDist = list.groupBy { it.willingToJoin }.mapValues { it.value.size }.entries.joinToString(", ") { "${it.key}: ${it.value}" }
            findViewById<TextView>(R.id.summary3).text = "Willing to formalize: ${if (willDist.isEmpty()) "—" else willDist}"

            exportBtn.setOnClickListener {
                val header = listOf("Full Name","Gender","Age","LGA","Cluster","PPE","Willing","Created")
                val rows = list.map { p ->
                    listOf(
                        p.fullName,
                        p.gender,
                        p.ageRange,
                        p.lga,
                        p.clusterName ?: "",
                        p.ppeUsage,
                        p.willingToJoin,
                        fmt.format(Calendar.getInstance().apply { timeInMillis = p.createdAt }.time)
                    )
                }
                val csv = buildString {
                    append(header.joinToString(",")); append('\n')
                    rows.forEach { append(it.joinToString(",")); append('\n') }
                }
                val share = Intent(Intent.ACTION_SEND).apply {
                    type = "text/csv"
                    putExtra(Intent.EXTRA_SUBJECT, "Waste Pickers Report")
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

    class PickerAdapter(
        private val items: List<WastePicker>,
        private val onClick: (WastePicker) -> Unit
    ) : RecyclerView.Adapter<PickerAdapter.VH>() {
        class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val icon: android.widget.ImageView = itemView.findViewById(R.id.itemIcon)
            val title: TextView = itemView.findViewById(R.id.itemTitle)
            val subtitle: TextView = itemView.findViewById(R.id.itemSubtitle)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_picker, parent, false)
            return VH(v)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val p = items[position]
            holder.icon.setImageResource(R.drawable.ic_person)
            holder.title.text = p.fullName
            holder.subtitle.text = listOfNotNull(p.gender, p.ageRange, p.lga, p.clusterName).joinToString(" • ")
            holder.itemView.setOnClickListener { onClick(p) }
        }

        override fun getItemCount(): Int = items.size
    }
}
