package com.qpay.customer.di

import com.qpay.customer.nid_scan.NIDScanCameraXFragment
import com.qpay.customer.ui.chapter_list.ChapterListFragment
import com.qpay.customer.ui.home.HomeFragment
import com.qpay.customer.ui.how_works.HowWorksFragment
import com.qpay.customer.ui.login.SignInFragment
import com.qpay.customer.ui.terms_and_conditions.TermsAndConditionsFragment
import com.qpay.customer.ui.on_boarding.tou.TouFragment
import com.qpay.customer.ui.otp.OtpFragment
import com.qpay.customer.ui.pre_on_boarding.PreOnBoardingFragment
import com.qpay.customer.ui.profiles.ProfilesFragment
import com.qpay.customer.ui.registration.RegistrationFragment
import com.qpay.customer.ui.settings.SettingsFragment
import com.qpay.customer.ui.setup.SetupFragment
import com.qpay.customer.ui.setup_complete.SetupCompleteFragment
import com.qpay.customer.ui.splash.SplashFragment
import com.qpay.customer.ui.video_play.LoadWebViewFragment
import com.qpay.customer.ui.video_play.VideoPlayFragment
import com.qpay.customer.ui.login.ViewPagerFragment
import com.qpay.customer.ui.otp_signin.OtpSignInFragment
import com.qpay.customer.ui.pin_number.PinNumberFragment
import com.qpay.customer.ui.profile_signin.ProfileSignInFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeSplashFragment(): SplashFragment

    @ContributesAndroidInjector
    abstract fun contributeTermsAndConditionsFragment(): TermsAndConditionsFragment


    @ContributesAndroidInjector
    abstract fun contributeSignInFragment(): SignInFragment

    @ContributesAndroidInjector
    abstract fun contributeNIDScanCameraXFragment(): NIDScanCameraXFragment

    @ContributesAndroidInjector
    abstract fun contributePreOnBoardingFragment(): PreOnBoardingFragment

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeHowWorksFragment(): HowWorksFragment

    @ContributesAndroidInjector
    abstract fun contributeRegistrationFragment(): RegistrationFragment

    @ContributesAndroidInjector
    abstract fun contributeOtpFragment(): OtpFragment

    @ContributesAndroidInjector
    abstract fun contributeTouFragment(): TouFragment

    @ContributesAndroidInjector
    abstract fun contributeSetupFragment(): SetupFragment

    @ContributesAndroidInjector
    abstract fun contributeSetupCompleteFragment(): SetupCompleteFragment

    @ContributesAndroidInjector
    abstract fun contributeProfilesFragment(): ProfilesFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun contributeViewPagerFragment(): ViewPagerFragment

    @ContributesAndroidInjector
    abstract fun contributeChapterListFragment(): ChapterListFragment

    @ContributesAndroidInjector
    abstract fun contributeVideoPlayFragment(): VideoPlayFragment

    @ContributesAndroidInjector
    abstract fun contributeLoadWebViewFragment(): LoadWebViewFragment

    @ContributesAndroidInjector
    abstract fun contributeOtpSignInFragment(): OtpSignInFragment

    @ContributesAndroidInjector
    abstract fun contributePinNumberFragment(): PinNumberFragment

    @ContributesAndroidInjector
    abstract fun contributeProfileSignInFragment(): ProfileSignInFragment
}