package com.paparazziteam.yakulap.modulos.laboratorio.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import com.paparazziteam.yakulap.helper.Constants.CATEGORY_DOMESTIC_ANIMALS
import com.paparazziteam.yakulap.helper.Constants.CATEGORY_INSECTS
import com.paparazziteam.yakulap.helper.Constants.CATEGORY_OTHERS_ANIMALS
import com.paparazziteam.yakulap.modulos.laboratorio.pojo.DataCategory
import com.paparazziteam.yakulap.modulos.laboratorio.pojo.DataChallenge
import com.paparazziteam.yakulap.modulos.laboratorio.pojo.DataItems
import com.paparazziteam.yakulap.modulos.laboratorio.pojo.DataListChallenge
import com.paparazziteam.yakulap.modulos.providers.LabAnimalsProvider

class ViewModelLab private constructor() {

    private var mLabProvider = LabAnimalsProvider()

    private var listCategoriaInsectos: MutableList<DataChallenge>? = mutableListOf()
    private var listAnimalesDomesticos: MutableList<DataChallenge>? = mutableListOf()
    private var listAnimalesOthers: MutableList<DataChallenge>? = mutableListOf()

    private val _observableListData = MutableLiveData<List<DataItems>>()
    val observableListData:LiveData<List<DataItems>> get() = _observableListData

    private val _observableCategorias = MutableLiveData<List<String>>()
    val observableCategorias:LiveData<List<String>> get() = _observableCategorias

    private var listCategoriasUnique = mutableListOf<String>()

    fun getData(type: String?) {
        mLabProvider.geDataProvider(type).addOnCompleteListener { snapshot ->
            if(snapshot.isSuccessful){
                var res = snapshot.result.toObject<DataCategory>()
                res?.Categorias?.forEach { dataItems ->
                    dataItems.Challenge?.distinctBy {
                        it.category
                    }?.map {
                        it.category?.let { catego -> listCategoriasUnique.add(catego) }
                    }
                }
                _observableCategorias.value = listCategoriasUnique
                _observableListData.value = res?.Categorias
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