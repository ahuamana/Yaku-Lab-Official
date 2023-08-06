package com.paparazziteam.yakulap.presentation.laboratorio.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.tabs.TabLayout
import com.paparazziteam.yakulab.binding.helper.beGone
import com.paparazziteam.yakulab.binding.helper.beVisible
import com.paparazziteam.yakulab.binding.helper.toJson
import com.paparazziteam.yakulap.databinding.ActivityChallengeParentBinding
import com.paparazziteam.yakulap.presentation.laboratorio.adapters.ViewPagerAdapter
import com.paparazziteam.yakulap.presentation.laboratorio.fragments.ListChallengeFragment
import com.yakulab.domain.laboratory.DataChallenge
import com.paparazziteam.yakulap.presentation.laboratorio.viewmodels.ViewModelLab
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import viewBinding

@AndroidEntryPoint
class ChallengeListFragment : Fragment() {

    private val binding by viewBinding { ActivityChallengeParentBinding.bind(it) }
    private val viewModelLab  by viewModels<ViewModelLab>()
    val TAG = javaClass.name

    //Components
    var mTabLayout:TabLayout?= null
    var mViewPager:ViewPager?= null

    var titles = mutableListOf<String>()
    var mAdapterViewPager:ViewPagerAdapter?= null

    //Ui Shimmer Loading
    var mShimmerLayout: ShimmerFrameLayout?= null

    private var listCategoriOne: MutableList<DataChallenge> = mutableListOf()
    private var listCategoriTwo: MutableList<DataChallenge> = mutableListOf()
    private var listCategoriThree: MutableList<DataChallenge> = mutableListOf()
    private var listCategoriFour: MutableList<DataChallenge> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ActivityChallengeParentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ui()
        otherComponents()
        observers()
    }

    private fun otherComponents() {
        mTabLayout?.setupWithViewPager(mViewPager)
        mAdapterViewPager = ViewPagerAdapter(childFragmentManager)
    }

    private fun ui() {
        binding.apply {
            mTabLayout  = tabLayout
            mViewPager  = viewPager
            mShimmerLayout = shimmerLoading
        }
    }

    private fun observers() {
        lifecycleScope.launch {
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
}