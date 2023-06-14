package com.paparazziteam.yakulap.presentation.laboratorio.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import com.paparazziteam.yakulap.helper.others.PermissionManager
import com.paparazziteam.yakulap.presentation.dashboard.pojo.ChallengeCompleted
import com.paparazziteam.yakulap.presentation.dashboard.viewmodels.ViewModelDashboard
import com.paparazziteam.yakulap.presentation.laboratorio.pojo.DataChallenge
import com.paparazziteam.yakulap.presentation.laboratorio.viewmodels.ViewModelChallenge
import com.paparazziteam.yakulap.presentation.laboratorio.viewmodels.ViewModelLab
import com.paparazziteam.yakulap.presentation.laboratorio.views.ChallengeActivity
import com.paparazziteam.yakulap.presentation.laboratorio.views.ResultCaptureImageActivity
import com.paparazziteam.yakulap.presentation.navigation.NavigationRootImpl
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import io.ak1.pix.helpers.PixBus
import io.ak1.pix.helpers.PixEventCallback
import io.ak1.pix.utility.ARG_PARAM_PIX
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ChallengeFragment : Fragment() {

    private val viewModelDashboard:ViewModelDashboard by viewModels()

    var binding: FragmentChallengeBinding?= null

    private val argJson        = "extra"
    private var dataExtra = DataChallenge()

    @Inject
    lateinit var mPreferences :MyPreferences

    @Inject
    lateinit var navigationRoot : NavigationRootImpl

    var idChallengeDocument = ""

    private val viewModel:ViewModelChallenge by viewModels()
    var txtImageNotUploaded: MaterialTextView?= null

    private lateinit var imgChallengeChild: FloatingActionButton
    private lateinit var imgChallengeParent: CircleImageView
    private lateinit var imgChallengeToComplete: CircleImageView

    //Upload Image
    private lateinit var fabUploadImage: FloatingActionButton
    private lateinit var btnRegistrarPhoto: MaterialButton
    private var isChallengeCompleted = false

    var mDialog: ProgressDialog? = null

    private val permissionManager by lazy { PermissionManager(requireActivity()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        extras()
    }

    private fun extras() {
        if (requireActivity().intent?.extras != null) {
            val json_object =  activity?.intent?.getStringExtra(argJson)
            dataExtra = fromJson(json_object?:"")
            Log.d(TAG_CHALLENGE_FRAGMENT, "Json received: $dataExtra")
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handlePixImageResult()
    }

    private fun handlePixImageResult() {
        PixBus.results(coroutineScope = CoroutineScope(Dispatchers.Main)) {
            when (it.status) {
                PixEventCallback.Status.SUCCESS -> {
                    //TODO: handle success result Pix image
                    Log.d("TAG","PixEventCallback.Status.SUCCESS")
                }
                PixEventCallback.Status.BACK_PRESSED -> {
                    requireActivity().onBackPressed()
                }
            }
        }
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
        dataExtra.image_result?.let {
            ramdon = (it.indices).random()
            Log.d(TAG,"Ramdon number: $ramdon")
            Log.d(TAG,"Zise number: ${it.size}")
            requireActivity().startActivity(Intent(context,ResultCaptureImageActivity::class.java).apply {
                putExtra("title", dataExtra.name)
                putExtra("description", dataExtra?.text_result?.get(ramdon))
                putExtra("image", dataExtra?.image_result?.get(ramdon))
                putExtra("pointsToGive", getPointsCompleted())
            })
        }
    }

    private fun getPointsCompleted():Int{
        return if(isChallengeCompleted) 0 else dataExtra.pointsToGive?:0
    }

    private fun registerPhoto() {
        if(getImageActivity()!= null){
            prepareImageToUploadRemote()
        }else{
            requireActivity().toast("Selecciona una imagen para subir")
        }
    }

    private fun prepareImageToUploadRemote() {
        var completed  = ChallengeCompleted(
            author_email = mPreferences.email,
            author_lastname =  mPreferences.lastName,
            author_name = mPreferences.firstName,
            timestamp = Date().time,
            tipo = dataExtra.category,
            challenge_id = dataExtra.id,
            name = dataExtra.challenge_name
        )
        UploadedPhoto(completed)
    }

    private fun UploadedPhoto(completed: ChallengeCompleted) {
        mDialog = ProgressDialog(requireContext())
        mDialog?.setTitle("Espere un momento")
        mDialog?.setMessage("Guardando Información")
        mDialog?.setCancelable(false)

        if(getImageFileActivity()!= null && getImageFileActivity()?.equals("") == false){
            mDialog?.show()
            viewModelDashboard.uploadPhotoRemote(completed,
                getImageFileActivity(),
                dataExtra.pointsToGive?:0,
                isChallengeCompleted,
                idChallengeDocument)
        }
    }

    fun getInfoChallengeCompleted(){
        println("MySharedPreferences email: ${mPreferences.email}")
        println("DataExtra id: ${dataExtra.id}")
        viewModel.getChallengeInformation(dataExtra.id){
                isCorrect:Boolean, challenge: ChallengeCompleted?->
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
        return (requireActivity() as ChallengeActivity).getPathResultPhoto()
    }

    private fun getImageFileActivity(): File?{
        //Log.d("TAG","Variable ARGUMENTO PATH: ${image}")
        return (requireActivity() as ChallengeActivity).getFileResultPhoto()
    }

    private fun loadImageFromRemotoOrLocal(challenge: ChallengeCompleted?) {
        //var image  = (requireActivity() as ChallengeActivity).getPathResultPhoto()
        var image  = ""
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
                        binding?.progressLoadImage?.beGone()
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

    fun loadImageNotUpload(challenge: ChallengeCompleted?) {

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
        fabUploadImage.setOnClickListener {
            openCamera()
        }
    }

    private fun openCamera() {
        when {
            permissionManager.hasCameraPermission() -> {
                navigationRoot.navigateToCameraWhatsapp()
            }
            else -> {
                permissionManager.requestCameraPermission()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        ViewModelLab.destroyInstance()
        viewModelDashboard.errorUpload.removeObservers(this)
        viewModelDashboard.completeUpload.removeObservers(this)
    }

    private fun setupChallengeData() {
        Glide.with(this)
            .load(dataExtra.image_child)
            .error(R.drawable.ic_image_defect)
            .placeholder(R.drawable.ic_image_defect)
            .into(imgChallengeChild)

        Glide.with(this)
            .load(dataExtra.image_parent)
            .error(R.drawable.ic_image_defect)
            .placeholder(R.drawable.ic_image_defect)
            .into(imgChallengeParent)
    }


    companion object {
        val TAG_CHALLENGE_FRAGMENT = javaClass.name
    }
}