package com.paparazziteam.yakulap.modulos.laboratorio.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import com.paparazziteam.yakulap.modulos.laboratorio.pojo.DataCategory
import com.paparazziteam.yakulap.modulos.laboratorio.pojo.DataItems
import com.paparazziteam.yakulap.modulos.repositorio.LabAnimalsProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelLab private constructor() {

    private var mLabProvider = LabAnimalsProvider()

    private val _observableListData = MutableLiveData<List<DataItems>>()
    val observableListData:LiveData<List<DataItems>> get() = _observableListData

    private val _observableCategorias = MutableLiveData<List<String>>()
    val observableCategorias:LiveData<List<String>> get() = _observableCategorias

    private var listCategoriasUnique = mutableListOf<String>()

    private val _loading = MutableLiveData<Boolean>()
    val loading:LiveData<Boolean> = _loading

    fun getData(type: String?) {
        _loading.value = true
        CoroutineScope(Dispatchers.Unconfined).launch {
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
                    _loading.value = false
                    res?.Categorias.let { _observableListData.value = it }
                }
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