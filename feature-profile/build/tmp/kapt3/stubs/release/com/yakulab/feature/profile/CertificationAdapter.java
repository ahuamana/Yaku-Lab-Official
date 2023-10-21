package com.yakulab.feature.profile;

import java.lang.System;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 \u00132\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001:\u0002\u0013\u0014B\u0005\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u0007H\u0016J\u0018\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0007H\u0016J/\u0010\u0005\u001a\u00020\u000b2\'\u0010\u0012\u001a#\u0012\u0004\u0012\u00020\u0002\u0012\u0013\u0012\u00110\u0007\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0004\u0012\u00020\u000b0\u0006R1\u0010\u0005\u001a%\u0012\u0004\u0012\u00020\u0002\u0012\u0013\u0012\u00110\u0007\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/yakulab/feature/profile/CertificationAdapter;", "Landroidx/recyclerview/widget/ListAdapter;", "Lcom/yakulab/domain/profile/CertificationsDomain;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "()V", "onItemClickListener", "Lkotlin/Function2;", "", "Lkotlin/ParameterName;", "name", "position", "", "onBindViewHolder", "holder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "listener", "Companion", "ViewHolder", "feature-profile_release"})
public final class CertificationAdapter extends androidx.recyclerview.widget.ListAdapter<com.yakulab.domain.profile.CertificationsDomain, androidx.recyclerview.widget.RecyclerView.ViewHolder> {
    @org.jetbrains.annotations.NotNull
    public static final com.yakulab.feature.profile.CertificationAdapter.Companion Companion = null;
    private kotlin.jvm.functions.Function2<? super com.yakulab.domain.profile.CertificationsDomain, ? super java.lang.Integer, kotlin.Unit> onItemClickListener;
    
    public CertificationAdapter() {
        super(null);
    }
    
    public final void onItemClickListener(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super com.yakulab.domain.profile.CertificationsDomain, ? super java.lang.Integer, kotlin.Unit> listener) {
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public androidx.recyclerview.widget.RecyclerView.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    androidx.recyclerview.widget.RecyclerView.ViewHolder holder, int position) {
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/yakulab/feature/profile/CertificationAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/yakulab/feature/profile/databinding/ItemCertificationBinding;", "(Lcom/yakulab/feature/profile/CertificationAdapter;Lcom/yakulab/feature/profile/databinding/ItemCertificationBinding;)V", "bind", "", "item", "Lcom/yakulab/domain/profile/CertificationsDomain;", "feature-profile_release"})
    public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        private final com.yakulab.feature.profile.databinding.ItemCertificationBinding binding = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull
        com.yakulab.feature.profile.databinding.ItemCertificationBinding binding) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull
        com.yakulab.domain.profile.CertificationsDomain item) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001:\u0001\u0003B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0004"}, d2 = {"Lcom/yakulab/feature/profile/CertificationAdapter$Companion;", "", "()V", "CertificationDiffCallback", "feature-profile_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016J\u0018\u0010\b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016\u00a8\u0006\t"}, d2 = {"Lcom/yakulab/feature/profile/CertificationAdapter$Companion$CertificationDiffCallback;", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/yakulab/domain/profile/CertificationsDomain;", "()V", "areContentsTheSame", "", "oldItem", "newItem", "areItemsTheSame", "feature-profile_release"})
        static final class CertificationDiffCallback extends androidx.recyclerview.widget.DiffUtil.ItemCallback<com.yakulab.domain.profile.CertificationsDomain> {
            
            public CertificationDiffCallback() {
                super();
            }
            
            @java.lang.Override
            public boolean areItemsTheSame(@org.jetbrains.annotations.NotNull
            com.yakulab.domain.profile.CertificationsDomain oldItem, @org.jetbrains.annotations.NotNull
            com.yakulab.domain.profile.CertificationsDomain newItem) {
                return false;
            }
            
            @java.lang.Override
            public boolean areContentsTheSame(@org.jetbrains.annotations.NotNull
            com.yakulab.domain.profile.CertificationsDomain oldItem, @org.jetbrains.annotations.NotNull
            com.yakulab.domain.profile.CertificationsDomain newItem) {
                return false;
            }
        }
    }
}