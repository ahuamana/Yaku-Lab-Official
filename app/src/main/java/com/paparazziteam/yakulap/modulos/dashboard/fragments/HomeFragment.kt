package com.paparazziteam.yakulap.modulos.dashboard.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.paparazziteam.yakulap.databinding.FragmentHomeBinding
import com.paparazziteam.yakulap.helper.application.MyPreferences
import com.paparazziteam.yakulap.helper.fromJson
import com.paparazziteam.yakulap.helper.preventDoubleClick
import com.paparazziteam.yakulap.helper.toJson
import com.paparazziteam.yakulap.modulos.dashboard.adapters.AdapterChallengeCompleted
import com.paparazziteam.yakulap.modulos.dashboard.interfaces.onClickThread
import com.paparazziteam.yakulap.modulos.dashboard.pojo.MoldeChallengeCompleted
import com.paparazziteam.yakulap.modulos.dashboard.viewmodels.ViewModelDashboard
import com.paparazziteam.yakulap.modulos.laboratorio.views.ChallengeParentActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), onClickThread {

    @Inject
    lateinit var mPreferences:MyPreferences

    private var _binding: FragmentHomeBinding? = null
    private val _viewModel by viewModels<ViewModelDashboard>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var clickedItemCompleted:onClickThread?=null

    //Laboratorio
    var mLinearLayoutManager: LinearLayoutManager? = null
    private var recyclerView: RecyclerView? = null

    //Content Animals
    var mLinearLayoutAnimals: LinearLayout? = null

    //Adapter
    var mAdapter: AdapterChallengeCompleted? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        clickedItemCompleted = this

        binding.apply {
            recyclerView = recycler
            mLinearLayoutAnimals = linearLayoutAnimales
        }

        observerComponents()
        getDataLaboratorio()
        otherComponents()

        return root
    }

    private fun otherComponents() {
        mLinearLayoutAnimals?.setOnClickListener {
            it.preventDoubleClick()
            startActivity(Intent(context, ChallengeParentActivity::class.java))
        }
    }

    private fun observerComponents() {

        _viewModel.challengesCompletedObservable.observe(viewLifecycleOwner) { challenges ->
            if (challenges.isNotEmpty()) {
                println("challenges total: ${challenges.count()}")
                var posts: MutableList<String>
                if(!mPreferences.postBlocked.isNullOrEmpty()){
                    try {
                        posts = fromJson(mPreferences.postBlocked)
                        println("challenges total: $posts")
                        posts.forEach { _post->

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                challenges.removeIf{
                                    it.id == _post
                                }
                            }else{

                            }
                        }
                    }catch (t:Throwable){
                        FirebaseCrashlytics.getInstance().recordException(t)
                    }

                }
                mAdapter = AdapterChallengeCompleted(challenges, clickedItemCompleted, mPreferences)
                recyclerView?.adapter = mAdapter
            }
        }

        _viewModel.newPostBlocked.observe(viewLifecycleOwner){
            mAdapter?.removePost(it)
        }
    }

    private fun getDataLaboratorio() {
        mLinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        recyclerView?.layoutManager = mLinearLayoutManager
        getChallengesCompleted()
    }

    private fun getChallengesCompleted() {
        _viewModel.showChallengesCompleted()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _viewModel.challengesCompletedObservable.removeObservers(this)
    }

    override fun clickOnUpdateLike(item: MoldeChallengeCompleted) {
        _viewModel.updateLikeStatusFirebase(item)
    }

    override fun clickedComentThread(item: MoldeChallengeCompleted) {
        val bottomSheetDialogFragment = BottomDialogFragmentComentar.newInstance(item.id?:"")
        bottomSheetDialogFragment.show(parentFragmentManager,"bottomSheetDialogFragment")
    }

    override fun clickedReportThread(item: MoldeChallengeCompleted) {
        val fragment = BottomDialogFragmentMoreOptions.newInstance(toJson(item))
        fragment.show(parentFragmentManager,"bottomSheetMoreOptions")

    }


}