package com.ozanarik.utilities

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreManager(context:Context) {


    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("appPrefs")
    private val dataStore = context.dataStore


    companion object{

        val bookmarkedYet = booleanPreferencesKey("isBookMarkedYet")


    }

    suspend fun setBookmarked(isBookMarked:Boolean){

        dataStore.edit { appPrefs->
            appPrefs[bookmarkedYet] = isBookMarked
        }

    }


    fun getBookmarked():Flow<Boolean>{

        return dataStore.data.catch {exception->

            if (exception is IOException){
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }.map { appPrefs->

            val bookmarkValue = appPrefs[bookmarkedYet]?:false
            bookmarkValue
        }
    }
}