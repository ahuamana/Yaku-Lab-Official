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
import com.paparazziteam.yakulab.binding.utils.fromJson
import com.paparazziteam.yakulab.binding.utils.getTimestamp
import com.paparazziteam.yakulab.binding.utils.getTimestampUnix
import com.paparazziteam.yakulab.binding.utils.toJson
import com.yakulab.domain.dashboard.ObservationEntity
import com.paparazziteam.yakulap.presentation.dashboard.model.ChallengeRepository
import com.paparazziteam.yakulap.presentation.dashboard.model.CommentRepository
import com.yakulab.domain.login.User
import com.yakulab.domain.dashboard.ChallengeCompleted
import com.yakulab.domain.dashboard.Comment
import com.yakulab.domain.dashboard.Reaccion
import com.yakulab.domain.dashboard.Report
import com.yakulab.domain.dashboard.TypeReported
import com.yakulab.domain.dashboard.TypeReportedPost
import com.yakulab.usecases.firebase.image.SaveAndDownloadUriUseCase
import com.yakulab.usecases.firebase.login.GetEmailUseCase
import com.yakulab.usecases.firebase.user.SearchUserByEmailUseCase
import com.yakulab.usecases.firebase.user.UpdatePostBlockedUseCase
import com.yakulab.usecases.firebase.user.UpdateUsersBlockedUseCase
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
import timber.log.Timber
import java.io.File
import javax.inject.Inject


@HiltViewModel
class ViewModelDashboard @Inject constructor(
    private val searchUserByEmailUseCase: SearchUserByEmailUseCase,
    private val getEmailUseCase: GetEmailUseCase,
    private val updatePostBlockedUseCase: UpdatePostBlockedUseCase,
    private val updateUsersBlockedUseCase: UpdateUsersBlockedUseCase,
    private val mCommentRepositoryImpl: CommentRepository,
    private val mReportProvider: com.ahuaman.data.dashboard.providers.ReportProvider,
    private val mPreferences: MyPreferences,
): ViewModel(){

    private var listChallenges = mutableListOf<ChallengeCompleted>()

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

    init {
        showUserData()
    }

    private fun showUserData() = viewModelScope.launch{
       val currentEmail = getEmailUseCase.invoke()
        searchUserByEmailUseCase
            .invoke(currentEmail)
            .onEach { user->
                user?.let {
                    saveUsersBlockedInCache(it)
                    savePostsBlockedInCache(it)
                    _user.value = it
                }
            }.catch {
                Timber.d("Error al obtener datos del usuario.")
        }.launchIn(viewModelScope)
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
                _snackbar.value = "Usuario reportado."
            }
        }
    }
    }

    fun reportComment(item: Comment, type: TypeReported) = viewModelScope.launch{
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

    fun addPostBlocked(idChallenge:String) = viewModelScope.launch{
        CoroutineScope(Dispatchers.Unconfined).launch {
            var posts = mutableListOf<String>()
            try {
                if(!mPreferences.savePostsBlocked.isNullOrEmpty()) posts = fromJson(mPreferences.savePostsBlocked)
                posts.add(idChallenge)
                mPreferences.savePostsBlocked = toJson(posts)

                updatePostBlockedUseCase
                    .invoke(mPreferences.email, posts)
                    .onEach {
                        removePostFromList(idChallenge)
                    }.catch {
                        Timber.e("Error al bloquear posts.")
                    }.launchIn(viewModelScope)

            }catch (t:Throwable){
                println("Error addPostBlocked: ${t.message}")
            }
        }
    }

    fun blockUser(email:String) = viewModelScope.launch{
            var users = mutableListOf<String>()
            try {
                if(!mPreferences.saveUsersBlocked.isNullOrEmpty()) users = fromJson(mPreferences.saveUsersBlocked)
                users.add(email)
                mPreferences.saveUsersBlocked = toJson(users)

                updateUsersBlockedUseCase
                    .invoke(mPreferences.email, users)
                    .onEach {
                        removePostByUser(email)
                        withContext(Dispatchers.Main){
                            _snackbar.value = "Usuario bloqueado."
                        }
                    }.catch {
                        Timber.e("Error al bloquear usuario.")
                    }.launchIn(viewModelScope)
            }catch (t:Throwable){
                withContext(Dispatchers.Main){
                    _snackbar.value = "Error al bloquear usuario."
                }
                println("Error addUserBlocked: ${t.message}")
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

    private fun removePostFromList(idChallenge:String) {
        var index = listChallenges.indexOfFirst {
            it.id == idChallenge
        }
        if (index < 0) return
        listChallenges.removeAt(index)
        _postsCompleted.value = listChallenges
    }

}