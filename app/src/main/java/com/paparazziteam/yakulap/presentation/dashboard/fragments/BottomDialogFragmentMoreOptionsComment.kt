package com.paparazziteam.yakulap.presentation.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textview.MaterialTextView
import com.paparazziteam.yakulab.binding.helper.beGone
import com.paparazziteam.yakulab.binding.utils.fromJson
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.BottomSheetMoreOptionsBinding
import com.paparazziteam.yakulap.presentation.dashboard.viewmodels.ViewModelDashboard
import com.yakulab.domain.dashboard.Comment
import com.yakulab.domain.dashboard.TypeReported
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomDialogFragmentMoreOptionsComment : BottomSheetDialogFragment() {

    private val ARG_DATA = "DATA"
    private var item: Comment?=null
    private var _binding: BottomSheetMoreOptionsBinding? = null
    private val binding get() = _binding!!

    private val _viewModel: ViewModelDashboard by activityViewModels()

    var contenedorOptionReport: ConstraintLayout? = null
    var contenedorOptionHidePost: ConstraintLayout? = null
    var txtTitleReport: MaterialTextView? = null
    var txtSubTitleReport: MaterialTextView? = null

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
            contenedorOptionReport      = opcionReportPost
            contenedorOptionHidePost    = opcionHide
            txtTitleReport              = titleReport
            txtSubTitleReport           = subtitleReport
        }
        setupComponentes()
        return view
    }

    private fun setupComponentes() {
        binding.opcionReportPost.setOnClickListener {
            item?.let { it -> _viewModel.reportComment(it, TypeReported.COMMENT) }
            dismiss()
        }

        binding.opcionBlockUser.setOnClickListener {
            item?.let { it -> _viewModel.blockUser(it.email?:"") }
            dismiss()
        }

        binding.opcionReportUser.setOnClickListener {
            item?.let { it -> _viewModel.reportUser(
                type = TypeReported.COMMENT,
                userReported = it.email?: "",
            ) }
            dismiss()
        }

        txtTitleReport?.text = getString(R.string.report_comment)
        txtSubTitleReport?.text = getString(R.string.concerned_comment)

        contenedorOptionHidePost?.beGone()
    }

    companion object {
        @JvmStatic
        fun newInstance(jsonItem: String) =
            BottomDialogFragmentMoreOptionsComment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DATA,jsonItem)
                }
            }
    }
}