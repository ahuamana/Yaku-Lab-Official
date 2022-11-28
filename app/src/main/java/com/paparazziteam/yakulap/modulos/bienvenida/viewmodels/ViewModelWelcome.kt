package com.paparazziteam.yakulap.modulos.bienvenida.viewmodels

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paparazziteam.yakulap.modulos.bienvenida.model.IntroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModelWelcome @Inject public constructor(
   private val repository: IntroRepository
): ViewModel() {

    private val _nextActivity = MutableLiveData<Boolean>()
    val nextActivity :LiveData<Boolean> = _nextActivity

    init {
        getFragments()
    }

    fun onClickStart(){
        _nextActivity.value = true
        _nextActivity.value = false
    }

    fun getFragments(){
        repository.getFragmentsIntro()
    }

    fun showFragments(): MutableLiveData<List<Fragment>> {
      return  repository.showFragments()
    }

}
