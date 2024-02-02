package com.paparazziteam.yakulap.presentation.laboratorio.ar

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.paparazziteam.yakulab.binding.Constants
import com.paparazziteam.yakulab.binding.Constants.AR_MODEL
import com.paparazziteam.yakulab.binding.Constants.AR_SCALE_IN_UNIT
import com.paparazziteam.yakulab.binding.Constants.AR_SPECIE
import com.paparazziteam.yakulab.binding.helper.load
import com.paparazziteam.yakulab.binding.helper.loadWithBlur
import com.paparazziteam.yakulab.binding.helper.navigator.Navigator
import com.paparazziteam.yakulab.binding.utils.getParcelableObject
import com.paparazziteam.yakulap.databinding.FragmentChallengeARBinding
import com.paparazziteam.yakulap.navigation.NavigationRootImpl
import com.paparazziteam.yakulap.navigation.NavigatorImpl
import com.yakulab.domain.dashboard.ItemSpecieAR
import com.yakulab.domain.dashboard.ItemSpecieARParcelable
import com.yakulab.domain.dashboard.toItemSpecieAR
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ChallengeFragmentAR : Fragment() {

    val binding by lazy { FragmentChallengeARBinding.inflate(layoutInflater) }

    val item: ItemSpecieARParcelable by lazy {
        arguments?.getParcelableObject(AR_SPECIE)?: ItemSpecieARParcelable(
            uuid = "",
            urlModel = "",
            name = "",
            preview = "",
            scaleInUnit = 0.5f
        )
    }

    @Inject
    lateinit var navigationRoot: NavigationRootImpl

    @Inject
    lateinit var navigatorModule: Navigator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
        loadComponents()
    }

    private fun loadComponents() {
        with(binding){
            challengePhotoParent.loadWithBlur(item.preview?:"")
            challengePhotoChild.load(item.preview?:"")
        }
    }

    private fun setupButtons() {

        val itemSpecieAR = item.toItemSpecieAR()
        Timber.d("itemSpecieAR - Challenge: $itemSpecieAR")

        binding.fabSelectImage.setOnClickListener {
            handleClickAR(itemSpecieAR)
        }

        binding.cardBack.setOnClickListener {
            navigationRoot.onBack()
        }

    }

    private fun handleClickAR(itemSpecieAR: ItemSpecieAR) {
        val bundle = Bundle()
        bundle.putString(AR_MODEL, itemSpecieAR.urlModel)
        bundle.putFloat(AR_SCALE_IN_UNIT, itemSpecieAR.scaleInUnit)
        navigatorModule.navigateToARWithBundleAndResultLauncher(requireContext(), isUnique = false, bundle = bundle, launcher = startForResultAR)
    }

    val startForResultAR = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            Timber.d("startForResult - resultCode OK: ${result.resultCode}")
            val data: Intent? = result.data
            val bitmap:ByteArray? = data?.getByteArrayExtra(Constants.AR_SCREEN_SHOOT)

            Timber.d("startForResult - bitmap: $bitmap")

            //ByteArray to Bitmap3
            val bitmap3 = bitmap?.let {
                android.graphics.BitmapFactory.decodeByteArray(it, 0, it.size)
            }

            //Set Bitmap to ImageView
            binding.challengePhotoToComplete.setImageBitmap(bitmap3)

        }else{
            Timber.d("startForResult - resultCode ELSE: ${result.resultCode}")
        }
    }

}