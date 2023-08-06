package com.paparazziteam.yakulap.presentation.laboratorio.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paparazziteam.yakulab.binding.helper.fromJson
import com.paparazziteam.yakulap.databinding.FragmentListChallengeBinding
import com.paparazziteam.yakulap.presentation.laboratorio.adapters.AdapterGridChallenge
import com.yakulab.domain.laboratory.DataChallenge
import com.paparazziteam.yakulap.navigation.NavigationRootImpl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


private const val ARG_TYPE_CATEGORY= "param1"
private const val ARG_DATA = "param2"

@AndroidEntryPoint
class ListChallengeFragment : Fragment() {

    private var data: String? = null
    private var type_category: String? = null

    private var _binding: FragmentListChallengeBinding? = null
    private val binding get() = _binding!!

    //Components
    private lateinit var mRecyclerView:RecyclerView
    var mGridLayoutManager: GridLayoutManager? = null

    @Inject
    lateinit var navigationRootImpl: NavigationRootImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            data = it.getString(ARG_DATA)
            type_category = it.getString(ARG_TYPE_CATEGORY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListChallengeBinding.inflate(layoutInflater,container, false)
        val view = binding.root
        // Inflate the layout for this fragment
        binding.apply {
            mRecyclerView = recyclerView
        }

        otherComponents()



        return view
    }

    private fun otherComponents() {
        var list =  fromJson< MutableList<DataChallenge>>(data?:"")

        when (list.count()){
            in 0..7 ->{
                mGridLayoutManager = GridLayoutManager(requireContext(), 2)
            }
            else ->{
                mGridLayoutManager = GridLayoutManager(requireContext(), 3)
            }
        }

        val adapterGrid = AdapterGridChallenge(list)

        mRecyclerView.apply {
            layoutManager = mGridLayoutManager
            adapter = adapterGrid
        }

        adapterGrid.onItemClicked { item, bundle ->
            navigationRootImpl.navigateToChallenge(bundle)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(data: String, category: String) =
            ListChallengeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DATA, data)
                    putString(ARG_TYPE_CATEGORY, category)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}