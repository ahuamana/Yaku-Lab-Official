package com.paparazziteam.yakulap.modulos.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.paparazziteam.yakulap.databinding.BottomSheetMoreOptionsBinding
import com.paparazziteam.yakulap.helper.fromJson
import com.paparazziteam.yakulap.modulos.dashboard.pojo.*
import com.paparazziteam.yakulap.modulos.dashboard.viewmodels.ViewModelDashboard
import com.paparazziteam.yakulap.modulos.providers.CommentProvider
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class BottomDialogFragmentMoreOptions : BottomSheetDialogFragment() {

    private val ARG_DATA = "DATA"
    private var item:MoldeChallengeCompleted?=null
    private var _binding: BottomSheetMoreOptionsBinding? = null
    private val binding get() = _binding!!

    private val _viewModel: ViewModelDashboard by viewModels()


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
            item?.let { it -> _viewModel.reportContent(it,TypeReport.THREAD) }
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
            BottomDialogFragmentMoreOptions().apply {
                arguments = Bundle().apply {
                    putString(ARG_DATA,jsonItem)
                }
            }
    }
}