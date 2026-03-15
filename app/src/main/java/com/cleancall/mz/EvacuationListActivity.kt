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
        recycler.adapter = EvacAdapter(emptyList())
        Thread {
            val list = try { EvacuationStore.list(this) } catch (_: Exception) { emptyList() }
            runOnUiThread {
                recycler.adapter = EvacAdapter(list)
            }
        }.start()
        findViewById<View>(R.id.backButton).setOnClickListener { finish() }
    }

    class EvacAdapter(private val items: List<EvacuationTask>) : RecyclerView.Adapter<EvacAdapter.VH>() {
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
        }
        override fun getItemCount(): Int = items.size
    }
}
