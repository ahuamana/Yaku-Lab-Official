package com.yakulab.feature.profile.viewmodels;

import java.lang.System;

@dagger.hilt.android.lifecycle.HiltViewModel
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B7\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u00a2\u0006\u0002\u0010\u000eJ\b\u0010\u0019\u001a\u00020 H\u0002J\b\u0010\u001c\u001a\u00020 H\u0002J\u0010\u0010!\u001a\u00020 2\u0006\u0010\"\u001a\u00020\u0016H\u0002J\u0006\u0010#\u001a\u00020 R\u001a\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00110\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u00110\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00110\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u00110\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001aR\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00160\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001f\u00a8\u0006$"}, d2 = {"Lcom/yakulab/feature/profile/viewmodels/ProfileParentViewModel;", "Landroidx/lifecycle/ViewModel;", "getEmailLoggedUseCase", "Lcom/yakulab/usecases/firebase/getEmailLoggedUseCase;", "getUserInfoUseCase", "Lcom/yakulab/usecases/firebase/getUserInfoUseCase;", "getMedalsUseCase", "Lcom/yakulab/usecases/yakulab/GetMedalsUseCase;", "getCertificationsUseCase", "Lcom/yakulab/usecases/yakulab/GetCertificationsUseCase;", "logoutUseCase", "Lcom/yakulab/usecases/yakulab/LogoutUseCase;", "myPreferences", "Lcom/paparazziteam/yakulab/binding/helper/application/MyPreferences;", "(Lcom/yakulab/usecases/firebase/getEmailLoggedUseCase;Lcom/yakulab/usecases/firebase/getUserInfoUseCase;Lcom/yakulab/usecases/yakulab/GetMedalsUseCase;Lcom/yakulab/usecases/yakulab/GetCertificationsUseCase;Lcom/yakulab/usecases/yakulab/LogoutUseCase;Lcom/paparazziteam/yakulab/binding/helper/application/MyPreferences;)V", "_certifications", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/yakulab/domain/profile/CertificationsDomain;", "_medals", "Lcom/yakulab/domain/profile/MedalsDomain;", "_user", "", "certifications", "Lkotlinx/coroutines/flow/StateFlow;", "getCertifications", "()Lkotlinx/coroutines/flow/StateFlow;", "medals", "getMedals", "user", "getUser", "()Lkotlinx/coroutines/flow/MutableStateFlow;", "Lkotlinx/coroutines/Job;", "getUserInfo", "email", "logout", "feature-profile_release"})
public final class ProfileParentViewModel extends androidx.lifecycle.ViewModel {
    private final com.yakulab.usecases.firebase.getEmailLoggedUseCase getEmailLoggedUseCase = null;
    private final com.yakulab.usecases.firebase.getUserInfoUseCase getUserInfoUseCase = null;
    private final com.yakulab.usecases.yakulab.GetMedalsUseCase getMedalsUseCase = null;
    private final com.yakulab.usecases.yakulab.GetCertificationsUseCase getCertificationsUseCase = null;
    private final com.yakulab.usecases.yakulab.LogoutUseCase logoutUseCase = null;
    private final com.paparazziteam.yakulab.binding.helper.application.MyPreferences myPreferences = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _user = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> user = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.yakulab.domain.profile.MedalsDomain>> _medals = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.yakulab.domain.profile.MedalsDomain>> medals = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.yakulab.domain.profile.CertificationsDomain>> _certifications = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.yakulab.domain.profile.CertificationsDomain>> certifications = null;
    
    @javax.inject.Inject
    public ProfileParentViewModel(@org.jetbrains.annotations.NotNull
    com.yakulab.usecases.firebase.getEmailLoggedUseCase getEmailLoggedUseCase, @org.jetbrains.annotations.NotNull
    com.yakulab.usecases.firebase.getUserInfoUseCase getUserInfoUseCase, @org.jetbrains.annotations.NotNull
    com.yakulab.usecases.yakulab.GetMedalsUseCase getMedalsUseCase, @org.jetbrains.annotations.NotNull
    com.yakulab.usecases.yakulab.GetCertificationsUseCase getCertificationsUseCase, @org.jetbrains.annotations.NotNull
    com.yakulab.usecases.yakulab.LogoutUseCase logoutUseCase, @org.jetbrains.annotations.NotNull
    com.paparazziteam.yakulab.binding.helper.application.MyPreferences myPreferences) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> getUser() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.yakulab.domain.profile.MedalsDomain>> getMedals() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.yakulab.domain.profile.CertificationsDomain>> getCertifications() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job logout() {
        return null;
    }
    
    private final kotlinx.coroutines.Job getUserInfo(java.lang.String email) {
        return null;
    }
    
    private final kotlinx.coroutines.Job getMedals() {
        return null;
    }
    
    private final kotlinx.coroutines.Job getCertifications() {
        return null;
    }
}