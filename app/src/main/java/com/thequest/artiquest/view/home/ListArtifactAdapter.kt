package com.thequest.artiquest.view.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thequest.artiquest.data.remote.api.response.ArtifactItem
import com.thequest.artiquest.databinding.ItemArtifactsBinding
import com.thequest.artiquest.view.detail.DetailActivity

class ListArtifactAdapter(private var listArtifact: List<ArtifactItem>) :
    RecyclerView.Adapter<ListArtifactAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemArtifactsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val artifact = listArtifact.getOrNull(position)
        artifact?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return listArtifact.size
    }

    // Fungsi untuk mengatur data baru dan memberitahu adapter bahwa data telah berubah
    fun setData(newList: List<ArtifactItem>) {
        listArtifact = newList
        notifyDataSetChanged()
    }


    // Kelas untuk menangani perbandingan antara dua list
    class ArtifactDiffCallback(
        private val oldList: List<ArtifactItem>,
        private val newList: List<ArtifactItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    class ListViewHolder(var binding: ItemArtifactsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(artifact: ArtifactItem) {
            binding.apply {
                tvItemName.text = artifact.name
                Glide.with(root.context)
                    .load(artifact.image)
                    .circleCrop()
                    .into(imgItemPhoto)
            }


            binding.root.setOnClickListener {
                val intentDetail = Intent(binding.root.context, DetailActivity::class.java)
                intentDetail.putExtra(EXTRA_ID, artifact.id.toString())
                binding.root.context.startActivity(intentDetail)
            }
        }
    }

    companion object {
        private const val EXTRA_ID = "extra_id"
    }
}