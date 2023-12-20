package com.thequest.artiquest.view.welcome.feature

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thequest.artiquest.data.local.database.Feature
import com.thequest.artiquest.databinding.ItemFeatureBinding
import com.thequest.artiquest.view.login.LoginActivity
import com.thequest.artiquest.view.signup.SignupActivity

class FeatureAdapter(private val features: List<Feature>) :
    RecyclerView.Adapter<FeatureAdapter.FeatureViewHolder>() {

    inner class FeatureViewHolder(var binding: ItemFeatureBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val titleTextView = binding.titleTextView
        val descriptionTextView = binding.descriptionTextView
        val signinButton = binding.signinButton
        val signupButton = binding.signupButton

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        val binding =
            ItemFeatureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeatureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {
        val feature = features[position]
        holder.titleTextView.visibility = if (position == 0) View.GONE else View.VISIBLE
        holder.titleTextView.text = feature.title
        holder.descriptionTextView.text = feature.description

        // Menyembunyikan/menampilkan tombol berdasarkan flag showButton
        if (feature.showButton) {
            holder.signinButton.visibility = View.VISIBLE
            holder.signupButton.visibility = View.VISIBLE
            holder.signinButton.setOnClickListener {
                val intent = Intent(it.context, LoginActivity::class.java)
                it.context.startActivity(intent)
            }

            holder.signupButton.setOnClickListener {
                val intent = Intent(it.context, SignupActivity::class.java)
                it.context.startActivity(intent)
            }
        } else {
            holder.signinButton.visibility = View.GONE
            holder.signupButton.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return features.size
    }
}
