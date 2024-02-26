package com.ozanarik.ui.fragments.main_fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ozanarik.gamegrab.R
import com.ozanarik.gamegrab.databinding.FragmentSettingsBinding
import com.ozanarik.ui.activities.SignupActivity
import com.ozanarik.ui.viewmodels.FirebaseViewModel
import com.ozanarik.utilities.Extensions.Companion.showSnackbar
import com.ozanarik.utilities.Resource
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var firebaseStorage:FirebaseStorage

    private lateinit var firebaseViewModel: FirebaseViewModel

    private lateinit var galleryActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher:ActivityResultLauncher<String>

    private var profilePhoto: Uri?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        firebaseViewModel = ViewModelProvider(this)[FirebaseViewModel::class.java]

        auth = FirebaseAuth.getInstance()
        firestoreDb = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()

        handleResultLaunchers()

        handleUserInfo()
        userSignOut()

        getPhotoFromGallery()

        getProfilePhoto()



        return binding.root
    }


    private fun getPhotoFromGallery(){

        binding.imageViewChangeProfilePicture.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){

                    Snackbar.make(binding.imageViewUserProfilePhoto,"Permission needed to access gallery!",Snackbar.LENGTH_LONG).apply {
                        setAction("Grant permission"){
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    }.show()

                }else{
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }else{

                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                galleryActivityResultLauncher.launch(intentToGallery)

            }

        }

    }


    private fun userSignOut(){

        binding.buttonLogOut.setOnClickListener {
            lifecycleScope.launch {

                firebaseViewModel.signOutUser()
                startActivity(Intent(requireContext(),SignupActivity::class.java))
                requireActivity().finish()
            }
        }

    }

    private fun handleUserInfo(){

        if (auth.currentUser!=null){

            binding.tvUserDisplayName.text = auth.currentUser!!.displayName
            binding.tvUserEmail.text = auth.currentUser!!.email

        }
    }

    private fun handleProfilePhotoUpload(){

        if (profilePhoto!=null){
            profilePhoto?.let { firebaseViewModel.uploadProfilePhoto(it) }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            firebaseViewModel.profilePhotoUri.collect{profilePhotoResponse->

                when(profilePhotoResponse){
                    is Resource.Success->{
                        Picasso.get().load(profilePhoto).placeholder(R.drawable.placeholder).error(R.drawable.error).into(binding.imageViewUserProfilePhoto)
                        showSnackbar("Profile photo successfully updated!")
                    }
                    is Resource.Error->{showSnackbar(profilePhotoResponse.message!!)}
                    is Resource.Loading->{showSnackbar("Uploading profile photo!")}
                }
            }
        }



    }


    private fun getProfilePhoto(){

        firebaseViewModel.getProfilePhotoFromFirestore()

        viewLifecycleOwner.lifecycleScope.launch {

            firebaseViewModel.getProfilePhotoFromFirestore.collect{profilePhotoResponse->
                when(profilePhotoResponse){
                    is Resource.Success->{Picasso.get().load(profilePhotoResponse.data).placeholder(R.drawable.placeholder).error(R.drawable.error).into(binding.imageViewUserProfilePhoto)}
                    is Resource.Error->{showSnackbar("profilePhotoResponse.message!!")}
                    is Resource.Loading->{showSnackbar("Fetching profile photo!")}
                }
            }
        }
}

    private fun handleResultLaunchers(){

        galleryActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            if (result.resultCode == RESULT_OK){

                val resultData = result.data

                if (resultData!=null){
                    profilePhoto = resultData.data

                    binding.imageViewUserProfilePhoto.setImageURI(profilePhoto)
                    handleProfilePhotoUpload()

                }
            }
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){isGranted->
            if (isGranted){
                val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                galleryActivityResultLauncher.launch(galleryIntent)
            }else{
                showSnackbar("Permission needed to access gallery!")
            }
        }
    }
}