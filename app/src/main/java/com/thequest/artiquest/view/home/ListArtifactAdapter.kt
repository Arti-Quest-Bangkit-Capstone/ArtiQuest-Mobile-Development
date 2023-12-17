package com.thequest.artiquest.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thequest.artiquest.R
import com.thequest.artiquest.databinding.ItemArtifactsBinding

class ListArtifactAdapter(private val dataList: List<String>) :
    RecyclerView.Adapter<ListArtifactAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemArtifactsBinding) : RecyclerView.ViewHolder(binding.root) {
        val textItem: TextView = itemView.findViewById(R.id.tv_item_name)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemArtifactsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind data to UI elements inside the ViewHolder
        holder.textItem.text = dataList[position]
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}