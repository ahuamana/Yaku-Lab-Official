package com.paparazziteam.yakulap.modulos.dashboard.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import com.paparazziteam.yakulap.helper.application.MyPreferences
import com.paparazziteam.yakulap.modulos.dashboard.pojo.Comment
import com.paparazziteam.yakulap.modulos.dashboard.pojo.MoldeChallengeCompleted
import com.paparazziteam.yakulap.modulos.login.pojo.User
import com.paparazziteam.yakulap.modulos.login.providers.LoginProvider
import com.paparazziteam.yakulap.modulos.login.providers.UserProvider
import com.paparazziteam.yakulap.modulos.providers.ChallengeProvider
import com.paparazziteam.yakulap.modulos.providers.CommentProvider
import com.paparazziteam.yakulap.modulos.providers.ImageProvider
import java.io.File

class ViewModelDashboard private constructor(){

    private var mUserProvider = UserProvider()
    private var mLoginProvider = LoginProvider()
    private var mCommentProvider = CommentProvider()
    private var mImageProvider = ImageProvider()
    private var mChallengeProvider = ChallengeProvider()
    private var challengesCompleted = mutableListOf<MoldeChallengeCompleted>()

    private lateinit var mListener: ListenerRegistration

    private val preferences = MyPreferences()


    private val _user = MutableLiveData<User>()
    private val _challengesCompleted= MutableLiveData<MutableList<MoldeChallengeCompleted>>()
    private val _commentsCompleted= MutableLiveData<MutableList<Comment>>()
    private val _emptyComments = MutableLiveData<Boolean>()

    private val _errorUpload = MutableLiveData<String>()
    val errorUpload:LiveData<String> = _errorUpload

    private val _completeUpload = MutableLiveData<Boolean>()
    val completeUpload:LiveData<Boolean> = _completeUpload

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

    fun uploadPhotoRemote(
        molde: MoldeChallengeCompleted,
        fileImage: File?,
        pointsToGive: Int,
        isCompleted: Boolean,
        idChallengeDocument: String
    ){
        var idChallengeCreated = mChallengeProvider.createDocument()?.id
        molde.id = idChallengeCreated

        if (fileImage != null) {
            mImageProvider.save(fileImage)?.addOnCompleteListener {
                if(it.isSuccessful){
                    mImageProvider.getDownloadUri()?.addOnSuccessListener {
                        Log.e("URL Upload", "url: $it")
                        molde.url = it.toString()
                        if(isCompleted) updatePhoto(molde,idChallengeDocument)
                        else saveDataOnFirebase(molde,pointsToGive)
                        
                    }?.addOnFailureListener {
                        println("Error imagen addOnFailureListener")
                    }
                }else{
                   // dissmiss dialog
                    println("Error imagen")
                    _errorUpload.value = "La imagen no se pudo almacenar, inténtelo de nuevo!"
                }
            }
        }else{
            //dissmiss
            _errorUpload.value = "La imagen se encuentra vácio, inténtelo de nuevo!"
        }
    }

    private fun updatePhoto(
        molde: MoldeChallengeCompleted,
        idChallengeDocument: String
    ) {
            mChallengeProvider.update(idChallengeDocument,molde.url)?.addOnCompleteListener { task->
                if(task.isSuccessful){
                    _completeUpload.value = true
                }else{
                    _errorUpload.value = "No se pudo actualizar la nueva imagen"
                }
            }
    }

    private fun saveDataOnFirebase(molde: MoldeChallengeCompleted, pointsToGive: Int) {
        mChallengeProvider.create(molde)?.addOnCompleteListener {
            _completeUpload.value = true
            updatePoints(pointsToGive)
        }
    }

    private fun updatePoints(pointsToGive: Int) {
        mUserProvider.updatePoints(MyPreferences().email_login, MyPreferences().
        points.plus(pointsToGive))?.
        addOnSuccessListener {
                MyPreferences().points+=pointsToGive
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