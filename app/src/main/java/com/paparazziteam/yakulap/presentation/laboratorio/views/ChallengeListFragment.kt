package com.paparazziteam.yakulap.presentation.laboratorio.views

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.tabs.TabLayout
import com.paparazziteam.yakulap.databinding.ActivityChallengeParentBinding
import com.paparazziteam.yakulap.helper.beGone
import com.paparazziteam.yakulap.helper.beVisible
import com.paparazziteam.yakulap.helper.toJson
import com.paparazziteam.yakulap.presentation.laboratorio.adapters.ViewPagerAdapter
import com.paparazziteam.yakulap.presentation.laboratorio.fragments.ListChallengeFragment
import com.paparazziteam.yakulap.presentation.laboratorio.pojo.DataChallenge
import com.paparazziteam.yakulap.presentation.laboratorio.viewmodels.ViewModelLab
import dagger.hilt.android.AndroidEntryPoint
import viewBinding

@AndroidEntryPoint
class ChallengeListFragment : Fragment() {

    private val binding by viewBinding { ActivityChallengeParentBinding.bind(it) }
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
        arguments?.let {
            //data = it.getString(ARG_DATA)
            //type_category = it.getString(ARG_TYPE_CATEGORY)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val type = arguments?.getString("typeGroup")

        ui()
        otherComponents()
        setupActionBar()
        observers()
        viewModelLab.getData(type)
    }

    private fun otherComponents() {
        mTabLayout?.setupWithViewPager(mViewPager)
        mAdapterViewPager = ViewPagerAdapter(childFragmentManager)
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
        //get toolbar

        myToolbar?.apply {
            title = "Retos"
        }
    }

    private fun observers() {
        viewModelLab.observableListData.observe(viewLifecycleOwner){ itemList ->
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

        viewModelLab.observableCategorias.observe(viewLifecycleOwner){ list ->
            if(!list.isNullOrEmpty()){
                titles.clear()
                titles = list.distinct().toMutableList()
                println("Title size setted: ${titles.count()}")
            }else{
                println("Titles empty")
            }

        }

        viewModelLab.loading.observe(viewLifecycleOwner){
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