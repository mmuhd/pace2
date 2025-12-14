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

class StakeholderCommitmentListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stakeholder_commitment_list)
        val recycler = findViewById<RecyclerView>(R.id.commitRecycler)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = CommitAdapter(emptyList()) { item ->
            val i = android.content.Intent(this, StakeholderCommitmentProfileActivity::class.java)
            i.putExtra("commit_id", item.id)
            startActivity(i)
        }
        Thread {
            val remote = try { ApiClient.fetchStakeholderCommitments(this) } catch (_: Exception) { emptyList() }
            val list = if (remote.isNotEmpty()) {
                StakeholderCommitmentStore.replaceAll(this, remote)
                remote
            } else StakeholderCommitmentStore.list(this)
            runOnUiThread {
                recycler.adapter = CommitAdapter(list) { item ->
                    val i = android.content.Intent(this, StakeholderCommitmentProfileActivity::class.java)
                    i.putExtra("commit_id", item.id)
                    startActivity(i)
                }
            }
        }.start()
        findViewById<View>(R.id.backButton).setOnClickListener { finish() }
    }

    class CommitAdapter(
        private val items: List<StakeholderCommitment>,
        private val onClick: (StakeholderCommitment) -> Unit
    ) : RecyclerView.Adapter<CommitAdapter.VH>() {
        class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val icon: ImageView = itemView.findViewById(R.id.itemIcon)
            val title: TextView = itemView.findViewById(R.id.itemTitle)
            val subtitle: TextView = itemView.findViewById(R.id.itemSubtitle)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_stakeholder_commitment, parent, false)
            return VH(v)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val s = items[position]
            holder.icon.setImageResource(R.drawable.ic_person)
            holder.title.text = s.stakeholderName
            val meta = listOfNotNull(s.stakeholderType, s.lga, s.status, s.dueDate).joinToString(" • ")
            holder.subtitle.text = meta
            holder.itemView.setOnClickListener { onClick(s) }
        }

        override fun getItemCount(): Int = items.size
    }
}
