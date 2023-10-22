package com.paparazziteam.yakulap.presentation.dashboard.adapters


import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.paparazziteam.yakulab.binding.helper.application.MyPreferences
import com.paparazziteam.yakulab.binding.helper.preventDoubleClick
import com.paparazziteam.yakulab.binding.utils.replaceFirstCharInSequenceToUppercase
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ItemChallengeCompletedBinding
import com.ahuaman.data.dashboard.providers.ReaccionProvider
import com.paparazziteam.yakulap.presentation.dashboard.interfaces.onClickThread
import com.yakulab.domain.dashboard.ChallengeCompleted
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AdapterChallengeCompleted @Inject constructor(
    val clickedItemCompleted: onClickThread?,
    private val mPreferences: MyPreferences,
) : RecyclerView.Adapter<AdapterChallengeCompleted.ViewHolder>() {

    private var oldListChallenges = mutableListOf<ChallengeCompleted>()

    fun setUserList(newList: List<ChallengeCompleted>) {
        val diffResult = DiffUtil.calculateDiff(ChallengeCompletedDiffCallback(oldListChallenges, newList))
        oldListChallenges.clear()
        oldListChallenges.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    private var onItemClickListener: ((ChallengeCompleted) -> Unit)? = null
    fun onClickUpdateLikeListener(listener:(ChallengeCompleted)-> Unit){
        onItemClickListener = listener
    }

    private var onClickCompartirListener: ((ChallengeCompleted) -> Unit)? = null
    fun onClickShareListener(listener:(ChallengeCompleted)-> Unit){
        onClickCompartirListener = listener
    }

    private var onClickImage: ((ChallengeCompleted, position:Int) -> Unit)? = null
    fun onClickImageListener(listener:(ChallengeCompleted, position:Int)-> Unit){
        onClickImage = listener
    }

    var count = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        println("OnCreateView ${count++}")
        val itemview = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_challenge_completed,parent,false)
        return  ViewHolder(itemview)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(oldListChallenges[position],clickedItemCompleted, mPreferences)
    }

    inner class ViewHolder(itemview : View): RecyclerView.ViewHolder(itemview) {

        var mActionProvider = ReaccionProvider()
        val binding = ItemChallengeCompletedBinding.bind(itemView)

        private lateinit var imageChalleng: ShapeableImageView
        private lateinit var contentImage: CardView
        private lateinit var nameChalleng: MaterialTextView
        private lateinit var authorNameChallenge: MaterialTextView
        private lateinit var authorAliasChallenge: MaterialTextView
        private lateinit var layoutComment: LinearLayout
        private lateinit var likeImg: LottieAnimationView
        private lateinit var itemOptions: ShapeableImageView

        fun bind(
            item: ChallengeCompleted,
            clickedItem: onClickThread?,
            mPreferences: MyPreferences
        ) {

            binding.apply {
                imageChalleng   = imgChallenge
                contentImage    = roundedImageView
                nameChalleng    = nameChallenge
                authorNameChallenge     = authorName
                authorAliasChallenge    = authorAlias
                likeImg                 = likeImageView
                layoutComment     = linearLayoutComentar
                itemOptions             = optionsChallenge

            }
            itemView.apply {

                var color = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorSecondaryLightColor))
                contentImage.backgroundTintList = color
                imageChalleng.setImageResource(R.drawable.ic_galeria)

                Glide
                    .with(context)
                    .load(item.url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_galeria)
                    .into(imageChalleng)

                imageChalleng.setOnClickListener {
                    it.preventDoubleClick()
                    onClickImage?.invoke(item, position)
                }

                nameChalleng.text = replaceFirstCharInSequenceToUppercase(item.name?:"")
                authorAliasChallenge.text = "\uD83C\uDFD6 Estudiante"
                println("Item author name: ${item.author_name}")
                authorNameChallenge.text = replaceFirstCharInSequenceToUppercase(item.author_name?:"")

                //Setup like
                setupLike(item, clickedItem, mPreferences)

                //Setup share
                binding.actionShare.setOnClickListener{
                    it.preventDoubleClick()
                    setupShare(item)
                }
                binding.textViewShare.setOnClickListener{
                    it.preventDoubleClick()
                    setupShare(item)
                }

                //SetupComment
                setupComment(item, clickedItem)

                //ReportPost
                reportPostOption(item, clickedItem)
            }

        }

        private fun reportPostOption(item: ChallengeCompleted, onClickThread: onClickThread?) {
            itemOptions.setOnClickListener{
                onClickThread?.clickedReportThread(item)
            }
        }

        private fun setupComment(item: ChallengeCompleted, onClickThread: onClickThread?) {
            layoutComment.setOnClickListener {
                onClickThread?.clickedComentThread(item)
            }
        }

        private fun setupShare(item:ChallengeCompleted) {
            Timber.d("Share -- AdapterChallengeCompleted")
            onClickCompartirListener?.invoke(item)
        }

        private fun setupLike(
            item: ChallengeCompleted,
            clickedItem: onClickThread?,
            mPreferences: MyPreferences
        ) {
            //First Time Like
            getFirsTimeLike(item,clickedItem, mPreferences)
        }

        private fun getFirsTimeLike(
            item: ChallengeCompleted,
            clickedItem: onClickThread?,
            mPreferences: MyPreferences
        ) {
            var status = false
            mActionProvider.getUserLike(mPreferences.email, item.id)?.get()
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
                onItemClickListener?.let {
                    it(item)
                }
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

    override fun getItemCount(): Int  = oldListChallenges.size
}