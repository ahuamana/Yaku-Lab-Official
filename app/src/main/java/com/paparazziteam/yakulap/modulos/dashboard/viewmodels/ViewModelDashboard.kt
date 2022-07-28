package com.paparazziteam.yakulap.modulos.dashboard.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import com.paparazziteam.yakulap.modulos.dashboard.pojo.MoldeChallengeCompleted
import com.paparazziteam.yakulap.modulos.login.pojo.User
import com.paparazziteam.yakulap.modulos.login.providers.LoginProvider
import com.paparazziteam.yakulap.modulos.login.providers.UserProvider
import com.paparazziteam.yakulap.modulos.repositorio.ChallengeProvider

class ViewModelDashboard {

    private var mUserProvider = UserProvider()
    private var mLoginProvider = LoginProvider()
    private var mChallengeProvider = ChallengeProvider()
    private var challengesCompleted = mutableListOf<MoldeChallengeCompleted>()
    private lateinit var mListener: ListenerRegistration


    private val _user = MutableLiveData<User>()
    private val _challengesCompleted= MutableLiveData<MutableList<MoldeChallengeCompleted>>()

    fun getUserData():LiveData<User> = _user
    fun getChallengeCompletedData():LiveData<MutableList<MoldeChallengeCompleted>> = _challengesCompleted

    fun showUserData(){
       var emailLogged = mLoginProvider.getEmail()
        mUserProvider.searchUserByEmail(emailLogged).addOnCompleteListener {
            if(it.isSuccessful){
                _user.value = it.result.toObject(User::class.java)
            }else{
                //error al consultar los puntos

            }
        }
    }


    fun showChallengesCompleted(){
            mListener = mChallengeProvider.getListChallengesOrderByTimeStamp()!!.addSnapshotListener { value, error ->

                if(value?.isEmpty == true)
                {
                    android.util.Log.e("TAG","QUERY VACIO")
                }else
                {
                    for (productsnaptshot in  value!!.documents)
                    {
                        val challenge = productsnaptshot.toObject<MoldeChallengeCompleted>()
                        if (challenge != null) {
                            challengesCompleted.add(challenge)
                        }
                    }
                    //fill Recycler view
                    _challengesCompleted.value = challengesCompleted
                }

            }

    }



    companion object Singleton{
        private var instance: ViewModelDashboard? = null

        fun getInstance(): ViewModelDashboard =
            instance ?: ViewModelDashboard(
                //local y remoto
            ).also {
                instance = it
            }

        fun destroyInstance(){
            instance = null
        }
    }
}