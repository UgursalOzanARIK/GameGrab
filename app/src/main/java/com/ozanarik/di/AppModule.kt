package com.ozanarik.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ozanarik.business.local.GamesDao
import com.ozanarik.business.local.GamesDatabase
import com.ozanarik.business.remote.GameApi
import com.ozanarik.business.repositories.FirebaseRepository
import com.ozanarik.business.repositories.GameRepository
import com.ozanarik.utilities.Constants.Companion.BASE_URL
import com.ozanarik.utilities.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideGameDatabase(@ApplicationContext context:Context):GamesDatabase{
        return Room.databaseBuilder(context,GamesDatabase::class.java,"game_Table.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context:Context):Context{
        return context
    }

    @Provides
    @Singleton
    fun provideDatastore(@ApplicationContext context:Context):DataStoreManager{
        return DataStoreManager(context = context )
    }

    @Provides
    @Singleton
    fun provideGamesDao(db:GamesDatabase):GamesDao{
        return db.gamesDao
    }


    @Provides
    @Singleton
    fun provideFirebaseRepository(firebaseAuth: FirebaseAuth,firebaseStorage: FirebaseStorage,firebaseFirestore: FirebaseFirestore):FirebaseRepository{
        return FirebaseRepository(firebaseAuth,firebaseStorage,firebaseFirestore)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth():FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFireStore():FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage():FirebaseStorage{
        return FirebaseStorage.getInstance()
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