package com.paparazziteam.yakulap.presentation.puntaje.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class PuntajeActivity : Fragment() {

    private val _viewModel:ViewModelPuntaje by viewModels()

    @Inject
    lateinit var mPreferences: MyPreferences

    val binding:ActivityPuntajeBinding by viewBinding { ActivityPuntajeBinding.bind(it) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ActivityPuntajeBinding.inflate(inflater, container, false)
        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observers()
        initializeComponents()
        //Star some features
    }

    private fun initializeComponents() {
        Handler(Looper.getMainLooper()).postDelayed({
            //Do something after 2000mms
            _viewModel.showMedal(resources = resources)
        }, 1000)
        binding.textViewPoints.text = mPreferences.points.toString()
    }


    private fun observers() {
        _viewModel.getMedal().observe(viewLifecycleOwner){ medal->
            binding.contentSkeletom.beGone()
            binding.imageViewMedalPoints.setImageDrawable(medal)
            binding.contentMain.beVisible()
        }

        _viewModel.loading.observe(viewLifecycleOwner){
            if(it){
                binding.contentSkeletom.apply {
                    beVisible()
                    startShimmer()
                }
            }else{
                binding.contentSkeletom.apply {
                    beGone()
                    stopShimmer()
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}