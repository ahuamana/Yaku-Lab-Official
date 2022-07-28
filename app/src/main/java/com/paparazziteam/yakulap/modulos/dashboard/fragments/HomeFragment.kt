package com.paparazziteam.yakulap.modulos.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paparazziteam.yakulap.databinding.FragmentHomeBinding
import com.paparazziteam.yakulap.helper.replaceFirstCharInSequenceToUppercase
import com.paparazziteam.yakulap.modulos.dashboard.adapters.AdapterChallengeCompleted
import com.paparazziteam.yakulap.modulos.dashboard.viewmodels.ViewModelDashboard
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private var _viewModel = ViewModelDashboard.getInstance()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //Laboratorio
    var mLinearLayoutManager: LinearLayoutManager? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.apply {
            recyclerView = recycler
        }

        observerComponents()
        getDataLaboratorio()

        return root
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