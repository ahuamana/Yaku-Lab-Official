package com.paparazziteam.yakulap.modulos.bienvenida.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ActivityWelcomeBinding
import com.paparazziteam.yakulap.helper.design.FadePageTransfomer
import com.paparazziteam.yakulap.modulos.bienvenida.adaters.MyPageAdapter
import com.paparazziteam.yakulap.modulos.bienvenida.viewmodels.ViewModelWelcome
import com.paparazziteam.yakulap.modulos.login.views.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity() {

    private lateinit var mViewpager: ViewPager
    private lateinit var mPageAdapter: MyPageAdapter
    private lateinit var dotsLayout: LinearLayout
    private lateinit var dots: MutableList<ImageView>


    private val _viewmodel: ViewModelWelcome by viewModels()
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityWelcomeBinding?>(this,R.layout.activity_welcome).apply {
          lifecycleOwner = this.lifecycleOwner
          model = _viewmodel
        }

        //Join views xml
        mViewpager   = binding.viewPagerIntro
        dotsLayout   = binding.layoutDots

        instanceAdapters()
        observers()
    }

    private fun observers() {
        _viewmodel.showFragments().observe(this){
            mPageAdapter.setFragments(it)
            addBottomDots(0)
        }
        _viewmodel.nextActivity.observe(this){
            if(it) startActivity(Intent(application, LoginActivity::class.java))
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
}