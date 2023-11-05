package com.paparazziteam.yakulap.presentation.dashboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.paparazziteam.yakulab.binding.helper.load
import com.paparazziteam.yakulap.databinding.ItemNearbySpeciesBinding
import com.paparazziteam.yakulap.databinding.ItemSpeciesWithArBinding
import com.yakulab.domain.dashboard.ItemSpecieAR
import com.yakulab.domain.dashboard.ObservationEntity

class AdapterSpeciesWithAR : ListAdapter<ItemSpecieAR, RecyclerView.ViewHolder>(ItemSpeciesWithARDiffCallback()) {

    companion object {
        private class ItemSpeciesWithARDiffCallback: DiffUtil.ItemCallback<ItemSpecieAR>(){
            override fun areItemsTheSame(oldItem: ItemSpecieAR, newItem: ItemSpecieAR): Boolean {
                return oldItem.uuid == newItem.uuid
            }
            override fun areContentsTheSame(oldItem: ItemSpecieAR, newItem: ItemSpecieAR): Boolean {
                return oldItem == newItem
            }
        }
    }


    private var onItemClickListener: ((ItemSpecieAR, position:Int) -> Unit)? = null
    fun onItemClickListener(listener:(ItemSpecieAR, position:Int)-> Unit){
        onItemClickListener = listener
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemSpeciesARViewHolder(
            ItemSpeciesWithArBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as ItemSpeciesARViewHolder).bind(item)
    }

    inner class ItemSpeciesARViewHolder(private val binding: ItemSpeciesWithArBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemSpecieAR) {
            binding.apply {

                title.text = item.name

                icArgumentedReality.setOnClickListener {
                    onItemClickListener?.invoke(getItem(adapterPosition), adapterPosition)
                }

                imgPreview.apply {
                    load(item.preview?: "")
                    setOnClickListener {
                        onItemClickListener?.invoke(getItem(adapterPosition), adapterPosition)
                    }
                }

                root.setOnClickListener {
                    onItemClickListener?.invoke(getItem(adapterPosition), adapterPosition)
                }
            }
        }
    }




}