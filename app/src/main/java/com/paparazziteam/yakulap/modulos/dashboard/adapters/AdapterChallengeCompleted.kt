package com.paparazziteam.yakulap.modulos.dashboard.adapters

import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.alpha
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ItemChallengeCompletedBinding
import com.paparazziteam.yakulap.helper.application.MyPreferences
import com.paparazziteam.yakulap.helper.design.SlideImageFullScreenActivity
import com.paparazziteam.yakulap.helper.replaceFirstCharInSequenceToUppercase
import com.paparazziteam.yakulap.modulos.dashboard.pojo.MoldeChallengeCompleted

class AdapterChallengeCompleted(challenges:MutableList<MoldeChallengeCompleted>) : RecyclerView.Adapter<AdapterChallengeCompleted.ViewHolder>() {

    var challengesCompleted = challenges


    class ViewHolder(itemview : View): RecyclerView.ViewHolder(itemview) {

        private lateinit var imageChalleng: ShapeableImageView
        private lateinit var contentImage: CardView
        private lateinit var nameChalleng: MaterialTextView
        private lateinit var authorNameChallenge: MaterialTextView
        private lateinit var authorAliasChallenge: MaterialTextView


        val binding = ItemChallengeCompletedBinding.bind(itemview)
        var preferences = MyPreferences()

        fun bind(item:MoldeChallengeCompleted) {

            binding.apply {
                imageChalleng = imgChallenge
                contentImage    = roundedImageView
                nameChalleng    = nameChallenge
                authorNameChallenge    = authorName
                authorAliasChallenge    = authorAlias
            }
            itemView.apply {

                var color = ColorStateList.valueOf(context.getColor(R.color.colorSecondaryLightColor))
                contentImage.backgroundTintList = color
                imageChalleng.setImageResource(R.drawable.ic_galeria)

                Glide
                    .with(context)
                    .load(item.url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_galeria)
                    .into(imageChalleng)

                imageChalleng.setOnClickListener {

                    var list = listOf(item.url)
                    val intent = Intent(this.context, SlideImageFullScreenActivity::class.java)
                    intent.putExtra("lista_imagenes", list.toString())
                    intent.putExtra("position"      , position)
                    context.startActivity(intent)
                }

                nameChalleng.text = replaceFirstCharInSequenceToUppercase(item.name?:"")
                authorAliasChallenge.text = "\uD83C\uDFD6 Estudiante"
                println("Item author name: ${item.author_name}")
                authorNameChallenge.text = replaceFirstCharInSequenceToUppercase(item.author_name?:"")

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.item_challenge_completed,parent,false)
        return  ViewHolder(itemview)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(challengesCompleted[position])
    }

    override fun getItemCount(): Int  = challengesCompleted.size
}