package com.example.cakeapi_rxjava_mvvm.dependency_injection.network_module

import android.app.Application
import com.example.cakeapi_rxjava_mvvm.common.Constants
import com.example.cakeapi_rxjava_mvvm.dependency_injection.application.MyApplication
import com.example.cakeapi_rxjava_mvvm.network.GetCakeRequest
import com.example.cakeapi_rxjava_mvvm.view.MainActivityViewModelFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule(private val application: Application) {
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient):Retrofit{
        return Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideInterceptor():HttpLoggingInterceptor {
      return HttpLoggingInterceptor()
    }

    @Provides
    @Singleton
    fun provideOKHTTPClient(loggingInterceptor: HttpLoggingInterceptor):OkHttpClient{
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor).build()
    }

    @Provides
    @Singleton
    fun provideClientInterface(retrofit: Retrofit) = retrofit.create(GetCakeRequest::class.java)

    @Provides
    @Singleton
    fun provideApplicationContext():Application = application

    @Provides
    @Singleton
    fun provideCakeViewModelFactory(clientInterface:GetCakeRequest,application: Application):MainActivityViewModelFactory{
        return MainActivityViewModelFactory(clientInterface,application)
    }
}