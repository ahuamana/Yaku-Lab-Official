package com.yakulab.feature.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.paparazziteam.yakulab.binding.helper.load
import com.yakulab.domain.dashboard.ObservationEntity
import com.yakulab.domain.profile.MedalsDomain
import com.yakulab.feature.profile.databinding.ItemMedalBinding

class MedalsAdapter : ListAdapter<MedalsDomain, RecyclerView.ViewHolder>(MedalsDiffCallback()) {

    companion object {
        private class MedalsDiffCallback: DiffUtil.ItemCallback<MedalsDomain>(){
            override fun areItemsTheSame(oldItem: MedalsDomain, newItem: MedalsDomain): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: MedalsDomain, newItem: MedalsDomain): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var onItemClickListener: ((MedalsDomain, position:Int) -> Unit)? = null
    fun onItemClickListener(listener:(MedalsDomain, position:Int)-> Unit){
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ItemMedalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as ViewHolder).bind(item)
    }

    inner class ViewHolder(private val binding: ItemMedalBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MedalsDomain) {
            binding.apply {
                tvName.text = item.name
                tvTotal.text = item.total.toString()

                val progressCircular  = CircularProgressDrawable(root.context)
                progressCircular.strokeWidth = 5f
                progressCircular.centerRadius = 30f
                progressCircular.start()


                ivIcon.load(item.icon, progressCircular)

                root.setOnClickListener {
                    onItemClickListener?.invoke(getItem(adapterPosition), adapterPosition)
                }
            }
        }
    }


}