package com.ozanarik.ui.fragments.main_fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ozanarik.gamegrab.databinding.FragmentSettingsBinding
import com.ozanarik.ui.activities.SignupActivity
import com.ozanarik.ui.viewmodels.FirebaseViewModel
import com.ozanarik.utilities.Extensions.Companion.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var fireStore:FirebaseFirestore
    private lateinit var firebaseViewModel: FirebaseViewModel


    private lateinit var cameraPermissionLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment



        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        firebaseViewModel = ViewModelProvider(this)[FirebaseViewModel::class.java]

        auth = FirebaseAuth.getInstance()

        handleUserInfo()
        userSignOut()






        return binding.root
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


    private fun handleCameraPermission(){


        if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_MEDIA_IMAGES)!=PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_MEDIA_IMAGES)){

                Snackbar.make(binding.imageViewChangeProfilePicture,"Permission needed to pick medias!",Snackbar.LENGTH_LONG).apply {
                    setAction("Grant Permission"){
                        requirePermission()
                    }.show()
                }


            }else{
                requirePermission()
            }
        }else{
            requirePermission()
        }



        binding.imageViewChangeProfilePicture.setOnClickListener {




        }


    }

    private fun requirePermission(){

    }



}