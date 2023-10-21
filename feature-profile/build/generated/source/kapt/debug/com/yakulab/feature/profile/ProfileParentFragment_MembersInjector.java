package com.yakulab.feature.profile;

import com.paparazziteam.yakulab.binding.helper.navigator.Navigator;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class ProfileParentFragment_MembersInjector implements MembersInjector<ProfileParentFragment> {
  private final Provider<Navigator> navigatorProvider;

  public ProfileParentFragment_MembersInjector(Provider<Navigator> navigatorProvider) {
    this.navigatorProvider = navigatorProvider;
  }

  public static MembersInjector<ProfileParentFragment> create(
      Provider<Navigator> navigatorProvider) {
    return new ProfileParentFragment_MembersInjector(navigatorProvider);
  }

  @Override
  public void injectMembers(ProfileParentFragment instance) {
    injectNavigator(instance, navigatorProvider.get());
  }

  @InjectedFieldSignature("com.yakulab.feature.profile.ProfileParentFragment.navigator")
  public static void injectNavigator(ProfileParentFragment instance, Navigator navigator) {
    instance.navigator = navigator;
  }
}
