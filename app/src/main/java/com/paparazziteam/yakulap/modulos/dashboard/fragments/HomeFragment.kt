package com.paparazziteam.yakulap.modulos.dashboard.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paparazziteam.yakulap.databinding.FragmentHomeBinding
import com.paparazziteam.yakulap.helper.preventDoubleClick
import com.paparazziteam.yakulap.modulos.dashboard.adapters.AdapterChallengeCompleted
import com.paparazziteam.yakulap.modulos.dashboard.viewmodels.ViewModelDashboard
import com.paparazziteam.yakulap.modulos.laboratorio.views.ChallengeParentActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private var _viewModel = ViewModelDashboard.getInstance()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //Laboratorio
    var mLinearLayoutManager: LinearLayoutManager? = null
    private var recyclerView: RecyclerView? = null

    //Content Animals
    var mLinearLayoutAnimals: LinearLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

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

        _viewModel.getChallengeCompletedData().observe(viewLifecycleOwner) { challenges ->
            if (challenges.isNotEmpty()) {
                println("challenges total: ${challenges.count()}")
                recyclerView?.adapter = AdapterChallengeCompleted(challenges)
            }
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
    }
}