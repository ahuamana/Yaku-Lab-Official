package com.paparazziteam.yakulap.modulos.dashboard.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import com.paparazziteam.yakulap.helper.application.MyPreferences
import com.paparazziteam.yakulap.helper.fromJson
import com.paparazziteam.yakulap.helper.toJson
import com.paparazziteam.yakulap.modulos.dashboard.model.ChallengeRepository
import com.paparazziteam.yakulap.modulos.dashboard.model.CommentRepository
import com.paparazziteam.yakulap.modulos.dashboard.model.CommentRepositoryImpl
import com.paparazziteam.yakulap.modulos.dashboard.pojo.*
import com.paparazziteam.yakulap.modulos.login.pojo.User
import com.paparazziteam.yakulap.modulos.login.providers.LoginProvider
import com.paparazziteam.yakulap.modulos.login.providers.UserProvider
import com.paparazziteam.yakulap.modulos.providers.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject


@HiltViewModel
class ViewModelDashboard @Inject constructor(
    private val mUserProvider:UserProvider,
    private val mLoginProvider:LoginProvider,
    val mCommentRepositoryImpl: CommentRepository,
    private val mImageProvider:ImageProvider,
    private val mChallengeProvider: ChallengeRepository,
    private val mActionProvider:ReaccionProvider,
    private val mReportProvider:ReportProvider,
    private val mPreferences:MyPreferences
): ViewModel(){

    private var challengesCompleted = mutableListOf<MoldeChallengeCompleted>()

    private lateinit var mListener: ListenerRegistration

    private val preferences = MyPreferences()

    private val _errorUpload = MutableLiveData<String>()
    val errorUpload:LiveData<String> = _errorUpload

    private val _completeUpload = MutableLiveData<Boolean>()
    val completeUpload:LiveData<Boolean> = _completeUpload

    private val _snackbar = MutableLiveData<String>()
    val snackbar:LiveData<String> = _snackbar

    private val _emptyComments = MutableLiveData<Boolean>()
    val emptyComments:LiveData<Boolean> = _emptyComments

    private val _commentsCompleted= MutableLiveData<MutableList<Comment>>()
    val commentsCompleted:LiveData<MutableList<Comment>> = _commentsCompleted

    private val _user = MutableLiveData<User>()
    fun getUserData():LiveData<User> = _user

    private val _challengesCompleted= MutableLiveData<MutableList<MoldeChallengeCompleted>>()
    val challengesCompletedObservable:LiveData<MutableList<MoldeChallengeCompleted>> = _challengesCompleted

    private val _newPostBlocked= MutableLiveData<String>()
    val newPostBlocked:LiveData<String> = _newPostBlocked

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
        mCommentRepositoryImpl.getCommentsByIdPhoto(idPhoto)?.addSnapshotListener { value, error ->
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

    fun updateLikeStatusFirebase(item:MoldeChallengeCompleted){
        mActionProvider.getUserLike(preferences.email_login, item.id)?.get()
            ?.addOnSuccessListener {
                if(it.isEmpty){
                    println("Like is empty")
                    createLike(item,true)
                }else{
                    //if already exist
                    val status  = it.documents[0].get("status").toString().toBoolean()
                    val idEmail = it.documents[0].get("id_email").toString()
                    println("Update like: $status, $idEmail")
                    updateLike(idEmail, status)
                }
            }
    }

    private fun updateLike(idEmail: String, status: Boolean) {
        mActionProvider.updateStatus(idEmail, !status)?.addOnCompleteListener {
            if(it.isSuccessful){
                //likeAnimation(likeImg, R.raw.heart_love,!like)
                //notified animation
            }
        }
    }

    private fun createLike(item: MoldeChallengeCompleted, like: Boolean) {
        val idEmail= "${item.id.toString()}_${preferences.email_login}"
        var mAction = Reaccion()
        mAction.id_email = idEmail
        mAction.status = like
        mAction.email = preferences.email_login
        mAction.id = item.id
        mAction.type = "Like"

        mActionProvider.create(mAction)?.addOnCompleteListener {
            if (it.isSuccessful) {
                //likeAnimation(likeImg, R.raw.heart_love,!like)
                //notified animation
            }
        }
    }

    fun reportContent(item: MoldeChallengeCompleted, type:TypeReport){
        CoroutineScope(Dispatchers.Unconfined).launch{
            //Report Post Update
            var reportPost = ReportPost(
                idPostReported = item?.id,
                idChallengeReported = item?.challenge_id,
                typeReport = type.value
            )

            mReportProvider.create(reportPost)
            withContext(Dispatchers.Main){
                println("Notified: Publicación reportada.")
                _snackbar.value = "Publicación reportada."
            }
        }
    }

    fun reportComment(item: Comment, type:TypeReport){
        CoroutineScope(Dispatchers.Unconfined).launch{
            //Report Post Update
            var reportPost = ReportPost(
                idCommentReported = item?.id,
                reportedComentario = item?.message,
                idPhotoReported = item?.id_photo,
                typeReport = type.value
            )

            mReportProvider.create(reportPost)
            withContext(Dispatchers.Main){
                _snackbar.value = "Comentario reportado."
            }
        }
    }


    fun addPostBlocked(idChallenge:String){
        CoroutineScope(Dispatchers.Unconfined).launch {
            var posts = mutableListOf<String>()
            try {
                if(!mPreferences.postBlocked.isNullOrEmpty()) posts = fromJson(mPreferences.postBlocked)
                posts.add(idChallenge)
                mPreferences.postBlocked = toJson(posts)
                mUserProvider.updatePostBlocked(mPreferences.email_login, posts)
                withContext(Dispatchers.Main) {
                    println("Notified: Post blocked.")
                    _newPostBlocked.value = idChallenge
                }
            }catch (t:Throwable){
                println("Error addPostBlocked: ${t.message}")
            }

        }
    }


    /*
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
    }*/
}