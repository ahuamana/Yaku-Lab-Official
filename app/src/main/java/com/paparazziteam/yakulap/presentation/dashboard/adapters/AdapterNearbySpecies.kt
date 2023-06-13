package com.paparazziteam.yakulap.presentation.dashboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.paparazziteam.yakulap.databinding.ItemNearbySpeciesBinding
import com.paparazziteam.yakulap.domain.dashboard.ObservationEntity
import com.paparazziteam.yakulap.helper.load

class AdapterNearbySpecies : ListAdapter<ObservationEntity, RecyclerView.ViewHolder>(ItemSpeciesDiffCallback()) {

    companion object {
        private class ItemSpeciesDiffCallback: DiffUtil.ItemCallback<ObservationEntity>(){
            override fun areItemsTheSame(oldItem: ObservationEntity, newItem: ObservationEntity): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: ObservationEntity, newItem: ObservationEntity): Boolean {
                return oldItem == newItem
            }
        }
    }


    private var onItemClickListener: ((ObservationEntity, position:Int) -> Unit)? = null
    fun onItemClickListener(listener:(ObservationEntity, position:Int)-> Unit){
        onItemClickListener = listener
    }


    private var onClickItemWiki: ((ObservationEntity, position:Int) -> Unit)? = null
    fun onClickItemWiki(listener: (ObservationEntity, position: Int) -> Unit) {
        onClickItemWiki = listener
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemSpeciesViewHolder(
            ItemNearbySpeciesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as ItemSpeciesViewHolder).bind(item)
    }

    inner class ItemSpeciesViewHolder(private val binding: ItemNearbySpeciesBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imgIconWikipedia.setOnClickListener {
                onClickItemWiki?.invoke(getItem(adapterPosition), adapterPosition)
            }
        }

        fun bind(item: ObservationEntity) {
            binding.apply {
                imgIcon.apply {
                    setOnClickListener { onItemClickListener?.invoke(item, adapterPosition) }
                    load(item.identifications.first()?.taxon?.default_photo?.medium_url?:"")
                }
                authorName.text = item.identifications.first()?.taxon?.name
            }
        }
    }




}