package com.thequest.artiquest.view.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thequest.artiquest.data.remote.api.response.AgentItem
import com.thequest.artiquest.databinding.ItemArtifactsBinding
import com.thequest.artiquest.view.detail.DetailActivity

class ListArtifactAdapter(private val listArtifact: List<AgentItem>) :
    RecyclerView.Adapter<ListArtifactAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemArtifactsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        // Bind data to UI elements inside the ViewHolder
        val artifact = listArtifact[position]
        holder.bind(artifact)
//        holder.textItem.text = listArtifact[position]
    }

    override fun getItemCount(): Int {
        return listArtifact.size
    }

    class ListViewHolder(var binding: ItemArtifactsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(artifact: AgentItem) {
            binding.apply {
                tvItemName.text = artifact.displayName
                Glide.with(root.context)
                    .load(artifact.displayIconSmall)
                    .circleCrop()
                    .into(imgItemPhoto)
            }


            binding.root.setOnClickListener {
                val intentDetail = Intent(binding.root.context, DetailActivity::class.java)
                intentDetail.putExtra(EXTRA_ID, artifact.uuid)
                binding.root.context.startActivity(intentDetail)
            }
        }
    }

    companion object {
        private const val EXTRA_ID = "extra_id"
    }
}