package com.paparazziteam.yakulap.presentation.laboratorio.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.toObject
import com.yakulab.domain.laboratory.DataCategory
import com.yakulab.domain.laboratory.DataItems
import com.paparazziteam.yakulap.presentation.repositorio.LabAnimalsProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ViewModelLab @Inject constructor(
    handle: SavedStateHandle
) : ViewModel() {

    private var mLabProvider = LabAnimalsProvider()

    private val _observableListData = MutableLiveData<List<DataItems>>()
    val observableListData:LiveData<List<DataItems>> get() = _observableListData

    private val _observableCategorias = MutableLiveData<List<String>>()
    val observableCategorias:LiveData<List<String>> get() = _observableCategorias

    private var listCategoriasUnique = mutableListOf<String>()

    private val _loading = MutableLiveData<Boolean>()
    val loading:LiveData<Boolean> = _loading

    init {
        handle.get<String>("typeGroup")?.let {
            Timber.d("typeGroup: $it")
            getData(it)
        }
    }


    private fun getData(type: String?) = viewModelScope.launch {
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
                    _observableCategorias.postValue(listCategoriasUnique)
                    _loading.postValue(false)
                    res?.Categorias.let { _observableListData.postValue(it) }
                }
            }
        }

    }
}