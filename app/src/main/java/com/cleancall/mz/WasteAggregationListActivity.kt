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

class WasteAggregationListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waste_aggregation_list)

        val recycler = findViewById<RecyclerView>(R.id.aggRecycler)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = AggAdapter(emptyList()) { report ->
            val i = android.content.Intent(this, WasteAggregationProfileActivity::class.java)
            i.putExtra("report_id", report.id)
            startActivity(i)
        }
        Thread {
            val remote = try { ApiClient.fetchWasteAggregations(this) } catch (_: Exception) { emptyList() }
            val pending = try { WasteAggregationStore.pending(this) } catch (_: Exception) { emptyList() }
            val list = if (remote.isNotEmpty()) remote + pending else WasteAggregationStore.list(this) + pending
            runOnUiThread {
                recycler.adapter = AggAdapter(list) { report ->
                    val i = android.content.Intent(this, WasteAggregationProfileActivity::class.java)
                    i.putExtra("report_id", report.id)
                    startActivity(i)
                }
            }
        }.start()
        findViewById<View>(R.id.backButton).setOnClickListener { finish() }
    }

    class AggAdapter(
        private val items: List<WasteAggregationReport>,
        private val onClick: (WasteAggregationReport) -> Unit
    ) : RecyclerView.Adapter<AggAdapter.VH>() {
        class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val icon: ImageView = itemView.findViewById(R.id.itemIcon)
            val title: TextView = itemView.findViewById(R.id.itemTitle)
            val subtitle: TextView = itemView.findViewById(R.id.itemSubtitle)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_waste_aggregation, parent, false)
            return VH(v)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val s = items[position]
            holder.icon.setImageResource(R.drawable.ic_person)
            holder.title.text = s.siteName
            val meta = listOfNotNull(s.lga, s.collectionDate, "${s.totalWasteKg} kg").joinToString(" • ")
            holder.subtitle.text = meta
            holder.itemView.setOnClickListener { onClick(s) }
        }

        override fun getItemCount(): Int = items.size
    }
}
