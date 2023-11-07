package com.paparazziteam.yakulap.presentation.dashboard.fragments

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.paparazziteam.yakulab.binding.Constants
import com.paparazziteam.yakulab.binding.helper.application.MyPreferences
import com.paparazziteam.yakulab.binding.helper.beGone
import com.paparazziteam.yakulab.binding.helper.beVisible
import com.paparazziteam.yakulab.binding.helper.navigator.Navigator
import com.paparazziteam.yakulab.binding.helper.others.LocationManager
import com.paparazziteam.yakulab.binding.helper.others.PermissionManager
import com.paparazziteam.yakulab.binding.helper.preventDoubleClick
import com.paparazziteam.yakulab.binding.utils.toJson
import com.paparazziteam.yakulab.binding.utils.openUrl
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.databinding.FragmentHomeBinding
import com.paparazziteam.yakulap.presentation.dashboard.adapters.AdapterChallengeCompleted
import com.paparazziteam.yakulap.presentation.dashboard.adapters.AdapterNearbySpecies
import com.paparazziteam.yakulap.presentation.dashboard.interfaces.onClickThread
import com.yakulab.domain.dashboard.ChallengeCompleted
import com.yakulab.domain.dashboard.TypeGroup
import com.paparazziteam.yakulap.presentation.dashboard.viewmodels.HomeViewModel
import com.yakulab.domain.laboratory.toDataChallengeNearbySpecies
import com.paparazziteam.yakulap.navigation.NavigationRootImpl
import com.paparazziteam.yakulap.presentation.dashboard.adapters.AdapterSpeciesWithAR
import com.paparazziteam.yakulap.presentation.dashboard.views.SlideImageFullScreenActivity
import com.yakulab.usecases.inaturalist.SpeciesByLocationResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), onClickThread {

    @Inject
    lateinit var navigationRoot: NavigationRootImpl

    @Inject
    lateinit var navigatorModule: Navigator

    @Inject
    lateinit var preferences:MyPreferences

    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val viewModel: HomeViewModel by activityViewModels()

    private var clickedItemCompleted:onClickThread?=null

    //Laboratorio
    var mLinearLayoutManager: LinearLayoutManager? = null


    //Adapter
    var mAdapter: AdapterChallengeCompleted? = null
    private val adapterSpeciesNearby = AdapterNearbySpecies()

    //Adapter Species With AR
    private val adapterSpeciesWithAR = AdapterSpeciesWithAR()

    //Get Last Know Location
    private val locationManager by lazy {
        LocationManager(requireActivity())
    }

    private val permissionManager by lazy {
        PermissionManager(requireActivity())
    }

    private val onListenerLocation = object : LocationManager.OnLocationReceivedListener {
        override fun onLocationReceived(location: Location?) {
            if(location == null) return
            location.let {
                Timber.d("onLocationReceived: ${it.latitude} ${it.longitude}")
                viewModel.getSpeciesByLocation(it.latitude, it.longitude)
            }
        }

        override fun onLocationFailed() {
            Timber.d("LOCATION", "onLocationFailed: ")
            val doubleLatitudePeruLima = -12.046374
            val doubleLongitudePeruLima = -77.042793
            viewModel.getSpeciesByLocation(doubleLatitudePeruLima, doubleLongitudePeruLima)
        }

        override fun onPermissionFailed() {
            Timber.d("LOCATION", "onPermissionFailed: ")
            val doubleLatitudePeruLima = -12.046374
            val doubleLongitudePeruLima = -77.042793
            viewModel.getSpeciesByLocation(doubleLatitudePeruLima, doubleLongitudePeruLima)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ui()
        setUpRecycler()
        setupRecyclerNearbySpecies()
        setupRecyclerSpeciesWithAR()
        observers()
        otherComponents()
        getChallengesCompleted()

        //get last know location
        if(permissionManager.checkLocationPermission()) {
            locationManager.getLastKnownLocationOnlyOnce(onListenerLocation)
        }else{
            permissionManager.requestLocationPermission()
            locationManager.getLastKnownLocationOnlyOnce(onListenerLocation)
        }
    }

    private fun setupRecyclerSpeciesWithAR() {
        val lManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        binding.rvSpeciesWithAR.apply {
            layoutManager = lManager
            adapter = adapterSpeciesWithAR
        }

        adapterSpeciesWithAR.onItemClickListener { itemSpecieAR, position ->
            val bundle = Bundle()
            bundle.putString(Constants.AR_MODEL, itemSpecieAR.urlModel)
            bundle.putFloat(Constants.AR_SCALE_IN_UNIT, itemSpecieAR.scaleInUnit)
            navigatorModule.navigateToAR(requireContext(), false, bundle)
        }
    }

    private fun setupGetLastLocation() {
        //Set up location listener to handle location changes or failures
        locationManager.getLastKnownLocationOnlyOnce(onListenerLocation)
    }

    private fun setupRecyclerNearbySpecies() {
        binding.rvNearbySpecies.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
            adapter = adapterSpeciesNearby
        }

        /*val spaceDecorationHorizontal = ItemSpaceDecorationHorizontal(30)
        //add divider to recycler
        binding.rvNearbySpecies.addItemDecoration(
            spaceDecorationHorizontal
        )*/

        adapterSpeciesNearby.onItemClickListener { observationEntity, position ->
            Timber.d( "onCreateView: ${observationEntity.identifications.first()?.taxon?.name}")
            val observationJson = toJson(observationEntity.toDataChallengeNearbySpecies())

            val bundle = Bundle()
            bundle.putString(Constants.EXTRA_CHALLENGE, observationJson)
            navigationRoot.navigateToChallenge(bundle)
        }

        adapterSpeciesNearby.onClickItemWiki { observationEntity, position ->
            Timber.d("WIKI: ${observationEntity.identifications.first()?.taxon?.wikipedia_url}")
            observationEntity.identifications.first()?.taxon?.wikipedia_url?.let {
                openUrl(requireContext(), it)
            }
        }

        adapterSpeciesNearby.onClickItemImageFullScreen { observationEntity, position, url ->
            var list = listOf(url)
            val intent = Intent(this.context, SlideImageFullScreenActivity::class.java)
            intent.putExtra("lista_imagenes", list.toString())
            intent.putExtra("position"      , position)
            requireActivity().startActivity(intent)
        }
    }

    private fun ui() {
        clickedItemCompleted = this
    }

    private fun setUpRecycler() {
        mLinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        mAdapter = AdapterChallengeCompleted(clickedItemCompleted, preferences)
        binding.rvChallengesCompleted.apply {
            layoutManager = mLinearLayoutManager
            adapter =  mAdapter
        }

        mAdapter?.onClickUpdateLikeListener {
            viewModel.updateLikeStatusFirebase(it)
        }

        mAdapter?.onClickShareListener {
           //Share the app
            Timber.d("Share the app")
            shareApp()
        }

        mAdapter?.onClickImageListener { challenge, position ->
            var list = listOf(challenge.url)
            val intent = Intent(this.context, SlideImageFullScreenActivity::class.java)
            intent.putExtra("lista_imagenes", list.toString())
            intent.putExtra("position"      , position)
            requireContext().startActivity(intent)
        }
    }

    private fun shareApp(){
        val intentCompartir = Intent(Intent.ACTION_SEND)
        intentCompartir.type = "text/plain"
        intentCompartir.putExtra(Intent.EXTRA_SUBJECT, "¡Descubre YAKU LAB!")
        val messageShare = getString(R.string.fragment_home_message_share, requireActivity().packageName).trimIndent()
        intentCompartir.putExtra(Intent.EXTRA_TEXT, messageShare)
        startActivity(Intent.createChooser(intentCompartir, "Compartir en"))
    }

    private fun otherComponents() {
        binding.cardAnimals.setOnClickListener {
            println("Animals")
            it.preventDoubleClick()
            val bundle = Bundle()
            bundle.putString("typeGroup", TypeGroup.ANIMALS.value)
            navigationRoot.navigateToChallengeList(bundle)
        }

        binding.cardFruits.setOnClickListener {
            it.preventDoubleClick()
            val bundle = Bundle()
            bundle.putString("typeGroup", TypeGroup.FRUITS.value)
            navigationRoot.navigateToChallengeList(bundle)
        }

        binding.cardPlants.setOnClickListener {
            it.preventDoubleClick()
            val bundle = Bundle()
            bundle.putString("typeGroup", TypeGroup.PLANTS.value)
            navigationRoot.navigateToChallengeList(bundle)
        }
    }

    private fun observers() {
        viewModel.challengesCompleted.observe(viewLifecycleOwner) { challenges ->
            hideSkeleton()
            if(challenges.isNotEmpty()) binding.rvChallengesCompleted.beVisible()
            else binding.rvChallengesCompleted.beGone()

            mAdapter?.setUserList(challenges)
        }

        viewModel.challengesEmpty.observe(viewLifecycleOwner, Observer {
            //Show Skeleton empty
            hideSkeleton()
        })

        viewModel.loading.observe(viewLifecycleOwner){
            if(it) showSkeleton()
            else hideSkeleton()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.speciesByLocation.collect() {
                    showSpeciesByLocationDialog(it)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.speciesWithAR.collect {
                   adapterSpeciesWithAR.submitList(it)
                }
            }
        }


    }

    private fun showSpeciesByLocationDialog(it: SpeciesByLocationResult) {
        when(it){
            is SpeciesByLocationResult.Error -> it.run{
                binding.apply {
                    internalContainerLayoutBodyNearbySpecies.beVisible()
                    containerBodyNearbySpecies.beGone()
                    containerBodyNearbySpeciesEmpty.beVisible()
                }
            }
            SpeciesByLocationResult.HideLoading -> {

                binding.shimmerNearbySpecies.beGone()
            }
            SpeciesByLocationResult.ShowLoading -> {
                binding.shimmerNearbySpecies.beVisible()
            }
            is SpeciesByLocationResult.Success -> it.run{
                binding.apply {
                    internalContainerLayoutBodyNearbySpecies.beVisible()
                    containerBodyNearbySpecies.beVisible()
                    containerBodyNearbySpeciesEmpty.beGone()
                }
                adapterSpeciesNearby.submitList(species)
            }

            SpeciesByLocationResult.Empty -> {
                binding.apply {
                    internalContainerLayoutBodyNearbySpecies.beVisible()
                    containerBodyNearbySpecies.beGone()
                    containerBodyNearbySpeciesEmpty.beVisible()
                }

            }
        }
    }

    private fun hideSkeleton() {
        Log.d("TAG","hideSkeleton")
        binding.shimmerLoading.beGone()
        binding.containerBody.beVisible()

    }

    private fun showSkeleton() {
        Log.d("TAG","ShowSkeleton")
        binding.containerBody.beGone()
        binding.shimmerLoading.beVisible()

    }

    private fun getChallengesCompleted() {
        viewModel.showChallengesCompleted()
    }

    override fun onDestroyView() {
        viewModel.challengesCompleted.removeObservers(this)
        viewModel.newPostHided.removeObservers(this)
        viewModel.removeListener()
        super.onDestroyView()
    }

    override fun clickOnUpdateLike(item: ChallengeCompleted) {
        viewModel.updateLikeStatusFirebase(item)
    }

    override fun clickedComentThread(item: ChallengeCompleted) {
        val bottomSheetDialogFragment = BottomDialogFragmentComentar.newInstance(item.id?:"")
        bottomSheetDialogFragment.show(parentFragmentManager,"bottomSheetDialogFragment")
    }

    override fun clickedReportThread(item: ChallengeCompleted) {
        val fragment = BottomDialogFragmentMoreOptions.newInstance(toJson(item) ?:"")
        fragment.show(parentFragmentManager,"bottomSheetMoreOptions")

    }


}