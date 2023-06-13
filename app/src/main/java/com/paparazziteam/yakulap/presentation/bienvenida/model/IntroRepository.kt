package com.paparazziteam.yakulap.presentation.bienvenida.model

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData


interface IntroRepository {
    fun getFragmentsIntro()
    fun showFragments(): MutableLiveData<List<Fragment>>
}