package com.paparazziteam.yakulap.presentation.bienvenida.views

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.paparazziteam.yakulab.binding.utils.fromHtml
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.FragmentCustomDisclosureBinding


class CustomDisclosureFragment : DialogFragment() {

    private var _binding: FragmentCustomDisclosureBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = activity?.let {
        val builder = AlertDialog.Builder(it)
        _binding = FragmentCustomDisclosureBinding.inflate(layoutInflater)
        builder.setView(binding.root)
        builder.setCancelable(false)
        builder.create()
    } ?: throw IllegalStateException("Activity cannot be null")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTitle.text = getString(R.string.data_collection_disclosure_title)
        binding.tvDescription.text = Html.fromHtml(getString(R.string.data_collection_disclosure_message))
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}