package com.ozanarik.business.repositories

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import com.google.firebase.FirebaseException
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ozanarik.business.model.GameGiveAwayResponseItem
import com.ozanarik.utilities.Resource
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseRepository @Inject constructor
    (
    private val firebaseAuth:FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseFirestore: FirebaseFirestore
) {

    private val _signupState:MutableStateFlow<Resource<FirebaseUser>> = MutableStateFlow(Resource.Loading())
    val signupState:StateFlow<Resource<FirebaseUser>> = _signupState

    private val _loginState:MutableStateFlow<Resource<FirebaseUser>> = MutableStateFlow(Resource.Loading())
    val loginState:StateFlow<Resource<FirebaseUser>> = _loginState

    private val _profilePhotoUri:MutableStateFlow<Resource<Uri>> = MutableStateFlow(Resource.Loading())
    val profilePhotoUri:StateFlow<Resource<Uri>> = _profilePhotoUri

    private val _getPhotoFromFirestore:MutableStateFlow<Resource<String>> = MutableStateFlow(Resource.Loading())
    val getPhotoFromFirestore:StateFlow<Resource<String>> = _getPhotoFromFirestore


    suspend fun uploadProfilePhoto(profilePhotoUri: Uri){

        _profilePhotoUri.value = Resource.Loading()

        val storageReference = firebaseStorage.reference

        val randomUUID = UUID.randomUUID()

        val auth = firebaseAuth
        val currentUser = auth.currentUser

        val randomImageName = "$randomUUID.jpg"

        try {

            if (firebaseAuth.currentUser!=null){
                val uploadedProfilePhotoPath = storageReference.child("profilePhotos").child(currentUser!!.uid).child(randomImageName)

                uploadedProfilePhotoPath.putFile(profilePhotoUri).await()

                val profilePhotoDownloadUrl = uploadedProfilePhotoPath.downloadUrl.await()

                val profileHashMap = hashMapOf(
                    "userEmail" to auth.currentUser?.email,
                    "timeStamp" to Timestamp.now(),
                    "profilePhotoDownloadUrl" to profilePhotoDownloadUrl
                )

                firebaseFirestore.collection("Users").document(auth.currentUser!!.uid).set(profileHashMap).await()

                _profilePhotoUri.value = Resource.Success(profilePhotoDownloadUrl)

            }


        }catch (e:Exception){

            _profilePhotoUri.value = Resource.Error(e.message?:e.localizedMessage!!)
        }
    }

    suspend fun getProfilePhoto(){

        _getPhotoFromFirestore.value = Resource.Loading()

        val auth = firebaseAuth

        try {
            val collection = firebaseFirestore.collection("Users").document(auth.currentUser!!.uid).get().await()

            val downloadUrl = collection.getString("profilePhotoDownloadUrl")?: ""

            if (downloadUrl.isNotEmpty()){
                _getPhotoFromFirestore.value = Resource.Success(downloadUrl)
            }else{
                _getPhotoFromFirestore.value = Resource.Error("Profile photo can't be found!")
            }

        }catch (e:Exception){
            _getPhotoFromFirestore.value = Resource.Error(e.message?:e.localizedMessage!!)
        }catch (e:IOException){
            _getPhotoFromFirestore.value = Resource.Error(e.message?:e.localizedMessage!!)

        }


    }


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