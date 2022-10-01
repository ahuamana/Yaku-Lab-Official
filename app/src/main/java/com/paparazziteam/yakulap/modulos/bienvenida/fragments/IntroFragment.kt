package com.paparazziteam.yakulap.modulos.bienvenida.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.paparazziteam.yakulap.databinding.FragmentIntroBinding


class IntroFragment : Fragment() {
    private var _binding: FragmentIntroBinding? = null
    private val binding get() = _binding!!
    private val EXTRA_IMG   = "EXTRA_ING_INTRO_FRAGMENT"
    private val EXTRA_TITLE = "EXTRA_TITLE_INTRO_FRAGMENT"
    private val EXTRA_DESC  = "EXTRA_DESC_INTRO_FRAGMENT"


    fun newInstance(img: Int, title: String, desc: String) =
        IntroFragment().apply {
            arguments = Bundle().apply {
                putInt(EXTRA_IMG     , img)
                putString(EXTRA_TITLE, title)
                putString(EXTRA_DESC , desc)
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       _binding = FragmentIntroBinding.inflate(inflater, container, false)
        val view = binding.root

        arguments?.let { value ->
            binding.introImg.setImageResource(value.getInt(EXTRA_IMG))
            binding.introTitle.text = value.getString(EXTRA_TITLE)
            binding.introDesc.text = value.getString(EXTRA_DESC)
        }

        return view
    }

}