package com.ozanarik.business.repositories

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.ozanarik.utilities.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseRepository @Inject constructor(private val firebaseAuth:FirebaseAuth) {

    private val _signupState:MutableStateFlow<Resource<FirebaseUser>> = MutableStateFlow(Resource.Loading())
    val signupState:StateFlow<Resource<FirebaseUser>> = _signupState

    private val _loginState:MutableStateFlow<Resource<FirebaseUser>> = MutableStateFlow(Resource.Loading())
    val loginState:StateFlow<Resource<FirebaseUser>> = _loginState


    suspend fun signupUser(email:String, password:String,displayName:String) {

        _signupState.value = Resource.Loading()

        try {


            val authResult = firebaseAuth.createUserWithEmailAndPassword(email,password).await()

            val firebaseUser = authResult.user


            val profileUpdateRequest = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()
            firebaseUser!!.updateProfile(profileUpdateRequest).await()


            _signupState.value = Resource.Success(firebaseUser)

        }catch (e:Exception){
            val errorMsg = when(e){
                is FirebaseException->{"Firebase Exception: ${e.message?:e.localizedMessage}"}
                is IOException->{"IO Exception: ${e.message?:e.localizedMessage}"}
                else->{"Unknown Error"}
            }
            _signupState.value = Resource.Error(errorMsg)
        }
    }


    suspend fun loginUser(email:String,password: String){
        _loginState.value = Resource.Loading()

        try {

            val loginResult = firebaseAuth.signInWithEmailAndPassword(email,password).await()

            if (loginResult.user!=null){
                _loginState.value = Resource.Success(loginResult.user!!)
            }


        }catch (e:Exception){
            val errorMsg = when(e){
                is FirebaseException->{"Firebase Exception: ${e.message?:e.localizedMessage}"}
                is IOException->{"IO Exception: ${e.message?:e.localizedMessage}"}
                else->{"Unknown Error"}
            }

            _loginState.value = Resource.Error(errorMsg)
        }
    }

    suspend fun userSignOut(){

        return suspendCoroutine { continuation->
            firebaseAuth.signOut()
            continuation.resume(Unit)
        }
    }


}