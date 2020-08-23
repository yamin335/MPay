package com.qpay.customer.di

import android.app.Application
import android.content.Context
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.room.Room
import com.google.common.util.concurrent.ListenableFuture
import com.qpay.customer.local_db.AppDb
import com.qpay.customer.prefs.AppPreferencesHelper
import com.qpay.customer.prefs.PreferencesHelper
import com.qpay.customer.util.AppConstants
import com.squareup.picasso.BuildConfig
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton


@Module(
    includes = [
        ViewModelModule::class,
        NetworkModule::class,
        WorkerBindingModule::class]
)
class AppModule {

    @Singleton
    @Provides
    fun provideDb(app: Application): AppDb {
        return Room
            .databaseBuilder(app, AppDb::class.java, "psb.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providePicasso(app: Application, okHttpClient: OkHttpClient) = Picasso.Builder(app)
        .downloader(OkHttp3Downloader(okHttpClient))
        .listener { _, _, e -> if (BuildConfig.DEBUG) e.printStackTrace() }
        .loggingEnabled(BuildConfig.DEBUG)
        .build()

    /*@Singleton
    @Provides
    fun provideProfileDao(db: AppDb): ProfileDao {
        return db.profileDao()
    }

    @Singleton
    @Provides
    fun provideBankDao(db: AppDb): BankDao {
        return db.bankDao()
    }

    @Singleton
    @Provides
    fun provideStatementDao(db: AppDb): StatementDao {
        return db.statementDao()
    }*/


    @Singleton
    @Provides
    internal fun providePreferencesHelper(appPreferencesHelper: AppPreferencesHelper): PreferencesHelper {
        return appPreferencesHelper
    }

    @Provides
    @PreferenceInfo
    internal fun providePreferenceName(): String {
        return AppConstants.PREF_NAME
    }

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context {
        return application
    }
}