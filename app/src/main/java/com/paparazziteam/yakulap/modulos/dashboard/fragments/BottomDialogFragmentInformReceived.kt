package com.paparazziteam.yakulap.modulos.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.BottomSheetInformReceivedBinding
import com.paparazziteam.yakulap.databinding.BottomSheetNormasComunitariasBinding
import com.paparazziteam.yakulap.databinding.BottomSheetReportPostBinding
import com.paparazziteam.yakulap.helper.Constants.ARG_DATA
import com.paparazziteam.yakulap.helper.Constants.REPORT_TYPE
import com.paparazziteam.yakulap.helper.Constants.REPORT_TYPE_POST
import com.paparazziteam.yakulap.helper.autoCleared
import com.paparazziteam.yakulap.helper.convertFirstLetterToUpperCaseAndRestToLowerCase
import com.paparazziteam.yakulap.helper.fromHtml
import com.paparazziteam.yakulap.helper.fromJson
import com.paparazziteam.yakulap.modulos.dashboard.pojo.*
import com.paparazziteam.yakulap.modulos.dashboard.viewmodels.ViewModelDashboard

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomDialogFragmentInformReceived : BottomSheetDialogFragment() {

    private var itemReportTypePost:String?=null

    private var _binding: BottomSheetInformReceivedBinding by autoCleared()
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            itemReportTypePost = it.getString(REPORT_TYPE_POST)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetInformReceivedBinding.inflate(layoutInflater,container, false)
        ui()
        return _binding.root
    }


    private fun ui() {
        binding.apply {
            tvSubTitle.text = itemReportTypePost.convertFirstLetterToUpperCaseAndRestToLowerCase()
            btnClose.setOnClickListener {
                dismiss()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(jsonItem: String) =
            BottomDialogFragmentInformReceived().apply {
                arguments = Bundle().apply {
                    putString(ARG_DATA,jsonItem)
                }
            }
    }
}