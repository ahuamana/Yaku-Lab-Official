package com.yakulab.feature.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.yakulab.feature.profile.databinding.FragmentProfileParentBinding
import com.yakulab.feature.profile.viewmodels.ProfileParentViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch
import viewBinding


@AndroidEntryPoint
class ProfileParentFragment : Fragment() {

    val binding by viewBinding(FragmentProfileParentBinding::bind)

    val viewModel by viewModels<ProfileParentViewModel>()

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
        observers()
    }

    private fun observers() {
        lifecycleScope.launch {
            viewModel.user.collect {
                binding.tvName.text = it
            }
        }
    }

    private fun setupUI() {
    }
}