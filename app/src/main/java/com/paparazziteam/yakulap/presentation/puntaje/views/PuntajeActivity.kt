package com.paparazziteam.yakulap.presentation.puntaje.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.paparazziteam.yakulab.binding.helper.application.MyPreferences
import com.paparazziteam.yakulab.binding.helper.beGone
import com.paparazziteam.yakulab.binding.helper.beVisible
import com.paparazziteam.yakulab.binding.utils.setColorToStatusBar
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ActivityPuntajeBinding
import com.paparazziteam.yakulap.presentation.puntaje.viewmodels.ViewModelPuntaje
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PuntajeActivity : AppCompatActivity() {

    private val _viewModel:ViewModelPuntaje by viewModels()

    @Inject
    lateinit var mPreferences: MyPreferences

    private lateinit var imgMedal:ShapeableImageView
    private lateinit var skeleton: ShimmerFrameLayout
    private lateinit var contenidoPrincipal: LinearLayout
    private lateinit var puntajeTxt: MaterialTextView
    private lateinit var myToolbar: Toolbar

    var binding:ActivityPuntajeBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPuntajeBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setColorToStatusBar(this)

        binding?.apply {
            imgMedal = imageViewMedalPoints
            skeleton = contentSkeletom
            contenidoPrincipal  = contentMain
            puntajeTxt  = textViewPoints
            myToolbar   = toolbarActivity
        }

        observers()
        initializeComponents()
        toolbar()
        //Star some features
    }

    private fun toolbar() {
        myToolbar.apply {
            navigationIcon = getDrawable(R.drawable.ic_arrow_back_white)
            setTitleTextColor(ContextCompat.getColor(context,R.color.white));
            title = "Puntaje"
            setNavigationOnClickListener {
                finish()
            }
        }
    }

    private fun initializeComponents() {
        Handler(Looper.getMainLooper()).postDelayed({
            //Do something after 2000mms
            _viewModel.showMedal()
        }, 1000)

        puntajeTxt.text = mPreferences.points.toString()

    }


    private fun observers() {
        _viewModel.getMedal().observe(this){ medal->
            skeleton.beGone()
            imgMedal.setImageDrawable(medal)
            contenidoPrincipal.beVisible()
        }

        _viewModel.loading.observe(this){
            if(it){
                skeleton.beVisible()
                skeleton.startShimmer()
            }else{
                skeleton.beGone()
                skeleton.stopShimmer()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}