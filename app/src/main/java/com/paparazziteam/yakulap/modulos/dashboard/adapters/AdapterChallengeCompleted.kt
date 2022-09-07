package com.paparazziteam.yakulap.modulos.dashboard.adapters

import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ItemChallengeCompletedBinding
import com.paparazziteam.yakulap.helper.application.MyPreferences
import com.paparazziteam.yakulap.helper.application.toast
import com.paparazziteam.yakulap.helper.design.SlideImageFullScreenActivity
import com.paparazziteam.yakulap.helper.preventDoubleClick
import com.paparazziteam.yakulap.helper.replaceFirstCharInSequenceToUppercase
import com.paparazziteam.yakulap.modulos.dashboard.fragments.BottomDialogFragmentComentar
import com.paparazziteam.yakulap.modulos.dashboard.pojo.MoldeChallengeCompleted
import com.paparazziteam.yakulap.modulos.dashboard.pojo.Reaccion
import com.paparazziteam.yakulap.modulos.providers.ReaccionProvider


class AdapterChallengeCompleted(challenges:MutableList<MoldeChallengeCompleted>) : RecyclerView.Adapter<AdapterChallengeCompleted.ViewHolder>() {

    var challengesCompleted = challenges


    class ViewHolder(itemview : View): RecyclerView.ViewHolder(itemview) {

        private lateinit var imageChalleng: ShapeableImageView
        private lateinit var contentImage: CardView
        private lateinit var nameChalleng: MaterialTextView
        private lateinit var authorNameChallenge: MaterialTextView
        private lateinit var authorAliasChallenge: MaterialTextView
        private lateinit var linearLayoutShare: LinearLayout
        private lateinit var layoutComment: LinearLayout
        private lateinit var likeImg: LottieAnimationView

        var mActionProvider = ReaccionProvider()

        val binding = ItemChallengeCompletedBinding.bind(itemview)
        var preferences = MyPreferences()

        fun bind(item:MoldeChallengeCompleted) {

            binding.apply {
                imageChalleng   = imgChallenge
                contentImage    = roundedImageView
                nameChalleng    = nameChallenge
                authorNameChallenge     = authorName
                authorAliasChallenge    = authorAlias
                likeImg                 = likeImageView
                linearLayoutShare       = linearCompartir
                layoutComment     = linearLayoutComentar

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

                //Setup like
                setupLike(item)

                //Setup share
                setupShare()

                //SetupComment
                setupComment(item)
            }

        }

        private fun setupComment(item: MoldeChallengeCompleted) {
            layoutComment.setOnClickListener {
                    val bottomSheetDialogFragment = BottomDialogFragmentComentar.newInstance(item.id?:"")
                    bottomSheetDialogFragment.show((itemView.context as FragmentActivity).supportFragmentManager,"bottomSheetDialogFragment")

            }
        }

        private fun setupShare() {
            linearLayoutShare.setOnClickListener {
                it.preventDoubleClick()
                it.context.toast("No disponible por el momento (¡Muy pronto!)")
            }
        }

        private fun setupLike(item: MoldeChallengeCompleted) {

            //First Time Like
           getFirsTimeLike(item)

            likeImg.setOnClickListener {
                it.preventDoubleClick()
                mActionProvider.getUserLike(preferences.email_login, item.id)?.get()
                    ?.addOnSuccessListener {
                        if(it.isEmpty){
                            println("Like is empty")
                            createLike(item,true)
                        }else{
                            //if already exist
                            val status  = it.documents[0].get("status").toString().toBoolean()
                            val idEmail = it.documents[0].get("id_email").toString()
                            println("Update like: $status, $idEmail")
                            updateLike(idEmail, status)
                        }
                    }

            }
        }

        private fun getFirsTimeLike(item: MoldeChallengeCompleted) {
            mActionProvider.getUserLike(preferences.email_login, item.id)?.get()
                ?.addOnSuccessListener {
                    if(it.isEmpty){
                        likeImg.setImageResource(R.drawable.ic_love)
                    }else{
                        //if already exist
                        val status  = it.documents[0].get("status").toString().toBoolean()
                        println("Status -> $status")
                        if(status) likeImg.setImageResource(R.drawable.love_yaku) else likeImg.setImageResource(R.drawable.ic_love)
                    }
                }
        }

        private fun updateLike(idEmail: String, status: Boolean) {
            mActionProvider.updateStatus(idEmail, !status)?.addOnCompleteListener {
                if(it.isSuccessful){
                    likeAnimation(likeImg, R.raw.heart_love, status)
                }
            }
        }

        private fun createLike(item: MoldeChallengeCompleted, like: Boolean) {

            val idEmail= "${item.id.toString()}_${preferences.email_login}"

            var mAction = Reaccion()
            mAction.id_email = idEmail
            mAction.status = like
            mAction.email = preferences.email_login
            mAction.id = item.id
            mAction.type = "Like"

            mActionProvider.create(mAction)?.addOnCompleteListener {
                if (it.isSuccessful) {
                    likeAnimation(likeImg, R.raw.heart_love,!like)
                }
            }
        }

        private fun likeAnimation(image: LottieAnimationView,
                                  animation:Int,
                                  like:Boolean):Boolean{

            if(!like){
                image.setAnimation(animation)
                image.playAnimation()
            }else{
                image.setImageResource(R.drawable.ic_love)
            }

            return !like
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