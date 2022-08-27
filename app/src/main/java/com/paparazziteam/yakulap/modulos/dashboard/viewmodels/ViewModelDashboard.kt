package com.paparazziteam.yakulap.modulos.dashboard.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import com.paparazziteam.yakulap.modulos.dashboard.pojo.Comment
import com.paparazziteam.yakulap.modulos.dashboard.pojo.MoldeChallengeCompleted
import com.paparazziteam.yakulap.modulos.login.pojo.User
import com.paparazziteam.yakulap.modulos.login.providers.LoginProvider
import com.paparazziteam.yakulap.modulos.login.providers.UserProvider
import com.paparazziteam.yakulap.modulos.providers.ChallengeProvider
import com.paparazziteam.yakulap.modulos.providers.CommentProvider

class ViewModelDashboard private constructor(){

    private var mUserProvider = UserProvider()
    private var mLoginProvider = LoginProvider()
    private var mCommentProvider = CommentProvider()
    private var mChallengeProvider = ChallengeProvider()
    private var challengesCompleted = mutableListOf<MoldeChallengeCompleted>()

    private lateinit var mListener: ListenerRegistration


    private val _user = MutableLiveData<User>()
    private val _challengesCompleted= MutableLiveData<MutableList<MoldeChallengeCompleted>>()
    private val _commentsCompleted= MutableLiveData<MutableList<Comment>>()
    private val _emptyComments = MutableLiveData<Boolean>()

    fun getUserData():LiveData<User> = _user
    fun getChallengeCompletedData():LiveData<MutableList<MoldeChallengeCompleted>> = _challengesCompleted
    fun getCommentsCompletedData():LiveData<MutableList<Comment>> = _commentsCompleted
    fun getCommentsEmpty():LiveData<Boolean> = _emptyComments

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

                if(value?.isEmpty == true) {
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

    fun getCommentsFromThread(idPhoto:String){
        mCommentProvider.getCommentsByIdPhoto(idPhoto)?.addSnapshotListener { value, error ->
            Log.e("SIZE BOTTOM", "SIZE: " + value!!.size())
            var commentsList = mutableListOf<Comment>()

            if(value?.isEmpty == true){
                _emptyComments.value = true
                android.util.Log.e("TAG","comentarios VACIO")
            }else{
                for (comment in  value!!.documents) {
                    var commentObj =comment.toObject<Comment>()
                    if(commentObj!=null){
                        commentsList.add(commentObj)
                    }
                }
                println("Comentarios Totales = ${commentsList.count()}")
                _commentsCompleted.value = commentsList
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