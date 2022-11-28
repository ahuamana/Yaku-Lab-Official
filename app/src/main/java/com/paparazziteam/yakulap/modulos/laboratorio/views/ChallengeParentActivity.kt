package com.paparazziteam.yakulap.modulos.laboratorio.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.tabs.TabLayout
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.ActivityChallengeParentBinding
import com.paparazziteam.yakulap.helper.Constants.CATEGORY_DOMESTIC_ANIMALS
import com.paparazziteam.yakulap.helper.Constants.CATEGORY_INSECTS
import com.paparazziteam.yakulap.helper.Constants.CATEGORY_OTHERS_ANIMALS
import com.paparazziteam.yakulap.helper.beGone
import com.paparazziteam.yakulap.helper.beVisible
import com.paparazziteam.yakulap.helper.setColorToStatusBar
import com.paparazziteam.yakulap.helper.toJson
import com.paparazziteam.yakulap.modulos.laboratorio.adapters.ViewPagerAdapter
import com.paparazziteam.yakulap.modulos.laboratorio.fragments.ListChallengeFragment
import com.paparazziteam.yakulap.modulos.laboratorio.pojo.DataChallenge
import com.paparazziteam.yakulap.modulos.laboratorio.pojo.TypeCategoria
import com.paparazziteam.yakulap.modulos.laboratorio.viewmodels.ViewModelLab
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChallengeParentActivity : AppCompatActivity() {

    var binding:ActivityChallengeParentBinding?= null
    var viewModelLab = ViewModelLab.getInstance()
    val TAG = javaClass.name

    //Components
    var mTabLayout:TabLayout?= null
    var mViewPager:ViewPager?= null
    var myToolbar:Toolbar?= null

    var titles = mutableListOf<String>()
    var mAdapterViewPager:ViewPagerAdapter?= null

    //Ui Shimmer Loading
    var mShimmerLayout: ShimmerFrameLayout?= null

    private var listCategoriOne: MutableList<DataChallenge> = mutableListOf()
    private var listCategoriTwo: MutableList<DataChallenge> = mutableListOf()
    private var listCategoriThree: MutableList<DataChallenge> = mutableListOf()
    private var listCategoriFour: MutableList<DataChallenge> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChallengeParentBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setColorToStatusBar(this)

        var type = intent.getStringExtra("typeGroup")


        ui()
        otherComponents()
        setupActionBar()
        observers()
        viewModelLab.getData(type)
    }

    private fun otherComponents() {
        mTabLayout?.setupWithViewPager(mViewPager)
        mAdapterViewPager = ViewPagerAdapter(supportFragmentManager)
    }

    private fun ui() {
        binding?.apply {
            mTabLayout  = tabLayout
            mViewPager  = viewPager
            myToolbar   = include.toolbar
            mShimmerLayout = shimmerLoading
        }
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
        viewModelLab.observableListData.observe(this){ itemList ->
            if(!itemList.isNullOrEmpty()){
                itemList.forEach {
                    when (it.Challenge?.get(0)?.category){
                        titles[0] ->{
                            listCategoriOne.add(it.Challenge!![0])
                        }
                        titles[1] -> {
                            listCategoriTwo.add(it.Challenge!![0])
                        }
                        else -> {
                            listCategoriThree.add(it.Challenge!![0])
                        }
                    }
                }
                setupViewPager()
            }else{
                println("Empty List items")
            }
        }

        viewModelLab.observableCategorias.observe(this){ list ->
            if(!list.isNullOrEmpty()){
                titles.clear()
                titles = list.distinct().toMutableList()
                println("Title size setted: ${titles.count()}")
            }else{
                println("Titles empty")
            }

        }

        viewModelLab.loading.observe(this){
            if(it){
                mShimmerLayout?.apply {
                    beVisible()
                    startShimmer()
                }
            }else{
                mShimmerLayout?.apply {
                    beGone()
                    stopShimmer()
                }
            }
        }
    }

    private fun setupViewPager() {
        println("Title size: ${titles.size}")
        when(titles.count()){
            1->{
                mAdapterViewPager?.addFragment(ListChallengeFragment.newInstance(toJson(listCategoriOne),titles[0]),titles[0])
            }
            2->{
                mAdapterViewPager?.addFragment(ListChallengeFragment.newInstance(toJson(listCategoriOne),titles[0]),titles[0])
                mAdapterViewPager?.addFragment(ListChallengeFragment.newInstance(toJson(listCategoriTwo),titles[1]),titles[1])
            }

            else->{
                mAdapterViewPager?.addFragment(ListChallengeFragment.newInstance(toJson(listCategoriOne),titles[0]),titles[0])
                mAdapterViewPager?.addFragment(ListChallengeFragment.newInstance(toJson(listCategoriTwo),titles[1]),titles[1])
                mAdapterViewPager?.addFragment(ListChallengeFragment.newInstance(toJson(listCategoriThree),titles[2]),titles[2])
            }
        }

        mViewPager?.adapter = mAdapterViewPager
    }

    override fun onDestroy() {
        super.onDestroy()
        ViewModelLab.destroyInstance()
    }
}