package com.paparazziteam.yakulap.presentation.laboratorio.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paparazziteam.yakulap.R
import com.yakulab.domain.laboratory.DataChallenge
import com.yakulab.domain.laboratory.DataListChallenge
import com.ahuaman.data.dashboard.providers.LabAnimalsProvider
import com.ahuaman.data.dashboard.providers.ResourcesProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ViewModelResult @Inject constructor(
    private val resoursesProvider: ResourcesProvider,
    private val mLabProvider: LabAnimalsProvider
):ViewModel() {


    private var listCategoriaInsectos: MutableList<DataChallenge>? = mutableListOf()
    private var listAnimalesDomesticos: MutableList<DataChallenge>? = mutableListOf()

    private var _congratulations = MutableLiveData<String>()
    val congratulations:LiveData<String> get() = _congratulations

    private val _observableListData = MutableLiveData<DataListChallenge>()
    val observableListData:LiveData<DataListChallenge> get() = _observableListData

    init {

    }

    fun setCongratulations(){
        _congratulations.value = resoursesProvider.getString(R.string.congratulations)
    }




}