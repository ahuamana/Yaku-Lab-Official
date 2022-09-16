package com.paparazziteam.yakulap.modulos.laboratorio.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.FragmentChallengeBinding
import com.paparazziteam.yakulap.helper.*
import com.paparazziteam.yakulap.helper.application.MyPreferences
import com.paparazziteam.yakulap.helper.application.toast
import com.paparazziteam.yakulap.modulos.dashboard.pojo.MoldeChallengeCompleted
import com.paparazziteam.yakulap.modulos.dashboard.viewmodels.ViewModelDashboard
import com.paparazziteam.yakulap.modulos.laboratorio.pojo.DataCategory
import com.paparazziteam.yakulap.modulos.laboratorio.pojo.DataChallenge
import com.paparazziteam.yakulap.modulos.laboratorio.viewmodels.ViewModelChallenge
import com.paparazziteam.yakulap.modulos.laboratorio.viewmodels.ViewModelLab
import com.paparazziteam.yakulap.modulos.laboratorio.views.ChallengeActivity
import com.paparazziteam.yakulap.modulos.laboratorio.views.ResultCaptureImageActivity
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import java.util.*

class ChallengeFragment(private val clickCallback: View.OnClickListener) : Fragment() {

    var binding: FragmentChallengeBinding?= null

    val ARG_JSON        = "extra"
    val ARG_EXTRA_PATH  = "ARG_EXTRA_PATH"
    var data_extra = DataChallenge()
    var preferences = MyPreferences()

    var idChallengeDocument = ""

    var viewModel = ViewModelChallenge.getInstance()
    var viewModelDashboard = ViewModelDashboard.getInstance()
    var txtImageNotUploaded: MaterialTextView?= null

    private lateinit var imgChallengeChild: FloatingActionButton
    private lateinit var imgChallengeParent: CircleImageView
    private lateinit var imgChallengeToComplete: CircleImageView

    //Upload Image
    private lateinit var fabUploadImage: FloatingActionButton
    private lateinit var btnRegistrarPhoto: MaterialButton
    private var isChallengeCompleted = false

    var mDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        extras()
    }

    private fun extras() {
        if (activity?.intent?.extras != null) {
            var json_object =  activity?.intent?.getStringExtra(ARG_JSON)
            data_extra = fromJson(json_object?:"")
            Log.d(TAG_CHALLENGE_FRAGMENT, "Json received: $data_extra")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChallengeBinding.inflate(layoutInflater)
        val view = binding?.root

        binding?.apply {
            imgChallengeChild       = challengePhotoChild
            imgChallengeParent      = challengePhotoParent
            imgChallengeToComplete  = challengePhotoToComplete
            txtImageNotUploaded     = textViewImagenNosubida
            fabUploadImage          = fabSelectImage
            btnRegistrarPhoto       = btnRegistrar
        }

        setupChallengeData()
        setupCamera()
        getInfoChallengeCompleted()
        observers()
        otherComponents()
        return view
    }

    private fun otherComponents() {
        btnRegistrarPhoto.setOnClickListener {
            it.preventDoubleClick()
            it.isEnabled = false
            registerPhoto()
        }
    }

    private fun observers() {
        viewModelDashboard.errorUpload.observe(viewLifecycleOwner){
            if(!it.isNullOrEmpty()){
                mDialog?.dismiss()
                requireActivity().toast(it)
                btnRegistrarPhoto.isEnabled = true
            }
        }

        viewModelDashboard.completeUpload.observe(viewLifecycleOwner){
            if(it){
                Log.d("TAG","completeUpload running")
                mDialog?.dismiss()
                btnRegistrarPhoto.isEnabled = true
                requireActivity().toast("Se guardo la información correctamente")
                openResultActivity()
            }
        }

    }

    private fun openResultActivity() {
        var ramdon: Int
        data_extra.image_result?.let {
            ramdon = (it.indices).random()
            Log.d(TAG,"Ramdon number: $ramdon")
            Log.d(TAG,"Zise number: ${it.size}")
            requireActivity().startActivity(Intent(context,ResultCaptureImageActivity::class.java).apply {
                putExtra("title", data_extra.name)
                putExtra("description", data_extra?.text_result?.get(ramdon))
                putExtra("image", data_extra?.image_result?.get(ramdon))
                putExtra("pointsToGive", getPointsCompleted())
            })
        }
    }

    private fun getPointsCompleted():Int{
        return if(isChallengeCompleted) 0 else data_extra.pointsToGive?:0
    }

    private fun registerPhoto() {
        if(getImageActivity()!= null){
            prepareImageToUploadRemote()
        }else{
            requireActivity().toast("Selecciona una imagen para subir")
        }
    }

    private fun prepareImageToUploadRemote() {
        var completed  = MoldeChallengeCompleted(
            author_email = preferences.email_login,
            author_lastname =  preferences.lastName,
            author_name = preferences.firstName,
            timestamp = Date().time,
            tipo = data_extra.category,
            challenge_id = data_extra.id,
            name = data_extra.challenge_name
        )
        UploadedPhoto(completed)
    }

    private fun UploadedPhoto(completed: MoldeChallengeCompleted) {
        mDialog = ProgressDialog(requireContext())
        mDialog?.setTitle("Espere un momento")
        mDialog?.setMessage("Guardando Información")
        mDialog?.setCancelable(false)

        if(getImageFileActivity()!= null && getImageFileActivity()?.equals("") == false){
            mDialog?.show()
            viewModelDashboard.uploadPhotoRemote(completed,
                getImageFileActivity(),
                data_extra.pointsToGive?:0,
                isChallengeCompleted,
                idChallengeDocument)
        }
    }

    fun getInfoChallengeCompleted(){
        println("MySharedPreferences email: ${MyPreferences().email_login}")
        println("DataExtra id: ${data_extra.id}")
        viewModel.getChallengeInformation(data_extra.id){
                isCorrect:Boolean, challenge: MoldeChallengeCompleted?->
            if(isCorrect){
                Log.d(TAG_CHALLENGE_FRAGMENT,"result reto: $challenge")
                isChallengeCompleted = true
            }else{
                isChallengeCompleted = false
                Log.d(TAG_CHALLENGE_FRAGMENT,"not completed")
            }

            loadImageFromRemotoOrLocal(challenge)
        }
    }

    private fun getImageActivity():Uri{
        //Log.d("TAG","Variable ARGUMENTO PATH: ${image}")
        return (context as ChallengeActivity).getPathResultPhoto()
    }

    private fun getImageFileActivity(): File?{
        //Log.d("TAG","Variable ARGUMENTO PATH: ${image}")
        return (context as ChallengeActivity).getFileResultPhoto()
    }

    private fun loadImageFromRemotoOrLocal(challenge: MoldeChallengeCompleted?) {
        var image  = (context as ChallengeActivity).getPathResultPhoto()
        idChallengeDocument = challenge?.id?:""
        println("image activity: ${image}")
        if(!image.toString().isNullOrEmpty()){
            Glide.with(this)
                .load(image)
                .listener(object :RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {

                        return false
                    }
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d(TAG, "OnResourceReady")
                        //do something when picture already loaded
                        binding?.progressLoadImage?.visibility = View.GONE
                        return false
                    }

                })
                .error(R.drawable.ic_image_defect)
                .placeholder(R.drawable.ic_image_defect)
                .into(imgChallengeToComplete)
            txtImageNotUploaded?.beVisible()
        }else{
            loadImageNotUpload(challenge)
        }

    }

    fun loadImageNotUpload(challenge: MoldeChallengeCompleted?) {

        Glide.with(this)
            .load(challenge?.url)
            .listener(object :RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    txtImageNotUploaded?.beVisible()
                    return false
                }
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d(TAG, "OnResourceReady")
                    //do something when picture already loaded
                    txtImageNotUploaded?.beGone()

                    return false
                }

            })
            .error(R.drawable.circle)
            .placeholder(R.drawable.circle)
            .into(imgChallengeToComplete)

        binding?.progressLoadImage?.visibility = View.GONE
    }

    private fun setupCamera() {
        fabUploadImage.setOnClickListener(clickCallback)
    }


    override fun onDestroy() {
        super.onDestroy()
        ViewModelLab.destroyInstance()
        ViewModelDashboard.destroyInstance()
    }

    private fun setupChallengeData() {
        Glide.with(this)
            .load(data_extra.image_child)
            .error(R.drawable.ic_image_defect)
            .placeholder(R.drawable.ic_image_defect)
            .into(imgChallengeChild)

        Glide.with(this)
            .load(data_extra.image_parent)
            .error(R.drawable.ic_image_defect)
            .placeholder(R.drawable.ic_image_defect)
            .into(imgChallengeParent)
    }


    companion object {
        val TAG_CHALLENGE_FRAGMENT = javaClass.name
    }
}