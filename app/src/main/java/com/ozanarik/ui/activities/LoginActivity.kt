package com.ozanarik.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ozanarik.gamegrab.R
import com.ozanarik.gamegrab.databinding.ActivityLoginBinding
import com.ozanarik.ui.viewmodels.FirebaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var firebaseViewModel: FirebaseViewModel
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        firebaseViewModel = ViewModelProvider(this)[FirebaseViewModel::class.java]


        handleSignup()




        setContentView(binding.root)
    }




    private fun handleSignup(){


        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        binding.buttonNext.setOnClickListener {

            if (email.isEmpty() || password.isEmpty()){

                Toast.makeText(this,"Empty",Toast.LENGTH_LONG).show()



                binding.etEmail.error = "This field is required"
                binding.etPassword.error = "This field is required"


                return@setOnClickListener
            }else{
                firebaseViewModel.signupUser(email,password)

                lifecycleScope.launch {
                    firebaseViewModel.authState.collect{authState->

                        when(authState){
                            true->{
                                startActivity(Intent(this@LoginActivity,MainActivity::class.java))

                            }
                            false->{
                                Log.e("asd",",asd")
                            }
                        }

                    }
                }
            }
            }






    }

}