package com.paparazziteam.yakulap.modulos.laboratorio.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ActivityChallengeParentBinding
import com.paparazziteam.yakulap.helper.Constants.CATEGORY_DOMESTIC_ANIMALS
import com.paparazziteam.yakulap.helper.Constants.CATEGORY_INSECTS
import com.paparazziteam.yakulap.helper.Constants.CATEGORY_OTHERS_ANIMALS
import com.paparazziteam.yakulap.helper.setColorToStatusBar
import com.paparazziteam.yakulap.helper.toJson
import com.paparazziteam.yakulap.modulos.laboratorio.adapters.ViewPagerAdapter
import com.paparazziteam.yakulap.modulos.laboratorio.fragments.ListChallengeFragment
import com.paparazziteam.yakulap.modulos.laboratorio.pojo.DataChallenge
import com.paparazziteam.yakulap.modulos.laboratorio.pojo.TypeCategoria
import com.paparazziteam.yakulap.modulos.laboratorio.viewmodels.ViewModelLab

class ChallengeParentActivity : AppCompatActivity() {

    var binding:ActivityChallengeParentBinding?= null
    var viewModelLab = ViewModelLab.getInstance()
    val TAG = javaClass.name

    //Components
    var mTabLayout:TabLayout?= null
    var mViewPager:ViewPager?= null
    var myToolbar:Toolbar?= null

    var titles = mutableListOf<String>()

    private var listInsectos: MutableList<DataChallenge>? = mutableListOf()
    private var listAnimalesDomesticos: MutableList<DataChallenge>? = mutableListOf()
    private var listAnimalesOthers: MutableList<DataChallenge>? = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChallengeParentBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setColorToStatusBar(this)

        binding?.apply {
            mTabLayout  = tabLayout
            mViewPager  = viewPager
            myToolbar   = include.toolbar
        }

        setupActionBar()
        observers()
        viewModelLab.getData()
    }

    private fun setupActionBar() {
        myToolbar?.apply {
            setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryYaku))
            setNavigationOnClickListener { onBackPressed() }
            title = "Retos"
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun observers() {
        viewModelLab.observableListData.observe(this){
            it.listAnimalsDomestic?.let { data -> listAnimalesDomesticos?.addAll(data) }
            it.listInsectos?.let { data -> listInsectos?.addAll(data) }
            it.listAnimalsOther?.let { data -> listAnimalesOthers?.addAll(data) }
            setupViewPager()
        }
    }

    private fun setupViewPager() {
        titles.add(CATEGORY_DOMESTIC_ANIMALS)
        titles.add(CATEGORY_INSECTS)
        titles.add(CATEGORY_OTHERS_ANIMALS)
        mTabLayout?.setupWithViewPager(mViewPager)

        var mAdapterViewPager = ViewPagerAdapter(supportFragmentManager)

        mAdapterViewPager.addFragment(ListChallengeFragment.newInstance(toJson(listAnimalesDomesticos),TypeCategoria.AnimalesDomesticos),titles[0])
        mAdapterViewPager.addFragment(ListChallengeFragment.newInstance(toJson(listInsectos),TypeCategoria.Insectos),titles[1])
        mAdapterViewPager.addFragment(ListChallengeFragment.newInstance(toJson(listAnimalesOthers),TypeCategoria.AnimalesOthers),titles[2])

        mViewPager?.adapter = mAdapterViewPager
    }

    override fun onDestroy() {
        super.onDestroy()
        ViewModelLab.destroyInstance()
    }
}