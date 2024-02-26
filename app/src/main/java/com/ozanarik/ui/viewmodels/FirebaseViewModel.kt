package com.ozanarik.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.ozanarik.business.model.GameGiveAwayResponseItem
import com.ozanarik.business.repositories.FirebaseRepository
import com.ozanarik.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirebaseViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) :ViewModel() {

    private val _authState:MutableStateFlow<Resource<FirebaseUser>> = MutableStateFlow(Resource.Loading())
    val authState:StateFlow<Resource<FirebaseUser>> = _authState

    private val _loginState:MutableStateFlow<Resource<FirebaseUser>> = MutableStateFlow(Resource.Loading())
    val loginState:StateFlow<Resource<FirebaseUser>> = _loginState

    private val _profilePhotoUri:MutableStateFlow<Resource<Uri>> = MutableStateFlow(Resource.Loading())
    val profilePhotoUri:StateFlow<Resource<Uri>> =_profilePhotoUri

    private val _getProfilePhotoFromFirestore:MutableStateFlow<Resource<String>> = MutableStateFlow(Resource.Loading())
    val getProfilePhotoFromFirestore:StateFlow<Resource<String>> = _getProfilePhotoFromFirestore



    fun getProfilePhotoFromFirestore()=viewModelScope.launch {

        firebaseRepository.getProfilePhoto()

        firebaseRepository.getPhotoFromFirestore.collect{profilePhotoResponse->
            _getProfilePhotoFromFirestore.value = profilePhotoResponse
        }


    }


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

    fun uploadProfilePhoto(profilePhotoUri:Uri)=viewModelScope.launch {
        firebaseRepository.uploadProfilePhoto(profilePhotoUri)

        firebaseRepository.profilePhotoUri.collect{profilePhotoResponse->
            _profilePhotoUri.value = profilePhotoResponse
        }
    }



    fun signOutUser()=viewModelScope.launch{
        firebaseRepository.userSignOut()
    }

}