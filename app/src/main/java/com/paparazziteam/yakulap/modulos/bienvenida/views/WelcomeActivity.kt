package com.paparazziteam.yakulap.modulos.bienvenida.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ActivityWelcomeBinding
import com.paparazziteam.yakulap.helper.design.FadePageTransfomer
import com.paparazziteam.yakulap.modulos.bienvenida.MyPageAdapter
import com.paparazziteam.yakulap.modulos.bienvenida.fragments.IntroFragment

class WelcomeActivity : AppCompatActivity() {

    private lateinit var mViewpager: ViewPager
    private lateinit var mPageAdapter: MyPageAdapter
    private lateinit var dotsLayout: LinearLayout
    private lateinit var dots: MutableList<ImageView>

    private lateinit var binding:ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Join views xml
        mViewpager   = binding.viewPagerIntro
        dotsLayout   = binding.layoutDots



        val fragments: List<Fragment> = getFragments()
        mPageAdapter = MyPageAdapter(supportFragmentManager, fragments)

        mViewpager.setPageTransformer(false, FadePageTransfomer())
        mViewpager.adapter = mPageAdapter

        addBottomDots(0)

        mViewpager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                addBottomDots(position)
            }
        })

    }

    private fun getFragments() : List<Fragment> {
        val fList = arrayListOf<Fragment>()

        fList.add(IntroFragment().newInstance(R.drawable.intro_primera,
            getString(R.string.intro_primera),
            getString(R.string.intro_desc_primera)))

        fList.add(IntroFragment().newInstance(R.drawable.intro_segunda,
            getString(R.string.intro_segunda),
            getString(R.string.intro_desc_segunda)))

        fList.add(IntroFragment().newInstance(R.drawable.intro_tercera,
            getString(R.string.intro_tercera),
            getString(R.string.intro_desc_tercera)))

        return fList
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