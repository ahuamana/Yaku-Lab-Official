package com.yakulab.feature.profile;

import java.lang.System;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0019\u001a\u00020\u001aH\u0002J&\u0010\u001b\u001a\u0004\u0018\u00010\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010 2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016J\u001a\u0010#\u001a\u00020\u001a2\u0006\u0010$\u001a\u00020\u001c2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016J\b\u0010%\u001a\u00020\u001aH\u0002J\b\u0010&\u001a\u00020\u001aH\u0002J\b\u0010\'\u001a\u00020\u001aH\u0002R\u001b\u0010\u0003\u001a\u00020\u00048FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\r\u001a\u00020\u000e8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001b\u0010\u0013\u001a\u00020\u00148FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0017\u0010\u0018\u001a\u0004\b\u0015\u0010\u0016\u00a8\u0006("}, d2 = {"Lcom/yakulab/feature/profile/ProfileParentFragment;", "Landroidx/fragment/app/Fragment;", "()V", "binding", "Lcom/yakulab/feature/profile/databinding/FragmentProfileParentBinding;", "getBinding", "()Lcom/yakulab/feature/profile/databinding/FragmentProfileParentBinding;", "binding$delegate", "LFragmentViewBindingDelegate;", "certificationsAdapter", "Lcom/yakulab/feature/profile/CertificationAdapter;", "medalsAdapter", "Lcom/yakulab/feature/profile/MedalsAdapter;", "navigator", "Lcom/paparazziteam/yakulab/binding/helper/navigator/Navigator;", "getNavigator", "()Lcom/paparazziteam/yakulab/binding/helper/navigator/Navigator;", "setNavigator", "(Lcom/paparazziteam/yakulab/binding/helper/navigator/Navigator;)V", "viewModel", "Lcom/yakulab/feature/profile/viewmodels/ProfileParentViewModel;", "getViewModel", "()Lcom/yakulab/feature/profile/viewmodels/ProfileParentViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "observers", "", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "view", "setupButtons", "setupRecyclerMedals", "setupUI", "feature-profile_debug"})
@dagger.hilt.android.AndroidEntryPoint
public final class ProfileParentFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.NotNull
    private final FragmentViewBindingDelegate binding$delegate = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy viewModel$delegate = null;
    private final com.yakulab.feature.profile.MedalsAdapter medalsAdapter = null;
    private final com.yakulab.feature.profile.CertificationAdapter certificationsAdapter = null;
    @javax.inject.Inject
    public com.paparazziteam.yakulab.binding.helper.navigator.Navigator navigator;
    
    public ProfileParentFragment() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.yakulab.feature.profile.databinding.FragmentProfileParentBinding getBinding() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.yakulab.feature.profile.viewmodels.ProfileParentViewModel getViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.paparazziteam.yakulab.binding.helper.navigator.Navigator getNavigator() {
        return null;
    }
    
    public final void setNavigator(@org.jetbrains.annotations.NotNull
    com.paparazziteam.yakulab.binding.helper.navigator.Navigator p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override
    public void onViewCreated(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupButtons() {
    }
    
    private final void setupRecyclerMedals() {
    }
    
    private final void observers() {
    }
    
    private final void setupUI() {
    }
}