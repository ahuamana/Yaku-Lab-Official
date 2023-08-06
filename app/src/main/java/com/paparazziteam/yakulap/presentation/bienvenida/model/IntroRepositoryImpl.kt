package com.paparazziteam.yakulap.presentation.bienvenida.model

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.paparazziteam.yakulab.binding.helper.application
import com.paparazziteam.yakulap.R
import com.paparazziteam.yakulap.presentation.bienvenida.fragments.IntroFragment
import javax.inject.Inject

class IntroRepositoryImpl @Inject constructor(
    application: Application
):IntroRepository {

    var _fList = arrayListOf<Fragment>()
    var fList = MutableLiveData<List<Fragment>>()

    override fun getFragmentsIntro() {

        _fList.add(
            IntroFragment().newInstance(
                R.drawable.intro_primera,
                application.getString(R.string.intro_primera),
                application.getString(R.string.intro_desc_primera)))

        _fList.add(
            IntroFragment().newInstance(
                R.drawable.intro_segunda,
                application.getString(R.string.intro_segunda),
                application.getString(R.string.intro_desc_segunda)))

        _fList.add(
            IntroFragment().newInstance(
                R.drawable.intro_tercera,
                application.getString(R.string.intro_tercera),
                application.getString(R.string.intro_desc_tercera)))
        fList.value = _fList
    }

    override fun showFragments():MutableLiveData<List<Fragment>> {
        return fList
    }

}