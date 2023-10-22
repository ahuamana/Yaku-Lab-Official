package com.yakulab.feature.profile;

import com.paparazziteam.yakulab.binding.helper.analytics.FBaseAnalytics;
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

  private final Provider<FBaseAnalytics> fBaseAnalyticsProvider;

  public ProfileParentFragment_MembersInjector(Provider<Navigator> navigatorProvider,
      Provider<FBaseAnalytics> fBaseAnalyticsProvider) {
    this.navigatorProvider = navigatorProvider;
    this.fBaseAnalyticsProvider = fBaseAnalyticsProvider;
  }

  public static MembersInjector<ProfileParentFragment> create(Provider<Navigator> navigatorProvider,
      Provider<FBaseAnalytics> fBaseAnalyticsProvider) {
    return new ProfileParentFragment_MembersInjector(navigatorProvider, fBaseAnalyticsProvider);
  }

  @Override
  public void injectMembers(ProfileParentFragment instance) {
    injectNavigator(instance, navigatorProvider.get());
    injectFBaseAnalytics(instance, fBaseAnalyticsProvider.get());
  }

  @InjectedFieldSignature("com.yakulab.feature.profile.ProfileParentFragment.navigator")
  public static void injectNavigator(ProfileParentFragment instance, Navigator navigator) {
    instance.navigator = navigator;
  }

  @InjectedFieldSignature("com.yakulab.feature.profile.ProfileParentFragment.fBaseAnalytics")
  public static void injectFBaseAnalytics(ProfileParentFragment instance,
      FBaseAnalytics fBaseAnalytics) {
    instance.fBaseAnalytics = fBaseAnalytics;
  }
}
