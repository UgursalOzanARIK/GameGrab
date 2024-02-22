package com.ozanarik.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.ozanarik.business.repositories.FirebaseRepository
import com.ozanarik.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirebaseViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) :ViewModel() {

    private val _authState:MutableStateFlow<Resource<FirebaseUser>> = MutableStateFlow(Resource.Loading())
    val authState:StateFlow<Resource<FirebaseUser>> = _authState

    private val _loginState:MutableStateFlow<Resource<FirebaseUser>> = MutableStateFlow(Resource.Loading())
    val loginState:StateFlow<Resource<FirebaseUser>> = _loginState



    fun loginUser(email: String, password: String)=viewModelScope.launch {

        firebaseRepository.loginUser(email,password)

        firebaseRepository.loginState.collect{firebaseLoginResponse->

            _loginState.value = firebaseLoginResponse

        }

    }


    fun signupUser(email:String,password:String,displayName:String)=viewModelScope.launch {


        firebaseRepository.signupUser(email,password,displayName)

        firebaseRepository.signupState.collect{firebaseResponse->

            _authState.value = firebaseResponse

        }

    }

    fun signOutUser()=viewModelScope.launch{
        firebaseRepository.userSignOut()
    }

}