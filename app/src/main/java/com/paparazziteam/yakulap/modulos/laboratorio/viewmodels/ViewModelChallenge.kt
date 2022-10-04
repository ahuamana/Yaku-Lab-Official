package com.paparazziteam.yakulap.modulos.laboratorio.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.paparazziteam.yakulap.helper.application.MyPreferences
import com.paparazziteam.yakulap.modulos.dashboard.pojo.MoldeChallengeCompleted
import com.paparazziteam.yakulap.modulos.dashboard.model.ChallengeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ViewModelChallenge : ViewModel() {

    @Inject lateinit var mChallengeProvider: ChallengeRepository

    private val _observableChallenge = MutableLiveData<MoldeChallengeCompleted>()
    val observableListData: LiveData< MoldeChallengeCompleted> get() = _observableChallenge

    val myPreferences = MyPreferences()

    fun getChallengeInformation(nameChallenge: String?, onComplete:(isCorrect:Boolean, challenge:MoldeChallengeCompleted?)->Unit){
        CoroutineScope(Dispatchers.Unconfined).launch {
            try {
                withContext(Dispatchers.Main){
                    mChallengeProvider.search(myPreferences.email_login,nameChallenge)?.get()?.addOnCompleteListener { task->
                        if (task.isSuccessful) {
                            if (task.result.size() > 0) {
                                var result = task.result.documents[0].toObject(MoldeChallengeCompleted::class.java)
                                onComplete(true,result)
                            }else{
                                onComplete(false,null)
                            }
                        }

                    }
                }?.addOnFailureListener {
                  FirebaseCrashlytics.getInstance().recordException(it)
                  onComplete(false,null)
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    FirebaseCrashlytics.getInstance().recordException(e)
                    onComplete(false,null)
                }
            }

        }

    }

    /*
    companion object Singleton{
        private var instance: ViewModelChallenge? = null

        fun getInstance(): ViewModelChallenge =
            instance ?: ViewModelChallenge(
                //local y remoto
            ).also {
                instance = it
            }

        fun destroyInstance(){
            instance = null
        }
    }*/
}