package com.ozanarik.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.ozanarik.business.repositories.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirebaseViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) :ViewModel() {

    private val _authState:MutableStateFlow<Boolean> = MutableStateFlow(false)
    val authState:StateFlow<Boolean> = _authState


    val firebaseLiveData:MutableLiveData<FirebaseUser> = MutableLiveData()



    private val _signupErrorMsg:MutableStateFlow<String> = MutableStateFlow("")
    val signupErrorMsg:StateFlow<String> = _signupErrorMsg



    fun signupUser(email:String,password:String)=viewModelScope.launch {

       firebaseRepository.signupUser(email,password)



    }


}