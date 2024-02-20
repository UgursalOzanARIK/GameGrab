package com.ozanarik.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ozanarik.business.remote.GameApi
import com.ozanarik.business.repositories.FirebaseRepository
import com.ozanarik.business.repositories.GameRepository
import com.ozanarik.utilities.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {



    @Provides
    @Singleton
    fun provideBaseUrl()=BASE_URL


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient):Retrofit{

        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }


    @Provides
    @Singleton
    fun provideFirebaseRepository(firebaseAuth: FirebaseAuth):FirebaseRepository{
        return FirebaseRepository(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth():FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideGameApi(retrofit: Retrofit):GameApi{
        return retrofit.create(GameApi::class.java)
    }

    fun provideGameRepository(gameApi: GameApi):GameRepository{
        return GameRepository(gameApi)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient():OkHttpClient{

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        GsonBuilder().setLenient()

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .retryOnConnectionFailure(true)
            .build()

    }

}