package com.paparazziteam.yakulap.modulos.dashboard.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.paparazziteam.yakulap.databinding.FragmentHomeBinding
import com.paparazziteam.yakulap.helper.*
import com.paparazziteam.yakulap.helper.application.MyPreferences
import com.paparazziteam.yakulap.modulos.dashboard.adapters.AdapterChallengeCompleted
import com.paparazziteam.yakulap.modulos.dashboard.interfaces.onClickThread
import com.paparazziteam.yakulap.modulos.dashboard.pojo.ChallengeCompleted
import com.paparazziteam.yakulap.modulos.dashboard.pojo.TypeGroup
import com.paparazziteam.yakulap.modulos.dashboard.viewmodels.ViewModelDashboard
import com.paparazziteam.yakulap.modulos.laboratorio.views.ChallengeParentActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), onClickThread {

    @Inject
    lateinit var mPreferences:MyPreferences

    private var binding: FragmentHomeBinding by autoCleared()
    private val viewModel:ViewModelDashboard by activityViewModels()

    private var clickedItemCompleted:onClickThread?=null

    //Laboratorio
    var mLinearLayoutManager: LinearLayoutManager? = null
    private var mRecyclerChallengesCompleted: RecyclerView? = null

    //UI shimmer
    private var shimmerSkeleton: ShimmerFrameLayout? = null
    //UI body
    private var bodyLayout: ConstraintLayout? = null

    //Content Animals
    var mLinearLayoutAnimals: LinearLayout? = null
    var mCardFruits: CardView? = null
    var mCardPlants: CardView? = null

    //Adapter
    var mAdapter: AdapterChallengeCompleted? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        ui()
        setUpRecycler()
        observerComponents()
        otherComponents()
        getChallengesCompleted()

        return binding.root
    }

    private fun ui() {
        binding.apply {
            mRecyclerChallengesCompleted = recycler
            mLinearLayoutAnimals = linearLayoutAnimales
            mCardFruits         = cardFruits
            mCardPlants         = cardPlants
            shimmerSkeleton              = shimmerLoading
            bodyLayout                   = containerLayoutBodyChallenges
        }
        clickedItemCompleted = this
    }

    private fun setUpRecycler() {
        mLinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        mAdapter = AdapterChallengeCompleted(clickedItemCompleted, mPreferences)
        mRecyclerChallengesCompleted?.apply {
            layoutManager = mLinearLayoutManager
            adapter =  mAdapter
        }

        mAdapter?.onClickUpdateLikeListener {
            viewModel.updateLikeStatusFirebase(it)
        }
    }

    private fun otherComponents() {
        mLinearLayoutAnimals?.setOnClickListener {
            it.preventDoubleClick()
            startActivity(Intent(context, ChallengeParentActivity::class.java).apply {
                putExtra("typeGroup",TypeGroup.ANIMALS.value)
            })
        }

        mCardFruits?.setOnClickListener {
            it.preventDoubleClick()
            startActivity(Intent(context, ChallengeParentActivity::class.java).apply {
                putExtra("typeGroup",TypeGroup.FRUITS.value)
            })
        }

        mCardPlants?.setOnClickListener {
            it.preventDoubleClick()
            startActivity(Intent(context, ChallengeParentActivity::class.java).apply {
                putExtra("typeGroup",TypeGroup.PLANTS.value)
            })
        }
    }

    private fun observerComponents() {

        viewModel.challengesCompleted.observe(viewLifecycleOwner) { challenges ->
            hideSkeleton()
            mAdapter?.setUserList(challenges)
        }

        viewModel.challengesEmpty.observe(viewLifecycleOwner, Observer {
            //Show Skeleton empty
            hideSkeleton()
        })

        viewModel.loading.observe(viewLifecycleOwner){
            if(it) showSkeleton()
            else hideSkeleton()
        }
    }

    private fun hideSkeleton() {
        Log.d("TAG","hideSkeleton")
        shimmerSkeleton?.beGone()
        bodyLayout?.beVisible()

    }

    private fun showSkeleton() {
        Log.d("TAG","ShowSkeleton")
        bodyLayout?.beGone()
        shimmerSkeleton?.beVisible()

    }

    private fun getChallengesCompleted() {
        viewModel.showChallengesCompleted()
    }

    override fun onDestroyView() {
        viewModel.challengesCompleted.removeObservers(this)
        viewModel.newPostHided.removeObservers(this)
        viewModel.removeListener()
        super.onDestroyView()
    }

    override fun clickOnUpdateLike(item: ChallengeCompleted) {
        viewModel.updateLikeStatusFirebase(item)
    }

    override fun clickedComentThread(item: ChallengeCompleted) {
        val bottomSheetDialogFragment = BottomDialogFragmentComentar.newInstance(item.id?:"")
        bottomSheetDialogFragment.show(parentFragmentManager,"bottomSheetDialogFragment")
    }

    override fun clickedReportThread(item: ChallengeCompleted) {
        val fragment = BottomDialogFragmentMoreOptions.newInstance(toJson(item))
        fragment.show(parentFragmentManager,"bottomSheetMoreOptions")

    }


}