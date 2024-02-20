package com.ozanarik.business.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class FirebaseRepository @Inject constructor(private val firebaseAuth:FirebaseAuth) {


    private val _firebaseUser:MutableLiveData<FirebaseUser> = MutableLiveData()

    val firebaseUser:LiveData<FirebaseUser?> =_firebaseUser

    val errorMessage:MutableStateFlow<String> = MutableStateFlow("")


    fun signupUser(email:String,password:String){

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->

            if (task.isSuccessful){
                _firebaseUser.value = firebaseAuth.currentUser
            }else{
                errorMessage.value = task.exception?.message.toString()
            }


        }
    }


    suspend fun signInUser(email:String,password: String,onComplete: (Boolean, String?) -> Unit){


        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->

            if (task.isSuccessful){
                onComplete(true,null)
            }else{
                onComplete(false,task.exception?.message)
            }
        }
    }


    fun signOut(){

        if (firebaseAuth.currentUser!=null){
            firebaseAuth.signOut()
        }
    }

}