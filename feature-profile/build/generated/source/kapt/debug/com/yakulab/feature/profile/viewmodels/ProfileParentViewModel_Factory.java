package com.yakulab.feature.profile.viewmodels;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class ProfileParentViewModel_Factory implements Factory<ProfileParentViewModel> {
  @Override
  public ProfileParentViewModel get() {
    return newInstance();
  }

  public static ProfileParentViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ProfileParentViewModel newInstance() {
    return new ProfileParentViewModel();
  }

  private static final class InstanceHolder {
    private static final ProfileParentViewModel_Factory INSTANCE = new ProfileParentViewModel_Factory();
  }
}
