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

class WomenTrainingListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_women_training_list)

        val recycler = findViewById<RecyclerView>(R.id.womenRecycler)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = WomenTrainingAdapter(emptyList()) { session ->
            val i = android.content.Intent(this, WomenTrainingProfileActivity::class.java)
            i.putExtra("session_id", session.id)
            startActivity(i)
        }
        Thread {
            val remote = try { ApiClient.fetchWomenTrainingSessions(this) } catch (_: Exception) { emptyList() }
            val list = if (remote.isNotEmpty()) {
                WomenTrainingStore.replaceAll(this, remote)
                remote
            } else WomenTrainingStore.list(this)
            runOnUiThread {
                recycler.adapter = WomenTrainingAdapter(list) { session ->
                    val i = android.content.Intent(this, WomenTrainingProfileActivity::class.java)
                    i.putExtra("session_id", session.id)
                    startActivity(i)
                }
            }
        }.start()
        findViewById<View>(R.id.backButton).setOnClickListener { finish() }
    }

    class WomenTrainingAdapter(
        private val items: List<WomenTrainingSession>,
        private val onClick: (WomenTrainingSession) -> Unit
    ) : RecyclerView.Adapter<WomenTrainingAdapter.VH>() {
        class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val icon: ImageView = itemView.findViewById(R.id.itemIcon)
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
            val meta = listOfNotNull(s.date, s.lga, "${s.totalWomen} women", s.sessionCode).joinToString(" • ")
            holder.subtitle.text = meta
            holder.itemView.setOnClickListener { onClick(s) }
        }

        override fun getItemCount(): Int = items.size
    }
}
