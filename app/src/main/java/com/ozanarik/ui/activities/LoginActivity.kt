package com.ozanarik.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ozanarik.gamegrab.databinding.ActivityLoginBinding
import com.ozanarik.ui.viewmodels.FirebaseViewModel
import com.ozanarik.utilities.Extensions.Companion.showSnackbar
import com.ozanarik.utilities.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseViewModel: FirebaseViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityLoginBinding.inflate(layoutInflater)
        firebaseViewModel = ViewModelProvider(this)[FirebaseViewModel::class.java]


        handleLogin()


        binding.imageViewNavigateBackFromSettings.setOnClickListener {
            startActivity(Intent(this@LoginActivity,SignupActivity::class.java))
            finish()
        }



        setContentView(binding.root)
    }




    private fun handleLogin(){


        binding.buttonLogin.setOnClickListener {




            val email = binding.etEmailLogin.text.toString()
            val password = binding.etPasswordLogin.text.toString()


            if (email.isNotEmpty() || password.isNotEmpty()){
                firebaseViewModel.loginUser(email,password)

                lifecycleScope.launch {

                    firebaseViewModel.loginState.collect{firebaseLoginResponse->
                        when(firebaseLoginResponse){
                            is Resource.Success->{
                                Log.e("asd","succ")

                                startActivity(Intent(this@LoginActivity,MainActivity::class.java))}
                            is Resource.Error->{binding.buttonLogin.showSnackbar(firebaseLoginResponse.message!!)}
                            is Resource.Loading->{binding.buttonLogin.showSnackbar("Logging in!")}
                        }

                    }
                }
            }

        }





    }


}