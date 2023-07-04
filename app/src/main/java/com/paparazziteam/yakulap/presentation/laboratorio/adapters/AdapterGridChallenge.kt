package com.paparazziteam.yakulap.presentation.laboratorio.adapters

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.DialogMoreInfoBinding
import com.paparazziteam.yakulap.databinding.ItemChallengeOptionBinding
import com.paparazziteam.yakulap.helper.*
import com.paparazziteam.yakulap.presentation.laboratorio.pojo.DataChallenge
import com.paparazziteam.yakulap.presentation.laboratorio.views.ChallengeActivity

class AdapterGridChallenge(challengesList:MutableList<DataChallenge>):RecyclerView.Adapter<AdapterGridChallenge.ViewHolder>() {

    var list = challengesList

    class ViewHolder(itemview : View): RecyclerView.ViewHolder(itemview) {

        val TAG = javaClass.name

        private lateinit var imageChalleng: ShapeableImageView
        private lateinit var textTitle: MaterialTextView


        val binding = ItemChallengeOptionBinding.bind(itemview)

        fun bind(item: DataChallenge) {
            binding.apply {
                imageChalleng   = imgChallenge
                textTitle       = tvTitulo
            }

            //UI loading
            binding.progressLoading.beVisible()

            itemView.apply {
                Glide.with(context)
                    .load(item.url)
                    .listener(object:RequestListener<Drawable>{
                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.progressLoading.beGone()
                            return false
                        }

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.progressLoading.beGone()
                            return false
                        }
                    })
                    .error(R.drawable.galeria_imagenes)
                    .into(imageChalleng)

                setOnClickListener {
                    it.preventDoubleClick()
                    createDialog(item)
                }
                imageChalleng.setOnClickListener {
                    it.preventDoubleClick()
                    createDialog(item)
                }
            }

            textTitle.text = replaceFirstCharInSequenceToUppercase(item.name?:"")

        }

        private fun createDialog(item: DataChallenge) {
            itemView.apply {

                var customBinding = DialogMoreInfoBinding.inflate(LayoutInflater.from(context))

                val dialog = Dialog(context)
                dialog.setCancelable(true)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.setContentView(customBinding.root)

                Glide.with(context)
                    .load(item.url)
                    .placeholder(R.drawable.galeria_imagenes)
                    .into(customBinding.imageViewChallenge)

                customBinding.textDescription.text = item.description
                customBinding.textTitle.text = replaceFirstCharInSequenceToUppercase(item.name?:"")
                val params = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                params.setMargins(30, 0, 30, 0)
                customBinding.cardParent.setLayoutParams(params)

                customBinding.btnCancel.setOnClickListener(View.OnClickListener { dialog.dismiss() })

                customBinding.btnOk.setOnClickListener(View.OnClickListener {
                    context.startActivity(Intent(context,ChallengeActivity::class.java).apply {
                        putExtra(Constants.EXTRA_CHALLENGE, toJson(item))
                    })
                })
                dialog.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.item_challenge_option,parent,false)
        return ViewHolder(itemview)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int  = list.size
}