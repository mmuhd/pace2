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

class PickerListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker_list)

        val recycler = findViewById<RecyclerView>(R.id.pickerRecycler)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = PickerAdapter(emptyList(), emptySet()) { picker ->
            val i = android.content.Intent(this, PickerProfileActivity::class.java)
            i.putExtra("picker_id", picker.id)
            startActivity(i)
        }
        Thread {
            try { PickerStore.migrateLegacyLocalToPending(this) } catch (_: Exception) {}
            try { PickerStore.syncPending(this) } catch (_: Exception) {}
            val remote = try { ApiClient.fetchWastePickers(this) } catch (_: Exception) { emptyList() }
            if (remote.isNotEmpty()) {
                PickerStore.replaceAll(this, remote)
            }
            val pending = try { PickerStore.pending(this) } catch (_: Exception) { emptyList() }
            val list = if (remote.isNotEmpty()) remote + pending else PickerStore.list(this) + pending
            val pendingIds = pending.map { it.id }.toSet()
            runOnUiThread {
                recycler.adapter = PickerAdapter(list, pendingIds) { picker ->
                    val i = android.content.Intent(this, PickerProfileActivity::class.java)
                    i.putExtra("picker_id", picker.id)
                    startActivity(i)
                }
            }
        }.start()
        findViewById<View>(R.id.backButton).setOnClickListener { finish() }
    }

    class PickerAdapter(
        private val items: List<WastePicker>,
        private val pendingIds: Set<String>,
        private val onClick: (WastePicker) -> Unit
    ) : RecyclerView.Adapter<PickerAdapter.VH>() {
        class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val icon: ImageView = itemView.findViewById(R.id.itemIcon)
            val title: TextView = itemView.findViewById(R.id.itemTitle)
            val subtitle: TextView = itemView.findViewById(R.id.itemSubtitle)
            val badge: TextView = itemView.findViewById(R.id.pendingBadge)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_picker, parent, false)
            return VH(v)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val p = items[position]
            holder.icon.setImageResource(R.drawable.ic_person)
            holder.title.text = p.fullName
            holder.subtitle.text = listOfNotNull(p.gender, p.ageRange, p.lga).joinToString(" • ")
            holder.badge.visibility = if (pendingIds.contains(p.id)) View.VISIBLE else View.GONE
            holder.itemView.setOnClickListener { onClick(p) }
        }

        override fun getItemCount(): Int = items.size
    }
}
