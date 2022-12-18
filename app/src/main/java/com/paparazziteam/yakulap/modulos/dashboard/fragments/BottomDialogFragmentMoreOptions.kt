package com.paparazziteam.yakulap.modulos.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.paparazziteam.yakulap.databinding.BottomSheetMoreOptionsBinding
import com.paparazziteam.yakulap.helper.fromJson
import com.paparazziteam.yakulap.helper.toJson
import com.paparazziteam.yakulap.modulos.dashboard.pojo.*
import com.paparazziteam.yakulap.modulos.dashboard.viewmodels.ViewModelDashboard

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomDialogFragmentMoreOptions : BottomSheetDialogFragment() {

    private val ARG_DATA = "DATA"
    private var item:ChallengeCompleted?=null
    private var _binding: BottomSheetMoreOptionsBinding? = null
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
        _binding = BottomSheetMoreOptionsBinding.inflate(layoutInflater,container, false)
        val view = binding.root

        binding.apply {
            contenedorOptionReport  = opcionReportPost
            contenedorOptionHidePost = opcionHide
        }
        setupComponentes()
        return view
    }

    private fun setupComponentes() {
        contenedorOptionReport?.setOnClickListener {
            openBottomDialogReport()
            /*item?.let { it -> _viewModel.reportPost(it,TypeReported.POST) }
            dismiss()*/
        }

        contenedorOptionHidePost?.setOnClickListener {
            item?.let { it-> _viewModel.addPostBlocked(it.id?:"") }
            dismiss()
        }
    }

    private fun openBottomDialogReport() {
        val bottomDialogFragmentReport = BottomDialogFragmentReport()
        val bundle = Bundle()
        bundle.putString(ARG_DATA, toJson(item))
        bottomDialogFragmentReport.arguments = bundle
        bottomDialogFragmentReport.show(parentFragmentManager, bottomDialogFragmentReport.tag)
        dismiss()
    }

    companion object {
        @JvmStatic
        fun newInstance(jsonItem: String) =
            BottomDialogFragmentMoreOptions().apply {
                arguments = Bundle().apply {
                    putString(ARG_DATA,jsonItem)
                }
            }
    }
}