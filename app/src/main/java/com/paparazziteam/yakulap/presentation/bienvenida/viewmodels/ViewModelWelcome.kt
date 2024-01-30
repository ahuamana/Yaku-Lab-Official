package com.paparazziteam.yakulap.presentation.bienvenida.viewmodels

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paparazziteam.yakulab.binding.helper.application.MyPreferences
import com.paparazziteam.yakulap.presentation.bienvenida.model.IntroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ViewModelWelcome @Inject public constructor(
   private val repository: IntroRepository,
   private val myPreferences: MyPreferences
): ViewModel() {

    private val _nextActivity = MutableLiveData<Boolean>()
    val nextActivity :LiveData<Boolean> = _nextActivity

    private val _needToShowDialogDisclosure  = MutableStateFlow(true)
    val needToShowDialogPreferences : StateFlow<Boolean> = _needToShowDialogDisclosure.asStateFlow()


    init {
        getFragments()
        showDialogDisclosure()
    }

    private fun showDialogDisclosure() {
        _needToShowDialogDisclosure.value = myPreferences.needsToShowTutorial
    }



    fun updateTutorialToNeverShowAgain() {
        myPreferences.needsToShowTutorial = false
        _needToShowDialogDisclosure.value = false
    }

    fun onClickStart(){
        _nextActivity.value = true
        _nextActivity.value = false
        updateTutorialToNeverShowAgain()
    }

    fun getFragments(){
        repository.getFragmentsIntro()
    }

    fun showFragments(): MutableLiveData<List<Fragment>> {
      return  repository.showFragments()
    }

}
