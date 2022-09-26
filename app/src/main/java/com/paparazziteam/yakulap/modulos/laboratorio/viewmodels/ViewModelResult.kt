package com.paparazziteam.yakulap.modulos.laboratorio.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.toObject
import com.google.gson.Gson
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.helper.Constants.CATEGORY_DOMESTIC_ANIMALS
import com.paparazziteam.yakulap.helper.Constants.CATEGORY_INSECTS
import com.paparazziteam.yakulap.helper.Constants.CATEGORY_OTHERS_ANIMALS
import com.paparazziteam.yakulap.helper.observer.Observable
import com.paparazziteam.yakulap.modulos.dashboard.viewmodels.ViewModelDashboard
import com.paparazziteam.yakulap.modulos.laboratorio.pojo.DataCategory
import com.paparazziteam.yakulap.modulos.laboratorio.pojo.DataChallenge
import com.paparazziteam.yakulap.modulos.laboratorio.pojo.DataItems
import com.paparazziteam.yakulap.modulos.laboratorio.pojo.DataListChallenge
import com.paparazziteam.yakulap.modulos.login.providers.UserProvider
import com.paparazziteam.yakulap.modulos.providers.LabAnimalsProvider
import com.paparazziteam.yakulap.modulos.providers.ResourcesProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.reflect.Constructor
import javax.inject.Inject
import javax.inject.Singleton


@HiltViewModel
class ViewModelResult @Inject constructor(private val resoursesProvider:ResourcesProvider):ViewModel() {

    private var mLabProvider = LabAnimalsProvider()

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