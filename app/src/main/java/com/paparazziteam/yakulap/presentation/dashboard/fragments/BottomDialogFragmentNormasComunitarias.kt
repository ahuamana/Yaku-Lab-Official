package com.paparazziteam.yakulap.presentation.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.BottomSheetNormasComunitariasBinding

import com.paparazziteam.yakulab.binding.Constants.ARG_DATA
import com.paparazziteam.yakulab.binding.Constants.REPORT_TYPE
import com.paparazziteam.yakulab.binding.Constants.REPORT_TYPE_POST
import com.paparazziteam.yakulab.binding.helper.autoCleared
import com.paparazziteam.yakulab.binding.helper.convertFirstLetterToUpperCaseAndRestToLowerCase
import com.paparazziteam.yakulab.binding.helper.fromHtml
import com.paparazziteam.yakulab.binding.helper.fromJson
import com.paparazziteam.yakulap.presentation.dashboard.viewmodels.ViewModelDashboard
import com.yakulab.domain.dashboard.ChallengeCompleted
import com.yakulab.domain.dashboard.TypeReported
import com.yakulab.domain.dashboard.TypeReportedPost

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomDialogFragmentNormasComunitarias : BottomSheetDialogFragment() {


    private var item: ChallengeCompleted?=null
    private var itemReportType:String?=null
    private var itemReportTypePost:String?=null

    private var reportType = TypeReported.POST
    private var reportTypePost = TypeReportedPost.OTHER

    private var _binding: BottomSheetNormasComunitariasBinding by autoCleared()
    private val binding get() = _binding!!

    private val _viewModel: ViewModelDashboard by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            item = fromJson(it.getString(ARG_DATA)?:"", ChallengeCompleted::class.java)
            itemReportType = it.getString(REPORT_TYPE)
            itemReportTypePost = it.getString(REPORT_TYPE_POST)
        }
    }

    /*override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        return dialog
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetNormasComunitariasBinding.inflate(layoutInflater,container, false)
        ui()
        validateData()
        listeners()
        return _binding.root
    }

    private fun validateData() {
        when(itemReportType){
            TypeReported.POST.value -> {
                reportType = TypeReported.POST
            }
            TypeReported.COMMENT.value -> {
                reportType = TypeReported.COMMENT
            }
        }

        when(itemReportTypePost){
            TypeReportedPost.OTHER.value -> {
                reportTypePost = TypeReportedPost.OTHER
            }
            TypeReportedPost.NAKED.value -> {
                reportTypePost = TypeReportedPost.NAKED
            }
            TypeReportedPost.VIOLENCE.value->{
                reportTypePost = TypeReportedPost.VIOLENCE
            }
            TypeReportedPost.SUICIDE.value->{
                reportTypePost = TypeReportedPost.SUICIDE
            }
            TypeReportedPost.HARASSMENT.value->{
                reportTypePost = TypeReportedPost.HARASSMENT
            }

        }
    }

    private fun ui() {
        binding.apply {
            tvTitle.text = itemReportTypePost.convertFirstLetterToUpperCaseAndRestToLowerCase()
            tvComunityNorms.text =  getString(R.string.htmlComunityRulesToDelete).fromHtml()
        }
    }

    private fun listeners() {
        binding.apply {
            item?.let { challenge ->
                btnSendReport.setOnClickListener {
                    _viewModel.reportPost(challenge, reportType, reportTypePost)
                    openDialogReceiveInform()
                }
            }
        }
    }

    private fun openDialogReceiveInform() {
        val bottomDialogFragmentReport = BottomDialogFragmentInformReceived()
        val bundle = Bundle()
        bundle.putString(REPORT_TYPE_POST,reportTypePost.value)
        bottomDialogFragmentReport.arguments = bundle
        bottomDialogFragmentReport.show(parentFragmentManager, bottomDialogFragmentReport.tag)
        dismiss()
    }


    companion object {
        @JvmStatic
        fun newInstance(jsonItem: String) =
            BottomDialogFragmentNormasComunitarias().apply {
                arguments = Bundle().apply {
                    putString(ARG_DATA,jsonItem)
                }
            }
    }
}