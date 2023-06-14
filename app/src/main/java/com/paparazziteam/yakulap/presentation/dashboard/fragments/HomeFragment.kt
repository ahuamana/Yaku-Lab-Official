package com.paparazziteam.yakulap.presentation.dashboard.fragments

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.type.LatLng
import com.paparazziteam.yakulap.databinding.FragmentHomeBinding
import com.paparazziteam.yakulap.helper.*
import com.paparazziteam.yakulap.helper.application.MyPreferences
import com.paparazziteam.yakulap.helper.design.decoration.ItemSpaceDecorationHorizontal
import com.paparazziteam.yakulap.helper.network.openUrl
import com.paparazziteam.yakulap.helper.others.LocationManager
import com.paparazziteam.yakulap.presentation.dashboard.adapters.AdapterChallengeCompleted
import com.paparazziteam.yakulap.presentation.dashboard.adapters.AdapterNearbySpecies
import com.paparazziteam.yakulap.presentation.dashboard.interfaces.onClickThread
import com.paparazziteam.yakulap.presentation.dashboard.pojo.ChallengeCompleted
import com.paparazziteam.yakulap.presentation.dashboard.pojo.TypeGroup
import com.paparazziteam.yakulap.presentation.dashboard.viewmodels.ViewModelDashboard
import com.paparazziteam.yakulap.presentation.laboratorio.views.ChallengeParentActivity
import com.paparazziteam.yakulap.usecases.SpeciesByLocationResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), onClickThread {

    @Inject
    lateinit var mPreferences:MyPreferences

    private var binding: FragmentHomeBinding by autoCleared()
    private val viewModel:ViewModelDashboard by activityViewModels()

    private var clickedItemCompleted:onClickThread?=null

    //Laboratorio
    var mLinearLayoutManager: LinearLayoutManager? = null

    //UI shimmer
    private var shimmerSkeleton: ShimmerFrameLayout? = null
    //UI body
    private var bodyLayout: ConstraintLayout? = null

    //Content Animals
    var mCardAnimals: CardView? = null
    var mCardFruits: CardView? = null
    var mCardPlants: CardView? = null

    //Adapter
    var mAdapter: AdapterChallengeCompleted? = null
    var adapterSpeciesNearby = AdapterNearbySpecies()

    //Get Last Know Location
    private val locationManager by lazy {
        LocationManager(requireActivity())
    }

    private val onListenerLocation = object : LocationManager.OnLocationReceivedListener {
        override fun onLocationReceived(location: Location?) {
            if(location == null) return
            location.let {
                viewModel.getSpeciesByLocation(it.latitude, it.longitude)
            }
        }

        override fun onLocationFailed() {
            Log.d("LOCATION", "onLocationFailed: ")
            val doubleLatitudePeruLima = -12.046374
            val doubleLongitudePeruLima = -77.042793
            viewModel.getSpeciesByLocation(doubleLatitudePeruLima, doubleLongitudePeruLima)
        }

        override fun onPermissionFailed() {
            Log.d("LOCATION", "onPermissionFailed: ")
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
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        ui()
        setUpRecycler()
        setupRecyclerNearbySpecies()
        observers()
        otherComponents()
        getChallengesCompleted()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //get last know location
        setupGetLastLocation()
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

        val spaceDecorationHorizontal = ItemSpaceDecorationHorizontal(30)
        //add divider to recycler
        binding.rvNearbySpecies.addItemDecoration(
            spaceDecorationHorizontal
        )

        adapterSpeciesNearby.onItemClickListener { observationEntity, position ->
            //TODO: Do logic
        }

        adapterSpeciesNearby.onClickItemWiki { observationEntity, position ->
            println("WIKI: ${observationEntity.identifications.first()?.taxon?.wikipedia_url}")
            observationEntity.identifications.first()?.taxon?.wikipedia_url?.let {
                openUrl(requireContext(), it)
            }
        }
    }

    private fun ui() {
        binding.apply {
            mCardAnimals    = cardAnimals
            mCardFruits         = cardFruits
            mCardPlants         = cardPlants
            shimmerSkeleton              = shimmerLoading
            bodyLayout                   = containerLayoutBodyChallenges
        }
        clickedItemCompleted = this
    }

    private fun setUpRecycler() {
        mLinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        mAdapter = AdapterChallengeCompleted(clickedItemCompleted, mPreferences)
        binding.rvChallengesCompleted.apply {
            layoutManager = mLinearLayoutManager
            adapter =  mAdapter
        }

        mAdapter?.onClickUpdateLikeListener {
            viewModel.updateLikeStatusFirebase(it)
        }
    }

    private fun otherComponents() {
        mCardAnimals?.setOnClickListener {
            it.preventDoubleClick()
            startActivity(Intent(context, ChallengeParentActivity::class.java).apply {
                putExtra("typeGroup",TypeGroup.ANIMALS.value)
            })
        }

        mCardFruits?.setOnClickListener {
            it.preventDoubleClick()
            startActivity(Intent(context, ChallengeParentActivity::class.java).apply {
                putExtra("typeGroup",TypeGroup.FRUITS.value)
            })
        }

        mCardPlants?.setOnClickListener {
            it.preventDoubleClick()
            startActivity(Intent(context, ChallengeParentActivity::class.java).apply {
                putExtra("typeGroup",TypeGroup.PLANTS.value)
            })
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
                    Log.d("TAG","speciesByLocation: $it")
                    showSpeciesByLocationDialog(it)
                }
            }
        }
    }

    private fun showSpeciesByLocationDialog(it: SpeciesByLocationResult) {
        when(it){
            is SpeciesByLocationResult.Error -> it.run{
                Log.e("TAG","Error: $message")
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

                Log.d("TAG","Success: $species")
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
        shimmerSkeleton?.beGone()
        bodyLayout?.beVisible()

    }

    private fun showSkeleton() {
        Log.d("TAG","ShowSkeleton")
        bodyLayout?.beGone()
        shimmerSkeleton?.beVisible()

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
        val fragment = BottomDialogFragmentMoreOptions.newInstance(toJson(item))
        fragment.show(parentFragmentManager,"bottomSheetMoreOptions")

    }


}