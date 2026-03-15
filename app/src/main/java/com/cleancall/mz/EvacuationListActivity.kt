package com.cleancall.mz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EvacuationListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evacuation_list)

        val recycler = findViewById<RecyclerView>(R.id.evacRecycler)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = EvacAdapter(emptyList()) { task ->
            val i = android.content.Intent(this, EvacuationDetailActivity::class.java)
            i.putExtra("evac_id", task.id)
            i.putExtra("evac_source", task.sourceType)
            i.putExtra("evac_lga", task.lga)
            i.putExtra("evac_status", task.status)
            i.putExtra("evac_assigned", task.assignedTo)
            startActivity(i)
        }
        Thread {
            val remote = try { ApiClient.fetchEvacuationTasks(this) } catch (_: Exception) { emptyList() }
            val pending = try { EvacuationStore.pending(this) } catch (_: Exception) { emptyList() }
            val list = if (remote.isNotEmpty()) remote + pending else EvacuationStore.list(this) + pending
            runOnUiThread {
                recycler.adapter = EvacAdapter(list) { task ->
                    val i = android.content.Intent(this, EvacuationDetailActivity::class.java)
                    i.putExtra("evac_id", task.id)
                    i.putExtra("evac_source", task.sourceType)
                    i.putExtra("evac_lga", task.lga)
                    i.putExtra("evac_status", task.status)
                    i.putExtra("evac_assigned", task.assignedTo)
                    startActivity(i)
                }
            }
        }.start()
        findViewById<View>(R.id.backButton).setOnClickListener { finish() }

        val lgaSpinner: android.widget.Spinner = findViewById(R.id.filterLga)
        val statusSpinner: android.widget.Spinner = findViewById(R.id.filterStatus)
        lgaSpinner.adapter = android.widget.ArrayAdapter(this, R.layout.spinner_item, listOf("All","Dala","Fagge","Gwale","Kano Municipal","Nassarawa","Tarauni","Kumbotso","Ungogo","Other")).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }
        statusSpinner.adapter = android.widget.ArrayAdapter(this, R.layout.spinner_item, listOf("All","pending","collected","delivered")).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }
        val refilter = {
            Thread {
                val lga = lgaSpinner.selectedItem?.toString()?.takeIf { it != "All" }
                val status = statusSpinner.selectedItem?.toString()?.takeIf { it != "All" }
                val remote = try {
                    val baseUrl = "" // not used; filtering done on client for simplicity
                    ApiClient.fetchEvacuationTasks(this)
                } catch (_: Exception) { emptyList() }
                val pending = try { EvacuationStore.pending(this) } catch (_: Exception) { emptyList() }
                var list = if (remote.isNotEmpty()) remote + pending else EvacuationStore.list(this) + pending
                if (lga != null) list = list.filter { it.lga == lga }
                if (status != null) list = list.filter { it.status == status }
                runOnUiThread {
                    recycler.adapter = EvacAdapter(list) { task ->
                        val i = android.content.Intent(this, EvacuationDetailActivity::class.java)
                        i.putExtra("evac_id", task.id)
                        i.putExtra("evac_source", task.sourceType)
                        i.putExtra("evac_lga", task.lga)
                        i.putExtra("evac_status", task.status)
                        i.putExtra("evac_assigned", task.assignedTo)
                        startActivity(i)
                    }
                }
            }.start()
        }
        lgaSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>, view: View?, position: Int, id: Long) { refilter() }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
        }
        statusSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>, view: View?, position: Int, id: Long) { refilter() }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
        }
    }

    class EvacAdapter(private val items: List<EvacuationTask>, private val onClick: (EvacuationTask) -> Unit) : RecyclerView.Adapter<EvacAdapter.VH>() {
        class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView = itemView.findViewById(R.id.itemTitle)
            val subtitle: TextView = itemView.findViewById(R.id.itemSubtitle)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_waste_aggregation, parent, false)
            return VH(v)
        }
        override fun onBindViewHolder(holder: VH, position: Int) {
            val t = items[position]
            holder.title.text = "${t.sourceType}: ${t.sourceName ?: t.address ?: t.lga}"
            val kg = t.totalKg?.let { String.format("%.1f kg", it) } ?: "—"
            holder.subtitle.text = listOfNotNull(t.lga, t.status, kg).joinToString(" • ")
            holder.itemView.setOnClickListener { onClick(t) }
        }
        override fun getItemCount(): Int = items.size
    }
}
