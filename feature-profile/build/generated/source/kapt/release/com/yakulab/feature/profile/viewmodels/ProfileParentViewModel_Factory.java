package com.yakulab.feature.profile.viewmodels;

import com.paparazziteam.yakulab.binding.helper.application.MyPreferences;
import com.yakulab.usecases.firebase.getEmailLoggedUseCase;
import com.yakulab.usecases.firebase.getUserInfoUseCase;
import com.yakulab.usecases.yakulab.GetCertificationsUseCase;
import com.yakulab.usecases.yakulab.GetMedalsUseCase;
import com.yakulab.usecases.yakulab.LogoutUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
  private final Provider<getEmailLoggedUseCase> getEmailLoggedUseCaseProvider;

  private final Provider<getUserInfoUseCase> getUserInfoUseCaseProvider;

  private final Provider<GetMedalsUseCase> getMedalsUseCaseProvider;

  private final Provider<GetCertificationsUseCase> getCertificationsUseCaseProvider;

  private final Provider<LogoutUseCase> logoutUseCaseProvider;

  private final Provider<MyPreferences> myPreferencesProvider;

  public ProfileParentViewModel_Factory(
      Provider<getEmailLoggedUseCase> getEmailLoggedUseCaseProvider,
      Provider<getUserInfoUseCase> getUserInfoUseCaseProvider,
      Provider<GetMedalsUseCase> getMedalsUseCaseProvider,
      Provider<GetCertificationsUseCase> getCertificationsUseCaseProvider,
      Provider<LogoutUseCase> logoutUseCaseProvider,
      Provider<MyPreferences> myPreferencesProvider) {
    this.getEmailLoggedUseCaseProvider = getEmailLoggedUseCaseProvider;
    this.getUserInfoUseCaseProvider = getUserInfoUseCaseProvider;
    this.getMedalsUseCaseProvider = getMedalsUseCaseProvider;
    this.getCertificationsUseCaseProvider = getCertificationsUseCaseProvider;
    this.logoutUseCaseProvider = logoutUseCaseProvider;
    this.myPreferencesProvider = myPreferencesProvider;
  }

  @Override
  public ProfileParentViewModel get() {
    return newInstance(getEmailLoggedUseCaseProvider.get(), getUserInfoUseCaseProvider.get(), getMedalsUseCaseProvider.get(), getCertificationsUseCaseProvider.get(), logoutUseCaseProvider.get(), myPreferencesProvider.get());
  }

  public static ProfileParentViewModel_Factory create(
      Provider<getEmailLoggedUseCase> getEmailLoggedUseCaseProvider,
      Provider<getUserInfoUseCase> getUserInfoUseCaseProvider,
      Provider<GetMedalsUseCase> getMedalsUseCaseProvider,
      Provider<GetCertificationsUseCase> getCertificationsUseCaseProvider,
      Provider<LogoutUseCase> logoutUseCaseProvider,
      Provider<MyPreferences> myPreferencesProvider) {
    return new ProfileParentViewModel_Factory(getEmailLoggedUseCaseProvider, getUserInfoUseCaseProvider, getMedalsUseCaseProvider, getCertificationsUseCaseProvider, logoutUseCaseProvider, myPreferencesProvider);
  }

  public static ProfileParentViewModel newInstance(getEmailLoggedUseCase getEmailLoggedUseCase,
      getUserInfoUseCase getUserInfoUseCase, GetMedalsUseCase getMedalsUseCase,
      GetCertificationsUseCase getCertificationsUseCase, LogoutUseCase logoutUseCase,
      MyPreferences myPreferences) {
    return new ProfileParentViewModel(getEmailLoggedUseCase, getUserInfoUseCase, getMedalsUseCase, getCertificationsUseCase, logoutUseCase, myPreferences);
  }
}
