package com.paparazziteam.yakulap.presentation.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.paparazziteam.yakulap.databinding.BottomSheetReportPostBinding
import com.paparazziteam.yakulab.binding.Constants.REPORT_TYPE
import com.paparazziteam.yakulab.binding.Constants.REPORT_TYPE_POST
import com.paparazziteam.yakulab.binding.helper.autoCleared
import com.paparazziteam.yakulab.binding.utils.fromJson
import com.paparazziteam.yakulab.binding.helper.preventDoubleClick
import com.paparazziteam.yakulab.binding.utils.toJson
import com.paparazziteam.yakulap.presentation.dashboard.viewmodels.ViewModelDashboard
import com.yakulab.domain.dashboard.ChallengeCompleted
import com.yakulab.domain.dashboard.TypeReported
import com.yakulab.domain.dashboard.TypeReportedPost

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomDialogFragmentReport : BottomSheetDialogFragment() {

    private val ARG_DATA = "DATA"
    private var item: ChallengeCompleted?=null
    private var _binding: BottomSheetReportPostBinding by autoCleared()
    private val binding get() = _binding!!

    private val _viewModel: ViewModelDashboard by activityViewModels()


    var contenedorOptionReport: ConstraintLayout? = null
    var contenedorOptionHidePost: ConstraintLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            item = fromJson(it.getString(ARG_DATA)?:"")
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
        _binding = BottomSheetReportPostBinding.inflate(layoutInflater,container, false)

        listeners()
        setupComponentes()
        return _binding.root
    }

    private fun listeners() {
        binding.apply {
            item?.let { challenge ->
                opcionNaked.setOnClickListener {
                    it.preventDoubleClick()
                    openDialogNormsComunity(challenge, TypeReportedPost.NAKED)
                }
                opcionViolence.setOnClickListener {
                    it.preventDoubleClick()
                    openDialogNormsComunity(challenge, TypeReportedPost.VIOLENCE)
                }
                opcionTerrorism.setOnClickListener {
                    it.preventDoubleClick()
                    openDialogNormsComunity(challenge, TypeReportedPost.TERRORISM)
                }
                opcionHarassment.setOnClickListener {
                    it.preventDoubleClick()
                    openDialogNormsComunity(challenge, TypeReportedPost.HARASSMENT)
                }
                opcionSuicide.setOnClickListener {
                    it.preventDoubleClick()
                    openDialogNormsComunity(challenge, TypeReportedPost.SUICIDE)
                }
            }
        }
    }

    fun openDialogNormsComunity(item: ChallengeCompleted, typeReportedPost: TypeReportedPost){
        val bottomDialogFragmentReport = BottomDialogFragmentNormasComunitarias()
        val bundle = Bundle()
        bundle.putString(ARG_DATA, toJson(item))
        bundle.putString(REPORT_TYPE, TypeReported.POST.value)
        bundle.putString(REPORT_TYPE_POST,typeReportedPost.value)
        bottomDialogFragmentReport.arguments = bundle
        bottomDialogFragmentReport.show(parentFragmentManager, bottomDialogFragmentReport.tag)
        dismiss()
    }

    private fun setupComponentes() {
        contenedorOptionReport?.setOnClickListener {
            item?.let { it -> _viewModel.reportPost(it, TypeReported.POST) }
            dismiss()
        }

        contenedorOptionHidePost?.setOnClickListener {
            item?.let { it-> _viewModel.addPostBlocked(it.id?:"") }
            dismiss()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(jsonItem: String) =
            BottomDialogFragmentReport().apply {
                arguments = Bundle().apply {
                    putString(ARG_DATA,jsonItem)
                }
            }
    }
}