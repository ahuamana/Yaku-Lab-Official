package com.paparazziteam.yakulap.presentation.laboratorio.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.crashlytics.FirebaseCrashlytics
import androidx.lifecycle.ViewModel
import com.paparazziteam.yakulab.binding.helper.application.MyPreferences
import com.yakulab.domain.dashboard.ChallengeCompleted
import com.paparazziteam.yakulap.presentation.dashboard.model.ChallengeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ViewModelChallenge @Inject constructor() : ViewModel() {

    @Inject
    lateinit var mChallengeProvider: ChallengeRepository

    private val _observableChallenge = MutableLiveData<ChallengeCompleted>()
    val observableListData: LiveData<ChallengeCompleted> get() = _observableChallenge

    @Inject
    lateinit var myPreferences: MyPreferences

    fun getChallengeInformation(nameChallenge: String?, onComplete:(isCorrect:Boolean, challenge: ChallengeCompleted?)->Unit){
        CoroutineScope(Dispatchers.Unconfined).launch {
            try {
                withContext(Dispatchers.Main){
                    mChallengeProvider.search(myPreferences.email,nameChallenge)?.get()?.addOnCompleteListener { task->
                        if (task.isSuccessful) {
                            if (task.result.size() > 0) {
                                var result = task.result.documents[0].toObject(ChallengeCompleted::class.java)
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
}