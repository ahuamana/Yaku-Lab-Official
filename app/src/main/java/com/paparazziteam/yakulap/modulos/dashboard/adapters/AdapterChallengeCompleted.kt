package com.paparazziteam.yakulap.modulos.dashboard.adapters

import android.content.Intent
import android.content.res.ColorStateList
import android.util.Log
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
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ItemChallengeCompletedBinding
import com.paparazziteam.yakulap.helper.application.MyPreferences
import com.paparazziteam.yakulap.helper.application.toast
import com.paparazziteam.yakulap.helper.design.SlideImageFullScreenActivity
import com.paparazziteam.yakulap.helper.preventDoubleClick
import com.paparazziteam.yakulap.helper.replaceFirstCharInSequenceToUppercase
import com.paparazziteam.yakulap.helper.toJson
import com.paparazziteam.yakulap.modulos.dashboard.fragments.BottomDialogFragmentComentar
import com.paparazziteam.yakulap.modulos.dashboard.fragments.BottomDialogFragmentMoreOptions
import com.paparazziteam.yakulap.modulos.dashboard.interfaces.clickedItemCompleted
import com.paparazziteam.yakulap.modulos.dashboard.pojo.MoldeChallengeCompleted
import com.paparazziteam.yakulap.modulos.repositorio.ReaccionProvider


class AdapterChallengeCompleted(
    challenges: MutableList<MoldeChallengeCompleted>,
    val clickedItemCompleted: clickedItemCompleted?
) : RecyclerView.Adapter<AdapterChallengeCompleted.ViewHolder>() {

    var challengesCompleted = challenges

    fun removePost(idChallenge:String){
        Log.d("TAG","Remove Post")
        var index = challengesCompleted.indexOfFirst {
            it.id == idChallenge
        }
        if(index<0) return
        challengesCompleted.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index,itemCount)
    }

    class ViewHolder(itemview : View): RecyclerView.ViewHolder(itemview) {

        private lateinit var imageChalleng: ShapeableImageView
        private lateinit var contentImage: CardView
        private lateinit var nameChalleng: MaterialTextView
        private lateinit var authorNameChallenge: MaterialTextView
        private lateinit var authorAliasChallenge: MaterialTextView
        private lateinit var linearLayoutShare: LinearLayout
        private lateinit var layoutComment: LinearLayout
        private lateinit var likeImg: LottieAnimationView
        private lateinit var itemOptions: ShapeableImageView

        var mActionProvider = ReaccionProvider()

        val binding = ItemChallengeCompletedBinding.bind(itemview)
        var preferences = MyPreferences()

        fun bind(item: MoldeChallengeCompleted, clickedItem: clickedItemCompleted?) {

            binding.apply {
                imageChalleng   = imgChallenge
                contentImage    = roundedImageView
                nameChalleng    = nameChallenge
                authorNameChallenge     = authorName
                authorAliasChallenge    = authorAlias
                likeImg                 = likeImageView
                linearLayoutShare       = linearCompartir
                layoutComment     = linearLayoutComentar
                itemOptions             = optionsChallenge

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
                setupLike(item, clickedItem)

                //Setup share
                setupShare()

                //SetupComment
                setupComment(item)

                //ReportPost
                reportPostOption(item)
            }

        }

        private fun reportPostOption(item: MoldeChallengeCompleted) {
            itemOptions.setOnClickListener{
                val fragment = BottomDialogFragmentMoreOptions.newInstance(toJson(item))
                fragment.show((itemView.context as FragmentActivity).supportFragmentManager,"bottomSheetMoreOptions")
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

        private fun setupLike(item: MoldeChallengeCompleted, clickedItem: clickedItemCompleted?) {
            //First Time Like
            getFirsTimeLike(item,clickedItem)
        }

        private fun getFirsTimeLike(
            item: MoldeChallengeCompleted,
            clickedItem: clickedItemCompleted?
        ) {
            var status = false
            mActionProvider.getUserLike(preferences.email_login, item.id)?.get()
                ?.addOnSuccessListener {
                    if(it.isEmpty){
                        likeImg.setImageResource(R.drawable.ic_love)
                    }else{
                        //if already exist
                        status  = it.documents[0].get("status").toString()?.toBoolean() == true
                        println("Status -> $status")
                        setImageResource(status)
                        //likeAnimation(likeImg, R.raw.heart_love, status)
                    }
                }?.addOnFailureListener {
                    FirebaseCrashlytics.getInstance().recordException(it)
                    setImageResource(status)
                }

            likeImg.setOnClickListener {
                //setupLike
                status = likeAnimation(likeImg, R.raw.heart_love, status)
                clickedItem?.clickOnUpdateLike(item)
            }
        }

        private fun setImageResource(status:Boolean){
            if(status) likeImg.setImageResource(R.drawable.love_yaku) else likeImg.setImageResource(R.drawable.ic_love)
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
        holder.bind(challengesCompleted[position],clickedItemCompleted)
    }

    override fun getItemCount(): Int  = challengesCompleted.size
}