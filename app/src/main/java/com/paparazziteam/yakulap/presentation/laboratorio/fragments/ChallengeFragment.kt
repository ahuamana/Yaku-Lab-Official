package com.paparazziteam.yakulap.presentation.laboratorio.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.FragmentChallengeBinding
import com.paparazziteam.yakulab.binding.Constants.EXTRA_CHALLENGE
import com.paparazziteam.yakulab.binding.helper.application.MyPreferences
import com.paparazziteam.yakulab.binding.helper.beGone
import com.paparazziteam.yakulab.binding.helper.beVisible
import com.paparazziteam.yakulab.binding.helper.fromJson
import com.paparazziteam.yakulab.binding.helper.others.PermissionManager
import com.paparazziteam.yakulab.binding.helper.preventDoubleClick
import com.paparazziteam.yakulab.binding.helper.toast
import com.yakulab.domain.dashboard.ChallengeCompleted
import com.paparazziteam.yakulap.presentation.laboratorio.fragments.bottomsheets.BottomSheetDialogOptionsCameraFragment
import com.paparazziteam.yakulap.presentation.laboratorio.fragments.bottomsheets.OnOptionSelectedSourcePicker
import com.yakulab.domain.laboratory.DataChallenge
import com.yakulab.domain.laboratory.TypeChallenge
import com.paparazziteam.yakulap.presentation.laboratorio.viewmodels.ViewModelChallenge
import com.paparazziteam.yakulap.presentation.laboratorio.viewmodels.ViewModelChallengeFragment
import com.paparazziteam.yakulap.navigation.NavigationRootImpl
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.glide.transformations.BlurTransformation
import timber.log.Timber
import viewBinding
import java.io.File
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ChallengeFragment : Fragment(), OnOptionSelectedSourcePicker {

    private val viewModelChallengeFragment: ViewModelChallengeFragment by viewModels()

    private val binding by viewBinding { FragmentChallengeBinding.bind(it) }

    private var dataExtra = DataChallenge()

    @Inject
    lateinit var mPreferences : MyPreferences

    @Inject
    lateinit var mNavigationRoot: NavigationRootImpl

    var idChallengeDocument = ""

    private val viewModel:ViewModelChallenge by viewModels()
    var txtImageNotUploaded: MaterialTextView?= null

    private lateinit var imgChallengeToComplete: ShapeableImageView

    //Upload Image
    private lateinit var fabUploadImage: FloatingActionButton
    private lateinit var btnRegistrarPhoto: MaterialButton
    private var isChallengeCompleted = false

    var mDialog: ProgressDialog? = null

    private val permissionManager by lazy { PermissionManager(requireActivity()) }

    private var mProfileUri: Uri? = null

    private val startForChallengeImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data
                Timber.d("File Uri: $fileUri")

                mProfileUri = fileUri
                binding.challengePhotoToComplete.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        extras()
    }

    private fun extras() {
        Timber.d("Extras")
        Timber.d(" ${arguments != null}")

        val extras =  arguments?.getString(EXTRA_CHALLENGE)
        dataExtra = fromJson(extras?:"")
        Log.d(TAG_CHALLENGE_FRAGMENT, "Json received: $dataExtra")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentChallengeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            imgChallengeToComplete  = challengePhotoToComplete
            txtImageNotUploaded     = textViewImagenNosubida
            fabUploadImage          = fabSelectImage
            btnRegistrarPhoto       = btnRegistrar
        }

        setupChallengeData()
        getInfoChallengeCompleted()
        observers()
        setupButtons()
    }

    private fun setupButtons() {
        binding.btnRegistrar.setOnClickListener { view->
            view.preventDoubleClick()
            view.isEnabled = false
            registerPhoto(
                onSuccess = {
                    view.isEnabled = true
                },
                onError = {
                    view.isEnabled = true
                }
            )
        }
        binding.fabSelectImage.setOnClickListener { view->
            view.preventDoubleClick()
            view.isEnabled = false
            openOptionsPickerCamera()
        }
    }

    private fun openOptionsPickerCamera(){
        val cameraPickerBottomSheet = BottomSheetDialogOptionsCameraFragment(this)
        cameraPickerBottomSheet.show(childFragmentManager, "BottomSheetDialogOptionsCameraFragment")
    }

    private fun observers() {
        viewModelChallengeFragment.errorUpload.observe(viewLifecycleOwner){
            if(!it.isNullOrEmpty()){
                mDialog?.dismiss()
                requireActivity().toast(it)
                btnRegistrarPhoto.isEnabled = true
            }
        }

        viewModelChallengeFragment.completeUpload.observe(viewLifecycleOwner){
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

            var description = ""
            var image = ""
            //validate if exist position in array
            when(dataExtra.type){
                TypeChallenge.NEARBY_SPECIES -> {
                    description = viewModelChallengeFragment.getExtraInfoChallengeNearbySpecies()
                    image = dataExtra.image_parent?:""
                }
                else -> {
                    description = dataExtra.text_result?.getOrNull(ramdon)?: ""
                    image = dataExtra.image_result?.getOrNull(ramdon)?:""
                }
            }


            val bundle = bundleOf(
                "title" to dataExtra.name,
                "description" to description,
                "image" to image,
                "pointsToGive" to getPointsCompleted()
            )

            mNavigationRoot.navigateToChallengeComplete(bundle)
        }
    }

    private fun getPointsCompleted():Int{
        return if(isChallengeCompleted) 0 else dataExtra.pointsToGive?:0
    }

    private fun registerPhoto(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if(mProfileUri == null){
            onError("Selecciona una imagen para subir")
            return
        }

        val completed  = ChallengeCompleted(
            author_email = mPreferences.email,
            author_lastname =  mPreferences.lastName,
            author_name = mPreferences.firstName,
            timestamp = Date().time,
            tipo = dataExtra.category,
            challenge_id = dataExtra.id,
            name = dataExtra.challenge_name
        )
        uploadedPhoto(completed)
    }

    private fun uploadedPhoto(completed: ChallengeCompleted) {
        mDialog = ProgressDialog(requireContext())
        mDialog?.setTitle("Espere un momento")
        mDialog?.setMessage("Guardando Información")
        mDialog?.setCancelable(false)
        mDialog?.show()

        val fileImage = File(mProfileUri?.path?:"")
        Timber.d("File Uri: $fileImage")

        viewModelChallengeFragment.uploadPhotoRemote(completed,
            fileImage = fileImage,
            pointsToGive = dataExtra.pointsToGive?:0,
            isCompleted = isChallengeCompleted,
            idChallengeDocument = idChallengeDocument)
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

    private fun loadImageFromRemotoOrLocal(challenge: ChallengeCompleted?) {
        val image  = ""
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
                        Timber.d("OnResourceReady")
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
                    Timber.d("OnResourceReady")
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


    private fun openCamera() {
        when {
            permissionManager.hasCameraPermission() -> {
                ImagePicker.with(this)
                    //.crop()	    			//Crop image(Optional), Check Customization for more option
                    .cameraOnly()
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(620, 620)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .createIntent {
                       startForChallengeImageResult.launch(it)
                    }
            }
            else -> {
                permissionManager.requestCameraPermission()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        viewModelChallengeFragment.errorUpload.removeObservers(this)
        viewModelChallengeFragment.completeUpload.removeObservers(this)
    }

    private fun setupChallengeData() {

        val urlChild = dataExtra.image_child?:""


        val viewImageWhereToLoad = if(urlChild.contains("png", true)){
            binding.containerChallengeChild.beGone()
            binding.challengePhotoChildPng.beVisible()
            binding.challengePhotoChildPng
        } else binding.challengePhotoChild


        Glide.with(this)
            .load(dataExtra.image_child)
            .into(viewImageWhereToLoad)

        Glide.with(this)
            .load(dataExtra.image_parent)
            //apply blur
            .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
            .into(binding.challengePhotoParent)
    }


    companion object {
        val TAG_CHALLENGE_FRAGMENT = javaClass.name
    }

    override fun onOptionSelectedCamera() {
        openCamera()
    }

    override fun onOptionSelectedGallery() {
        //show snackbar with message "Coming soon" in spanish "Proximamente"
        Snackbar.make(requireView(),"Proximamente", Snackbar.LENGTH_SHORT).show()
    }
}