package com.paparazziteam.yakulap.presentation.laboratorio.fragments.bottomsheets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.FragmentBottomSheetDialogOptionsCameraBinding
import viewBinding


class BottomSheetDialogOptionsCameraFragment(
    private val onOptionSelectedSourcePicker: OnOptionSelectedSourcePicker
) : BottomSheetDialogFragment() {

    private val binding by viewBinding { FragmentBottomSheetDialogOptionsCameraBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentBottomSheetDialogOptionsCameraBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Logic here

        binding.optionBtnCamera.setOnClickListener {
            onOptionSelectedSourcePicker.onOptionSelectedCamera()
            dismiss()
        }

        binding.optionBtnGallery.setOnClickListener {
            onOptionSelectedSourcePicker.onOptionSelectedGallery()
            dismiss()
        }
    }
}