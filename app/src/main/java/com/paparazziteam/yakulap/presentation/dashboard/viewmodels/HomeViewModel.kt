package com.paparazziteam.yakulap.presentation.dashboard.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import com.paparazziteam.yakulab.binding.helper.application.MyPreferences
import com.paparazziteam.yakulab.binding.helper.fromJson
import com.paparazziteam.yakulab.binding.helper.getTimestamp
import com.paparazziteam.yakulab.binding.helper.getTimestampUnix
import com.paparazziteam.yakulab.binding.helper.toJson
import com.yakulab.domain.dashboard.ObservationEntity
import com.paparazziteam.yakulap.presentation.dashboard.model.ChallengeRepository
import com.paparazziteam.yakulap.presentation.dashboard.model.CommentRepository
import com.yakulab.domain.login.User
import com.paparazziteam.yakulap.presentation.login.providers.LoginProvider
import com.paparazziteam.yakulap.presentation.login.providers.UserProvider
import com.paparazziteam.yakulap.presentation.repositorio.*
import com.yakulab.domain.dashboard.ChallengeCompleted
import com.yakulab.domain.dashboard.Comment
import com.yakulab.domain.dashboard.Reaccion
import com.yakulab.domain.dashboard.Report
import com.yakulab.domain.dashboard.TypeReported
import com.yakulab.domain.dashboard.TypeReportedPost
import com.yakulab.usecases.inaturalist.GetSpeciesByLocationUseCase
import com.yakulab.usecases.inaturalist.SpeciesByLocationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mUserProvider:UserProvider,
    private val mLoginProvider:LoginProvider,
    private val mCommentRepositoryImpl: CommentRepository,
    private val mImageProvider:ImageProvider,
    private val mChallengeProvider: ChallengeRepository,
    private val mActionProvider:ReaccionProvider,
    private val mReportProvider:ReportProvider,
    private val mPreferences: MyPreferences,
    private val getSpeciesByLocationUseCase: GetSpeciesByLocationUseCase,
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

    private val _postsCompleted = MutableLiveData<MutableList<ChallengeCompleted>>()
    val challengesCompleted:LiveData<MutableList<ChallengeCompleted>> = _postsCompleted

    private val _challengesEmpty = MutableLiveData<MutableList<ChallengeCompleted>>()
    val challengesEmpty:LiveData<MutableList<ChallengeCompleted>> = _challengesEmpty

    private val _newPostHided= MutableLiveData<String>()
    val newPostHided:LiveData<String> = _newPostHided

    private val _loading = MutableLiveData<Boolean>()
    val loading:LiveData<Boolean> = _loading

    private val _speciesByLocation = MutableStateFlow<SpeciesByLocationResult>(
        SpeciesByLocationResult.ShowLoading)
    val speciesByLocation = _speciesByLocation.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000,1),
        initialValue = SpeciesByLocationResult.ShowLoading
    )

    init {
        showUserData()
    }

    fun showUserData(){
       var emailLogged = mLoginProvider.getEmail()
        mUserProvider.searchUserByEmail(emailLogged).addOnCompleteListener {
            if(it.isSuccessful){
                try {
                    var user = it.result?.toObject<User>()
                    user?.let {
                        saveUsersBlockedInCache(it)
                        savePostsBlockedInCache(it)
                        _user.value = it
                    }
                }catch (e:Throwable){
                    FirebaseCrashlytics.getInstance().recordException(e)
                }
            }else{
               //TODO: show error user not found
            }
        }
    }

    private fun savePostsBlockedInCache(user: User) {
        var postsBlocked = user.post_blocked
        if(postsBlocked != null){
            var json = toJson(postsBlocked)
            mPreferences.savePostsBlocked = json
        }else  mPreferences.savePostsBlocked = ""
    }

    private fun saveUsersBlockedInCache(user: User) {
        var usersBlocked = user.users_blocked
        if(!usersBlocked.isNullOrEmpty()){
            mPreferences.saveUsersBlocked = toJson(usersBlocked)
        }else mPreferences.saveUsersBlocked = ""
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
        mUserProvider.updatePoints(mPreferences.email,mPreferences.points.plus(pointsToGive))?.
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
                        removePostsFromUsers()
                        removePostsHidden()
                    }
                }

            }
        }
    }

    private fun removePostsFromUsers() {
        var usersBlocked = mPreferences.saveUsersBlocked
        if(!usersBlocked.isNullOrEmpty()){
            var listUsersBlocked: MutableList<String> = fromJson(usersBlocked)
            listUsersBlocked.forEach { userBlocked ->
                    listChallenges.removeAll { it.author_email == userBlocked }
            }
        }
    }

    private fun removePostsHidden(){
        var posts: MutableList<String>
        if(!mPreferences.savePostsBlocked.isNullOrEmpty()){
            try {
                posts = fromJson(mPreferences.savePostsBlocked)
                for(_post in posts){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) removeAboveAndroid7ToMore(listChallenges, _post)
                    else removePostOnAndroid5To7(listChallenges, _post)
                }
                //Notified Challenges completed
                _loading.value = false
                _postsCompleted.value = listChallenges
            }catch (t:Throwable){
                FirebaseCrashlytics.getInstance().recordException(t)
                //Notified error filtering Hidden post
                _loading.value = false
                _challengesEmpty.value = mutableListOf()
            }
        }else{
            _loading.value = false
            _postsCompleted.value = listChallenges
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
        _postsCompleted.value = mutableListOf()
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

    fun updateLikeStatusFirebase(item: ChallengeCompleted){
        mActionProvider.getUserLike(mPreferences.email, item.id)?.get()
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
        val idEmail= "${item.id.toString()}_${mPreferences.email}"
        var mAction = Reaccion()
        mAction.id_email = idEmail
        mAction.status = like
        mAction.email = mPreferences.email
        mAction.id = item.id
        mAction.type = "Like"

        mActionProvider.create(mAction)?.addOnCompleteListener {
            if (it.isSuccessful) {
                //likeAnimation(likeImg, R.raw.heart_love,!like)
                //notified animation
            }
        }
    }

    fun reportPost(item: ChallengeCompleted, type: TypeReported, typeReportedPost: TypeReportedPost?= null){
        CoroutineScope(Dispatchers.Unconfined).launch{
            //Report Post Update
            var reportPost = Report(
                idPostReported = item?.id,
                idChallengeReported = item?.challenge_id,
                typeReport = type.value,
                emailWhoReport = mPreferences.email,
                firstNameWhoReport = mPreferences.firstName,
                lastNameWhoReport = mPreferences.lastName,
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

    fun reportUser(type: TypeReported, userReported:String){
        CoroutineScope(Dispatchers.Unconfined).launch{
            //Report User Update
            var reportUser = Report(
                typeReport = type.value,
                datetime = getTimestamp(),
                datetimeUnixTime = getTimestampUnix(),
                emailWhoReport = mPreferences.email,
                firstNameWhoReport = mPreferences.firstName,
                lastNameWhoReport = mPreferences.lastName,
            )

            if (userReported == mPreferences.email) {
                return@launch
            }
            if(!userReported.isNullOrEmpty()){
                reportUser.userReported = userReported

            mReportProvider.create(reportUser)
            withContext(Dispatchers.Main){
                println("Notified: Usuario reportado.")
                _snackbar.value = "Usuario reportado."
            }
        }
    }
    }

    fun reportComment(item: Comment, type: TypeReported){
        CoroutineScope(Dispatchers.Unconfined).launch{
            //Report Post Update
            var reportPost = Report(
                idCommentReported = item?.id,
                reportedComentario = item?.message,
                idPhotoReported = item?.id_photo,
                typeReport = type.value,
                emailWhoReport = mPreferences.email,
                firstNameWhoReport = mPreferences.firstName,
                lastNameWhoReport = mPreferences.lastName,
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
                if(!mPreferences.savePostsBlocked.isNullOrEmpty()) posts = fromJson(mPreferences.savePostsBlocked)
                posts.add(idChallenge)
                mPreferences.savePostsBlocked = toJson(posts)
                mUserProvider.updatePostBlocked(mPreferences.email, posts)
                removePostFromList(idChallenge)
                //_newPostHided.value = idChallenge
            }catch (t:Throwable){
                println("Error addPostBlocked: ${t.message}")
            }
        }
    }

    fun blockUser(email:String){
        CoroutineScope(Dispatchers.Unconfined).launch {
            var users = mutableListOf<String>()
            try {
                if(!mPreferences.saveUsersBlocked.isNullOrEmpty()) users = fromJson(mPreferences.saveUsersBlocked)
                users.add(email)
                mPreferences.saveUsersBlocked = toJson(users)
                mUserProvider.updateUsersBlocked(mPreferences.email, users)
                removePostByUser(email)
            }catch (t:Throwable){
                println("Error addUserBlocked: ${t.message}")
            }
        }
    }

    private fun removePostByUser(email: String) {
        CoroutineScope(Dispatchers.Unconfined).launch {
            try {
                if(listChallenges.isEmpty()) return@launch
                listChallenges.removeAll{
                    it.author_email == email
                }
                _postsCompleted.value = listChallenges
            }catch (t:Throwable){
                println("Error removePostByUser: ${t.message}")
            }
        }
    }

    private fun removePostFromList(idChallenge:String){
        var index = listChallenges.indexOfFirst {
            it.id == idChallenge
        }
        if(index<0) return
        listChallenges.removeAt(index)
        _postsCompleted.value = listChallenges
    }

    fun getSpeciesByLocation(
        lat : Double,
        lng : Double,
    ) = viewModelScope.launch(Dispatchers.IO) {

        getSpeciesByLocationUseCase
            .invoke(lat, lng)
            .onStart {
                withContext(Dispatchers.Main){
                    _speciesByLocation.value = SpeciesByLocationResult.ShowLoading
                }
            }.onEach {
                withContext(Dispatchers.Main){
                    if (it.isNotEmpty()) {
                        val listFiltered = handledSpeciesRemoveIfIdentificationsIsEmpty(it)
                        _speciesByLocation.value = SpeciesByLocationResult.Success(listFiltered)
                    } else _speciesByLocation.value = SpeciesByLocationResult.Empty
                }
            }.catch {
                withContext(Dispatchers.Main){
                    _speciesByLocation.value = SpeciesByLocationResult.Error(it.message?:"")
                }
            }.onCompletion {
                withContext(Dispatchers.Main){
                    _speciesByLocation.value = SpeciesByLocationResult.HideLoading
                }
            }.launchIn(viewModelScope)
    }

    fun handledSpeciesRemoveIfIdentificationsIsEmpty(list:List<ObservationEntity>): List<ObservationEntity>{
        return list.filter {
            it.identifications.isNotEmpty()
        }
    }

}