package com.paparazziteam.yakulap.presentation.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.paparazziteam.yakulap.databinding.BottomSheetMoreOptionsBinding
import com.paparazziteam.yakulab.binding.Constants.ARG_DATA
import com.paparazziteam.yakulab.binding.helper.application.MyPreferences
import com.paparazziteam.yakulab.binding.helper.autoCleared
import com.paparazziteam.yakulab.binding.helper.fromJson
import com.paparazziteam.yakulab.binding.helper.toJson
import com.paparazziteam.yakulap.presentation.dashboard.viewmodels.ViewModelDashboard
import com.yakulab.domain.dashboard.ChallengeCompleted
import com.yakulab.domain.dashboard.TypeReported

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomDialogFragmentMoreOptions : BottomSheetDialogFragment() {

    private var item: ChallengeCompleted?=null
    private var _binding: BottomSheetMoreOptionsBinding by autoCleared()
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

        ui()
        setupComponentes()
        listeners()
        return _binding.root
    }

    private fun listeners() {
        _binding.apply {
            opcionBlockUser.setOnClickListener {
                item?.author_email?.let { email -> _viewModel.blockUser(email) }
                dismiss()
            }
            opcionReportUser.setOnClickListener {
                item?.author_email?.let { email ->
                    _viewModel.reportUser(TypeReported.USER, email)
                }
                dismiss()
            }
        }
    }

    private fun ui() {
        _binding.apply {
            contenedorOptionReport  = opcionReportPost
            contenedorOptionHidePost = opcionHide
            println("item?.author_email: ${item?.author_email}")
            println("MyPreferences().email_login: ${MyPreferences().email}")
            if(item?.author_email == MyPreferences().email) {
                opcionBlockUser.visibility = View.GONE
                opcionReportUser.visibility = View.GONE
                opcionReportPost.visibility = View.GONE
            }
        }


    }

    private fun setupComponentes() {
        contenedorOptionReport?.setOnClickListener {
            openBottomDialogReport()
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