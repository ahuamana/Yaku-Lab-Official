package com.paparazziteam.yakulap.presentation.bienvenida.views

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager.widget.ViewPager
import com.paparazziteam.yakulab.binding.helper.application.MyPreferences
import com.paparazziteam.yakulab.binding.utils.fromHtml
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ActivityWelcomeBinding
import com.paparazziteam.yakulap.databinding.CustomDisclosureDataCollectionBinding
import com.paparazziteam.yakulap.databinding.FragmentCustomDisclosureBinding
import com.paparazziteam.yakulap.presentation.bienvenida.adaters.MyPageAdapter
import com.paparazziteam.yakulap.presentation.bienvenida.viewmodels.ViewModelWelcome
import com.paparazziteam.yakulap.presentation.login.views.LoginActivity
import com.paparazziteam.yakulap.utils.design.FadePageTransfomer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity() {

    private lateinit var mViewpager: ViewPager
    private lateinit var mPageAdapter: MyPageAdapter
    private lateinit var dotsLayout: LinearLayout
    private lateinit var dots: MutableList<ImageView>


    private val viewModel: ViewModelWelcome by viewModels()
    private lateinit var binding : ActivityWelcomeBinding


    private val dialog by lazy {
       createCustomDialog()
    }

    @Inject
    lateinit var preferences: MyPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Join views xml
        mViewpager   = binding.viewPagerIntro
        dotsLayout   = binding.layoutDots

        instanceAdapters()
        setupButtons()
        observers()
    }

    private fun setupButtons() {
        binding.btnIniciarSesion.setOnClickListener {
            viewModel.onClickStart()
        }
    }


    private fun observers() {
        viewModel.showFragments().observe(this){
            mPageAdapter.setFragments(it)
            addBottomDots(0)
        }
        viewModel.nextActivity.observe(this){
            if(it) startActivity(Intent(application, LoginActivity::class.java))
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.needToShowDialogPreferences.collect{ needToShowDialog ->
                    if(needToShowDialog){
                        dialog.show()
                    }
                }
            }
        }
    }

    private fun createCustomDialog(): Dialog {
        val customBinding = FragmentCustomDisclosureBinding.inflate(layoutInflater)

        return Dialog(this).apply {
            setCancelable(false)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
            setContentView(customBinding.root)
        }.also { dialog->

            customBinding.tvDescription.text = getString(R.string.data_collection_disclosure_message).fromHtml()

            customBinding.btnAccept.setOnClickListener {
                viewModel.updateTutorialToNeverShowAgain()
                dialog.dismiss()
            }
        }
    }

    private fun instanceAdapters() {
        mPageAdapter = MyPageAdapter(supportFragmentManager)
        mViewpager.setPageTransformer(false, FadePageTransfomer())
        mViewpager.adapter = mPageAdapter

        mViewpager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                addBottomDots(position)
            }
        })
    }

    private fun addBottomDots(currentPage: Int) {
        dots = mutableListOf()
        dotsLayout.removeAllViews()
        val drawaInactive = ContextCompat.getDrawable(applicationContext, R.drawable.line)
        val drawaActive= ContextCompat.getDrawable(applicationContext, R.drawable.line)
        drawaInactive?.alpha = 85
        for (i: Int in 0 until mPageAdapter.count) {
            dots.add(ImageView(this))
            dots[i].setImageDrawable(drawaInactive)
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(8, 0, 8, 0)
            dotsLayout.addView(dots[i], params)
        }
        dots[currentPage].setImageDrawable(drawaActive)
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog.dismiss()

    }
}