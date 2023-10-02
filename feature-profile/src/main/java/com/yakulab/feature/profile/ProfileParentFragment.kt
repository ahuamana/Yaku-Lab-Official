package com.yakulab.feature.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.yakulab.feature.profile.databinding.FragmentProfileParentBinding
import com.yakulab.feature.profile.viewmodels.ProfileParentViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch
import timber.log.Timber
import viewBinding


@AndroidEntryPoint
class ProfileParentFragment : Fragment() {

    val binding by viewBinding(FragmentProfileParentBinding::bind)

    val viewModel by viewModels<ProfileParentViewModel>()

    private val medalsAdapter = MedalsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileParentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Code here
        setupUI()
        setupRecyclerMedals()
        observers()
    }

    private fun setupRecyclerMedals() {
        val manager = GridLayoutManager(requireContext(), 3)
        binding.rvMedals.apply {
            layoutManager = manager
            adapter = medalsAdapter
        }

        medalsAdapter.onItemClickListener { item, position ->
            /*TODO:*/
        }
    }

    private fun observers() {
        lifecycleScope.launch {
            viewModel.user.collect {
                binding.tvName.text = it
            }
        }

        lifecycleScope.launch {
            viewModel.medals.collect {
                Timber.d("Medals: $it")
                medalsAdapter.submitList(it)
            }
        }
    }

    private fun setupUI() {
    }
}