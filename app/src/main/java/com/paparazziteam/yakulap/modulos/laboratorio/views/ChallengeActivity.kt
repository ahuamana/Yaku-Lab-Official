package com.paparazziteam.yakulap.modulos.laboratorio.views

import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.browser.trusted.ScreenOrientation
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ActivityChallengeBinding
import com.paparazziteam.yakulap.helper.application.MyPreferences
import com.paparazziteam.yakulap.helper.beGone
import com.paparazziteam.yakulap.helper.design.toolbar.IToolbarActivity
import com.paparazziteam.yakulap.helper.design.toolbar.ToolbarActivity
import com.paparazziteam.yakulap.helper.fromJson
import com.paparazziteam.yakulap.helper.setColorToStatusBar
import com.paparazziteam.yakulap.modulos.dashboard.pojo.MoldeChallengeCompleted
import com.paparazziteam.yakulap.modulos.laboratorio.pojo.DataChallenge
import com.paparazziteam.yakulap.modulos.laboratorio.viewmodels.ViewModelChallenge
import com.paparazziteam.yakulap.modulos.laboratorio.viewmodels.ViewModelLab
import de.hdodenhof.circleimageview.CircleImageView
import io.ak1.pix.helpers.PixEventCallback
import io.ak1.pix.helpers.addPixToActivity
import io.ak1.pix.models.Flash
import io.ak1.pix.models.Mode
import io.ak1.pix.models.Options

class ChallengeActivity : ToolbarActivity() {

    var binding: ActivityChallengeBinding?= null
    val TAG = javaClass.name
    var ARG_JSON = "extra"
    var dataChallengeReceived = DataChallenge()
    var myToolbar: Toolbar?= null

    var viewModel = ViewModelChallenge.getInstance()
    var txtImageNotUploaded: MaterialTextView?= null

    private lateinit var imgChallengeChild: FloatingActionButton
    private lateinit var imgChallengeParent: CircleImageView
    private lateinit var imgChallengeToComplete: CircleImageView

    //Upload Image
    private lateinit var fabUploadImage:FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChallengeBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setColorToStatusBar(this)

        binding?.apply {
            imgChallengeChild   = challengePhotoChild
            imgChallengeParent  = challengePhotoParent
            imgChallengeToComplete = challengePhotoToComplete
            myToolbar           = include.toolbar
            txtImageNotUploaded = textViewImagenNosubida
            fabUploadImage      =fabSelectImage
        }
        extras()
        setupActionBar()
        setupChallengeData()
        setupCamera()

        println("MySharedPreferences email: ${MyPreferences().email_login}")
        viewModel.getChallengeInformation(dataChallengeReceived.id){
                isCorrect:Boolean, challenge: MoldeChallengeCompleted?->
            if(isCorrect){
                Log.d(TAG,"result reto: $challenge")
                Glide.with(this)
                    .load(challenge?.url)
                    .error(R.drawable.ic_image_defect)
                    .placeholder(R.drawable.ic_image_defect)
                    .into(imgChallengeToComplete)
                txtImageNotUploaded?.beGone()
            }else{
                Log.d(TAG,"is incorrect")
            }
        }
    }

    private fun setupCamera() {
        fabUploadImage.setOnClickListener {
            openCamera(100)
        }
    }

    private fun openCamera(requescode: Int) {
        //ImagePicker
       val mOptions = Options().apply {
           count = 1 //Number of images to restict selection count
           spanCount = 4 //Span count for gallery min 1 & max 5
           mode = Mode.Picture //Option to select only pictures or videos or both
           isFrontFacing = false
           flash = Flash.Auto
           requestedOrientation = ScreenOrientation.PORTRAIT
           path = "Pix/Camera"
       }

        addPixToActivity(R.id.contenedorChallenge, mOptions){
            when (it.status) {
                    PixEventCallback.Status.SUCCESS -> {

                    }//use results as it.data
                    PixEventCallback.Status.BACK_PRESSED -> {

                    } // back pressed called
                }
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        ViewModelLab.destroyInstance()
    }

    private fun setupChallengeData() {
        Glide.with(this)
            .load(dataChallengeReceived.image_child)
            .error(R.drawable.ic_image_defect)
            .placeholder(R.drawable.ic_image_defect)
            .into(imgChallengeChild)

        Glide.with(this)
            .load(dataChallengeReceived.image_parent)
            .error(R.drawable.ic_image_defect)
            .placeholder(R.drawable.ic_image_defect)
            .into(imgChallengeParent)
    }

    private fun setupActionBar() {
        myToolbar?.apply {
            setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryYaku))
            setNavigationOnClickListener { onBackPressed() }
            title = dataChallengeReceived.tittle
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun extras() {
        if (intent.extras != null) {
           var json_object =  intent.getStringExtra(ARG_JSON)
            dataChallengeReceived = fromJson(json_object?:"")
            Log.d(TAG, "Json received: $dataChallengeReceived")
        }
    }

}