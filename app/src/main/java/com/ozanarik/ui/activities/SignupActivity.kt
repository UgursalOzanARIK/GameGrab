package com.ozanarik.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.ozanarik.gamegrab.databinding.ActivityLoginBinding
import com.ozanarik.gamegrab.databinding.ActivitySignupBinding
import com.ozanarik.ui.viewmodels.FirebaseViewModel
import com.ozanarik.utilities.Extensions.Companion.showSnackbar
import com.ozanarik.utilities.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {
    private lateinit var firebaseViewModel: FirebaseViewModel
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        firebaseViewModel = ViewModelProvider(this)[FirebaseViewModel::class.java]
        auth = FirebaseAuth.getInstance()


        if (auth.currentUser!=null){
            startActivity(Intent(this@SignupActivity,MainActivity::class.java))
        }


        handleSignup()
        handleSignupOrLogin()







        setContentView(binding.root)
    }



    private fun handleSignupOrLogin(){

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this@SignupActivity,LoginActivity::class.java))

        }


    }




    private fun handleSignup() {


        binding.buttonNext.setOnClickListener {


            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val nickName = binding.etNickname.text.toString()

            if (email.isNotEmpty() || password.isNotEmpty() || nickName.isNotEmpty()) {

                firebaseViewModel.signupUser(email,password,nickName)

                lifecycleScope.launch {
                    firebaseViewModel.authState.collect{firebaseResponse->
                        when(firebaseResponse){
                            is Resource.Success->{startActivity(Intent(this@SignupActivity,MainActivity::class.java))}
                            is Resource.Error->{Snackbar.make(binding.buttonNext,firebaseResponse.message!!,Snackbar.LENGTH_LONG).show()}
                            is Resource.Loading->{Snackbar.make(binding.buttonNext,"Signing you up!",Snackbar.LENGTH_LONG).show()}
                        }

                    }
                }
            }else if(binding.etEmail.text.isEmpty() || binding.etPassword.text.isEmpty()){
                binding.etEmail.setError("E mail or password can not be empty!",)
            }
            else{
                binding.buttonNext.showSnackbar("Neither E mail nor password must be empty!")
            }


        }

    }
}