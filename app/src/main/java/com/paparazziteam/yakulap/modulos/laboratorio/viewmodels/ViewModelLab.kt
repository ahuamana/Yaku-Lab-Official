package com.paparazziteam.yakulap.modulos.laboratorio.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import com.google.gson.Gson
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

class ViewModelLab private constructor() {

    private var mLabProvider = LabAnimalsProvider()

    private var listCategoriaInsectos: MutableList<DataChallenge>? = mutableListOf()
    private var listAnimalesDomesticos: MutableList<DataChallenge>? = mutableListOf()
    private var listAnimalesOthers: MutableList<DataChallenge>? = mutableListOf()

    private val _observableListData = MutableLiveData<DataListChallenge>()

    val observableListData:LiveData<DataListChallenge> get() = _observableListData


    fun getData(){
        mLabProvider.geDataProvider().addOnCompleteListener { snapshot ->
            if(snapshot.isSuccessful){
                var res = snapshot.result.toObject<DataCategory>()
                res?.Categorias?.forEach { dataItems ->

                    dataItems.Challenge?.forEach {
                        when(it.category){
                            CATEGORY_INSECTS -> {
                                listCategoriaInsectos?.add(it)
                            }
                            CATEGORY_DOMESTIC_ANIMALS -> {
                                listAnimalesDomesticos?.add(it)
                            }
                            CATEGORY_OTHERS_ANIMALS -> {
                                listAnimalesOthers?.add(it)
                            }
                        }
                    }

                }

                var dataAll = DataListChallenge(
                    listInsectos = listCategoriaInsectos,
                    listAnimalsOther = listAnimalesOthers,
                    listAnimalsDomestic = listAnimalesDomesticos
                )
                _observableListData.value = dataAll

                //println("Categorias listCategoriaInsectos: $listCategoriaInsectos")
                //println("Categorias categoryAnimalesDomesticos: $listAnimalesDomesticos")
            }
        }
    }

    companion object Singleton{
        private var instance: ViewModelLab? = null

        fun getInstance(): ViewModelLab =
            instance ?: ViewModelLab(
                //local y remoto
            ).also {
                instance = it
            }

        fun destroyInstance(){
            instance = null
        }
    }
}