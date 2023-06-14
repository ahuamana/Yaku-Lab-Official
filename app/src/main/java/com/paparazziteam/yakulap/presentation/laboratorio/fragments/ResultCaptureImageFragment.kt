package com.paparazziteam.yakulap.presentation.laboratorio.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.FragmentResultCaptureImageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultCaptureImageFragment : Fragment() {

    private val binding by lazy { FragmentResultCaptureImageBinding.inflate(layoutInflater) }

    private val options: RequestOptions =
        RequestOptions().override(300,200).transform(CenterCrop(), RoundedCorners(10))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getExtras()
    }

    private fun getExtras() {
        val intent = requireActivity().intent
        if(intent.extras!= null){
            var title = intent.getStringExtra("title")
            var description = intent.getStringExtra("description")
            var image = intent.getStringExtra("image")
            var pointsToGive = intent.getIntExtra("pointsToGive",0)

            binding.textPoints.text = pointsToGive.toString()
            binding.title.text = title
            binding.textViewDescriptionSustantivo.text = description

            Glide.with(this)
                .load(image)
                .apply(options)
                .into(binding.roundedImageViewSustantivo)
        }
    }
}