package com.paparazziteam.yakulap.modulos.puntaje.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ActivityPuntajeBinding
import com.paparazziteam.yakulap.helper.application.MyPreferences
import com.paparazziteam.yakulap.helper.beGone
import com.paparazziteam.yakulap.helper.beVisible
import com.paparazziteam.yakulap.helper.setColorToStatusBar
import com.paparazziteam.yakulap.modulos.puntaje.viewmodels.ViewModelPuntaje

class PuntajeActivity : AppCompatActivity() {

    private var _viewModel = ViewModelPuntaje.getInstance()
    private var preferences = MyPreferences()

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
            setTitleTextColor(getColor(R.color.white));
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
        }, 3000)

        puntajeTxt.text = preferences.points.toString()

    }


    private fun observers() {
        _viewModel.getMedal().observe(this){ medal->
                //close skeleton
            skeleton.stopShimmer()
            skeleton.hideShimmer()
            imgMedal.setImageDrawable(medal)
            skeleton.beGone()
            contenidoPrincipal.beVisible()


        }

    }

    override fun onDestroy() {
        super.onDestroy()
        ViewModelPuntaje.destroyInstance()
    }
}