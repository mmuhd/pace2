package com.cleancall.mz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SchoolWasteBankListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_waste_bank_list)

        val recycler = findViewById<RecyclerView>(R.id.schoolRecycler)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = SWBAdapter(emptyList()) { record ->
            val i = android.content.Intent(this, SchoolWasteBankProfileActivity::class.java)
            i.putExtra("record_id", record.id)
            startActivity(i)
        }
        Thread {
            val remote = try { ApiClient.fetchSchoolWasteBanks(this) } catch (_: Exception) { emptyList() }
            val pending = try { SchoolWasteBankStore.pending(this) } catch (_: Exception) { emptyList() }
            val list = if (remote.isNotEmpty()) {
                SchoolWasteBankStore.replaceAll(this, remote)
                remote + pending
            } else SchoolWasteBankStore.list(this) + pending
            runOnUiThread {
                recycler.adapter = SWBAdapter(list) { record ->
                    val i = android.content.Intent(this, SchoolWasteBankProfileActivity::class.java)
                    i.putExtra("record_id", record.id)
                    startActivity(i)
                }
            }
        }.start()
        findViewById<View>(R.id.backButton).setOnClickListener { finish() }
    }

    class SWBAdapter(
        private val items: List<SchoolWasteBankRecord>,
        private val onClick: (SchoolWasteBankRecord) -> Unit
    ) : RecyclerView.Adapter<SWBAdapter.VH>() {
        class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val icon: ImageView = itemView.findViewById(R.id.itemIcon)
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
            val periodStr = when (s.reportingPeriodType.lowercase()) {
                "daily" -> s.reportingDate ?: ""
                "weekly" -> s.reportingWeekStart ?: ""
                "monthly" -> s.reportingMonth ?: ""
                else -> ""
            }
            val meta = listOfNotNull(s.lga, s.status, periodStr).joinToString(" • ")
            holder.subtitle.text = meta
            holder.itemView.setOnClickListener { onClick(s) }
        }

        override fun getItemCount(): Int = items.size
    }
}
