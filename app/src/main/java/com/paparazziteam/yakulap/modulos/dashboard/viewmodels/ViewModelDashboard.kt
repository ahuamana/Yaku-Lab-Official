package com.paparazziteam.yakulap.modulos.dashboard.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import com.paparazziteam.yakulap.helper.application.MyPreferences
import com.paparazziteam.yakulap.helper.fromJson
import com.paparazziteam.yakulap.helper.toJson
import com.paparazziteam.yakulap.modulos.dashboard.model.ChallengeRepository
import com.paparazziteam.yakulap.modulos.dashboard.model.CommentRepository
import com.paparazziteam.yakulap.modulos.dashboard.pojo.*
import com.paparazziteam.yakulap.modulos.login.pojo.User
import com.paparazziteam.yakulap.modulos.login.providers.LoginProvider
import com.paparazziteam.yakulap.modulos.login.providers.UserProvider
import com.paparazziteam.yakulap.modulos.repositorio.*
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
    private val mCommentRepositoryImpl: CommentRepository,
    private val mImageProvider:ImageProvider,
    private val mChallengeProvider: ChallengeRepository,
    private val mActionProvider:ReaccionProvider,
    private val mReportProvider:ReportProvider,
    private val mPreferences:MyPreferences
): ViewModel(){

    private var listChallenges = mutableListOf<ChallengeCompleted>()

    private var mListener: ListenerRegistration?= null

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

    private val _challengesCompleted = MutableLiveData<MutableList<ChallengeCompleted>>()
    val challengesCompleted:LiveData<MutableList<ChallengeCompleted>> = _challengesCompleted

    private val _challengesEmpty = MutableLiveData<MutableList<ChallengeCompleted>>()
    val challengesEmpty:LiveData<MutableList<ChallengeCompleted>> = _challengesEmpty

    private val _newPostHided= MutableLiveData<String>()
    val newPostHided:LiveData<String> = _newPostHided

    private val _loading = MutableLiveData<Boolean>()
    val loading:LiveData<Boolean> = _loading

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
        molde: ChallengeCompleted,
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
        molde: ChallengeCompleted,
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

    private fun saveDataOnFirebase(molde: ChallengeCompleted, pointsToGive: Int) {
        mChallengeProvider.create(molde)?.addOnCompleteListener {
            _completeUpload.value = true
            updatePoints(pointsToGive)
        }
    }

    private fun updatePoints(pointsToGive: Int) {
        mUserProvider.updatePoints(mPreferences.email_login,mPreferences.points.plus(pointsToGive))?.
        addOnSuccessListener {
            mPreferences.points+=pointsToGive
        }
    }


    fun showChallengesCompleted(){
        _loading.value = true
        CoroutineScope(Dispatchers.Unconfined).launch {
            mListener = mChallengeProvider.getListChallengesOrderByTimeStamp()!!.addSnapshotListener { value, error ->
                if(value?.isEmpty == true) {
                    showListEmpty()
                }else {
                    //getItems
                    value?.let {
                        filterChallengesFromFirebase(it.documents)
                        filterPostHidden()
                    }
                }

            }
        }
    }

    private fun filterPostHidden(){
        var posts: MutableList<String>
        if(!mPreferences.postBlocked.isNullOrEmpty()){
            try {
                posts = fromJson(mPreferences.postBlocked)
                for(_post in posts){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) removeAboveAndroid7ToMore(listChallenges, _post)
                    else removePostOnAndroid5To7(listChallenges, _post)
                }
                //Notified Challenges completed
                _loading.value = false
                _challengesCompleted.value = listChallenges
            }catch (t:Throwable){
                FirebaseCrashlytics.getInstance().recordException(t)
                //Notified error filtering Hidden post
                _loading.value = false
                _challengesEmpty.value = mutableListOf()
            }
        }else{
            _loading.value = false
            _challengesCompleted.value = listChallenges
        }
    }

    private fun removePostOnAndroid5To7(
        challenges: MutableList<ChallengeCompleted>,
        _post: String) {
        //Init removing
        for(_challenge in challenges){
            if(_challenge.id == _post) challenges.remove(_challenge)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun removeAboveAndroid7ToMore(
        challenges: MutableList<ChallengeCompleted>,
        _post: String
    ) {
        challenges.removeIf{ it.id == _post }
    }



    private fun filterChallengesFromFirebase(documents: MutableList<DocumentSnapshot>) {
        for (productsnaptshot in  documents) {
            val challenge = productsnaptshot.toObject<ChallengeCompleted>()
            if (challenge != null) {
                listChallenges.add(challenge)
            }
        }
        //fill Recycler view
        val challengesUnique = listChallenges.distinctBy {
            it.id
        }
        if(listChallenges.isNotEmpty())  listChallenges.clear()
        listChallenges.addAll(challengesUnique)
    }

    private fun showListEmpty() {
        Log.e("TAG","QUERY VACIO")
        _challengesCompleted.value = mutableListOf()
    }

    fun removeListener(){
        mListener?.remove()
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

    fun updateLikeStatusFirebase(item:ChallengeCompleted){
        mActionProvider.getUserLike(mPreferences.email_login, item.id)?.get()
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

    private fun createLike(item: ChallengeCompleted, like: Boolean) {
        val idEmail= "${item.id.toString()}_${mPreferences.email_login}"
        var mAction = Reaccion()
        mAction.id_email = idEmail
        mAction.status = like
        mAction.email = mPreferences.email_login
        mAction.id = item.id
        mAction.type = "Like"

        mActionProvider.create(mAction)?.addOnCompleteListener {
            if (it.isSuccessful) {
                //likeAnimation(likeImg, R.raw.heart_love,!like)
                //notified animation
            }
        }
    }

    fun reportPost(item: ChallengeCompleted, type:TypeReported, typeReportedPost: TypeReportedPost?= null){
        CoroutineScope(Dispatchers.Unconfined).launch{
            //Report Post Update
            var reportPost = ReportPost(
                idPostReported = item?.id,
                idChallengeReported = item?.challenge_id,
                typeReport = type.value
            )

            if(typeReportedPost!=null){
                reportPost.typeReportedPost = typeReportedPost.value
            }

            mReportProvider.create(reportPost)
            withContext(Dispatchers.Main){
                println("Notified: Publicación reportada.")
                //_snackbar.value = "Publicación reportada."
            }
        }
    }

    fun reportComment(item: Comment, type:TypeReported){
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

    fun searchUserByEmail(email:String){
         CoroutineScope(Dispatchers.Unconfined).launch {
             mUserProvider.searchUserByEmail(email).addOnCompleteListener {

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
                removePostFromList(idChallenge)
                //_newPostHided.value = idChallenge
            }catch (t:Throwable){
                println("Error addPostBlocked: ${t.message}")
            }

        }
    }

    private fun removePostFromList(idChallenge:String){
        var index = listChallenges.indexOfFirst {
            it.id == idChallenge
        }
        if(index<0) return
        listChallenges.removeAt(index)
        _challengesCompleted.value = listChallenges
    }

    override fun onCleared() {
        super.onCleared()
    }
}