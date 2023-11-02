package com.yakulab.feature.profile;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 \u00132\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001:\u0002\u0013\u0014B\u0005\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u0007H\u0016J\u0018\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0007H\u0016J/\u0010\u0005\u001a\u00020\u000b2\'\u0010\u0012\u001a#\u0012\u0004\u0012\u00020\u0002\u0012\u0013\u0012\u00110\u0007\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0004\u0012\u00020\u000b0\u0006R1\u0010\u0005\u001a%\u0012\u0004\u0012\u00020\u0002\u0012\u0013\u0012\u00110\u0007\u00a2\u0006\f\b\b\u0012\b\b\t\u0012\u0004\b\b(\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/yakulab/feature/profile/MedalsAdapter;", "Landroidx/recyclerview/widget/ListAdapter;", "Lcom/yakulab/domain/profile/MedalsDomain;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "()V", "onItemClickListener", "Lkotlin/Function2;", "", "Lkotlin/ParameterName;", "name", "position", "", "onBindViewHolder", "holder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "listener", "Companion", "ViewHolder", "feature-profile_debug"})
public final class MedalsAdapter extends androidx.recyclerview.widget.ListAdapter<com.yakulab.domain.profile.MedalsDomain, androidx.recyclerview.widget.RecyclerView.ViewHolder> {
    @org.jetbrains.annotations.Nullable
    private kotlin.jvm.functions.Function2<? super com.yakulab.domain.profile.MedalsDomain, ? super java.lang.Integer, kotlin.Unit> onItemClickListener;
    @org.jetbrains.annotations.NotNull
    public static final com.yakulab.feature.profile.MedalsAdapter.Companion Companion = null;
    
    public MedalsAdapter() {
        super(null);
    }
    
    public final void onItemClickListener(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super com.yakulab.domain.profile.MedalsDomain, ? super java.lang.Integer, kotlin.Unit> listener) {
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public androidx.recyclerview.widget.RecyclerView.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    androidx.recyclerview.widget.RecyclerView.ViewHolder holder, int position) {
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001:\u0001\u0003B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0004"}, d2 = {"Lcom/yakulab/feature/profile/MedalsAdapter$Companion;", "", "()V", "MedalsDiffCallback", "feature-profile_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016J\u0018\u0010\b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016\u00a8\u0006\t"}, d2 = {"Lcom/yakulab/feature/profile/MedalsAdapter$Companion$MedalsDiffCallback;", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/yakulab/domain/profile/MedalsDomain;", "()V", "areContentsTheSame", "", "oldItem", "newItem", "areItemsTheSame", "feature-profile_debug"})
        static final class MedalsDiffCallback extends androidx.recyclerview.widget.DiffUtil.ItemCallback<com.yakulab.domain.profile.MedalsDomain> {
            
            public MedalsDiffCallback() {
                super();
            }
            
            @java.lang.Override
            public boolean areItemsTheSame(@org.jetbrains.annotations.NotNull
            com.yakulab.domain.profile.MedalsDomain oldItem, @org.jetbrains.annotations.NotNull
            com.yakulab.domain.profile.MedalsDomain newItem) {
                return false;
            }
            
            @java.lang.Override
            public boolean areContentsTheSame(@org.jetbrains.annotations.NotNull
            com.yakulab.domain.profile.MedalsDomain oldItem, @org.jetbrains.annotations.NotNull
            com.yakulab.domain.profile.MedalsDomain newItem) {
                return false;
            }
        }
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/yakulab/feature/profile/MedalsAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/yakulab/feature/profile/databinding/ItemMedalBinding;", "(Lcom/yakulab/feature/profile/MedalsAdapter;Lcom/yakulab/feature/profile/databinding/ItemMedalBinding;)V", "bind", "", "item", "Lcom/yakulab/domain/profile/MedalsDomain;", "feature-profile_debug"})
    public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull
        private final com.yakulab.feature.profile.databinding.ItemMedalBinding binding = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull
        com.yakulab.feature.profile.databinding.ItemMedalBinding binding) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull
        com.yakulab.domain.profile.MedalsDomain item) {
        }
    }
}