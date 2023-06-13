package com.paparazziteam.yakulap.presentation.dashboard.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.FragmentBottomDialogBinding


class BottomDialogFragment : BottomSheetDialogFragment() {


    private var _binding: FragmentBottomDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =FragmentBottomDialogBinding.inflate(layoutInflater,container, false)
        val view = binding.root

        binding.opcionContacteSoporte.setOnClickListener {
            var msj = "${getString(R.string.mensaje_default_soporte)}"
            openWhatsapp("",msj)
        }

        return view
    }

    fun openWhatsapp(celular: String?, msj: String)
    {
        try {

            var msg = "Its Working"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=${getString(R.string.codigo_pais)}${getString(R.string.telefono_empresa)}" + celular + "&text=" + msj)).apply {
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }catch (t: Throwable){
            //whatsapp app not install
            println("Error whatsapp: $t")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            BottomDialogFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}