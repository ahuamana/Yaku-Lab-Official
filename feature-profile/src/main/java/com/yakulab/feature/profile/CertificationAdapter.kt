package com.yakulab.feature.profile

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.paparazziteam.yakulab.binding.helper.beGone
import com.paparazziteam.yakulab.binding.helper.beVisible
import com.paparazziteam.yakulab.binding.helper.load
import com.yakulab.domain.profile.CertificationsDomain
import com.yakulab.domain.profile.EnumCertified
import com.yakulab.feature.profile.databinding.ItemCertificationBinding


class CertificationAdapter : ListAdapter<CertificationsDomain, RecyclerView.ViewHolder>(CertificationDiffCallback()) {

    companion object {
        private class CertificationDiffCallback: DiffUtil.ItemCallback<CertificationsDomain>(){
            override fun areItemsTheSame(oldItem: CertificationsDomain, newItem: CertificationsDomain): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: CertificationsDomain, newItem: CertificationsDomain): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var onItemClickListener: ((CertificationsDomain, position:Int) -> Unit)? = null
    fun onItemClickListener(listener:(CertificationsDomain, position:Int)-> Unit){
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ItemCertificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as ViewHolder).bind(item)
    }

    inner class ViewHolder(private val binding: ItemCertificationBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CertificationsDomain) {
            binding.apply {
                tvName.text = item.name
                EnumCertified.values().find { it.value == item.type }.apply {
                    tvType.text = if(item.isCertified) this?.getCertifiedName() else "No certificado"
                    //set colors
                    val colorString = this?.getColorBackground()
                    val colorText = this?.getColorText()

                    //set text color and background color from enum avoid !!
                    tvType.setTextColor(Color.parseColor(colorText))
                    cardType.setCardBackgroundColor(Color.parseColor(colorString))
                }

                val progressCircular  = CircularProgressDrawable(root.context)
                progressCircular.strokeWidth = 5f
                progressCircular.centerRadius = 30f
                progressCircular.start()


                ivIcon.load(item.icon, progressCircular)

                root.setOnClickListener {
                    onItemClickListener?.invoke(getItem(adapterPosition), adapterPosition)
                }

                if(item.isCertified){
                    tvType
                }
            }
        }
    }


}