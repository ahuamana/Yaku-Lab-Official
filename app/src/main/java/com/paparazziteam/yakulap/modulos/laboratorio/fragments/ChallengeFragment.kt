package com.paparazziteam.yakulap.modulos.laboratorio.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.FragmentChallengeBinding
import com.paparazziteam.yakulap.helper.application.MyPreferences
import com.paparazziteam.yakulap.helper.beGone
import com.paparazziteam.yakulap.helper.fromJson
import com.paparazziteam.yakulap.modulos.dashboard.pojo.MoldeChallengeCompleted
import com.paparazziteam.yakulap.modulos.laboratorio.pojo.DataChallenge
import com.paparazziteam.yakulap.modulos.laboratorio.viewmodels.ViewModelChallenge
import com.paparazziteam.yakulap.modulos.laboratorio.viewmodels.ViewModelLab
import com.paparazziteam.yakulap.modulos.laboratorio.views.ChallengeActivity
import de.hdodenhof.circleimageview.CircleImageView
import io.ak1.pix.helpers.PixEventCallback
import io.ak1.pix.helpers.addPixToActivity
import io.ak1.pix.helpers.pixFragment
import io.ak1.pix.models.Flash
import io.ak1.pix.models.Mode
import io.ak1.pix.models.Options

class ChallengeFragment : Fragment() {

    var binding: FragmentChallengeBinding?= null

    var ARG_JSON = "extra"
    var dataChallengeReceived = DataChallenge()
    var myToolbar: Toolbar?= null

    var viewModel = ViewModelChallenge.getInstance()
    var txtImageNotUploaded: MaterialTextView?= null

    private lateinit var imgChallengeChild: FloatingActionButton
    private lateinit var imgChallengeParent: CircleImageView
    private lateinit var imgChallengeToComplete: CircleImageView

    //Upload Image
    private lateinit var fabUploadImage: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        extras()
    }

    private fun extras() {
        if (activity?.intent?.extras != null) {
            var json_object =  activity?.intent?.getStringExtra(ARG_JSON)
            dataChallengeReceived = fromJson(json_object?:"")
            Log.d(TAG_CHALLENGE_FRAGMENT, "Json received: $dataChallengeReceived")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChallengeBinding.inflate(layoutInflater)
        val view = binding?.root
        //

        binding?.apply {
            imgChallengeChild   = challengePhotoChild
            imgChallengeParent  = challengePhotoParent
            imgChallengeToComplete = challengePhotoToComplete
            txtImageNotUploaded = textViewImagenNosubida
            fabUploadImage      =fabSelectImage
        }

        setupChallengeData()
        setupCamera()

        println("MySharedPreferences email: ${MyPreferences().email_login}")
        viewModel.getChallengeInformation(dataChallengeReceived.id){
                isCorrect:Boolean, challenge: MoldeChallengeCompleted?->
            if(isCorrect){
                Log.d(TAG_CHALLENGE_FRAGMENT,"result reto: $challenge")
                Glide.with(this)
                    .load(challenge?.url)
                    .error(R.drawable.ic_image_defect)
                    .placeholder(R.drawable.ic_image_defect)
                    .into(imgChallengeToComplete)
                txtImageNotUploaded?.beGone()
            }else{
                Log.d(TAG_CHALLENGE_FRAGMENT,"is incorrect")
            }
        }



        return view
    }

    private fun setupCamera() {
        fabUploadImage.setOnClickListener {
            openCameraActivity()
        }
    }

    private fun openCameraActivity() {
        (activity as ChallengeActivity).openCameraActivity()
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

    companion object {
        val TAG_CHALLENGE_FRAGMENT = javaClass.name

        @JvmStatic
        fun newInstance() =
            ChallengeFragment().apply {}
    }
}