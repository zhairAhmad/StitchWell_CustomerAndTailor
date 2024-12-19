package com.zhair.stitchwell

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.zhair.stitchwell.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {
    lateinit var progressDialog: ProgressDialog
    lateinit var binding: ActivityLoginBinding
    lateinit var viewModel:AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel=AuthViewModel()
        viewModel.checkUser()
//         viewModel.getCurrentUser()
        progressDialog=ProgressDialog(this)
        progressDialog.setMessage("Please wait while we check your credentials...")
        progressDialog.setCancelable(false)

        lifecycleScope.launch {
            viewModel.failureMessage.collect{
                progressDialog.dismiss()
                if (it!=null){
                    Toast.makeText(this@Login,it, Toast.LENGTH_SHORT).show()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.currentUser.collect{
                if (it!=null){
                    progressDialog.dismiss()
                    startActivity(Intent(this@Login, MainActivity::class.java))
                    viewModel.loadUser()
                    finish()
                }
            }
        }

        binding.signup.setOnClickListener {
            startActivity(Intent(this,signup::class.java))
            finish()
        }
        binding.forget.setOnClickListener {
            startActivity(Intent(this,forgetPassword::class.java))
            finish()
        }
        binding.loginbtn.setOnClickListener {
            val email=binding.name.editText?.text.toString()
            val password=binding.password.editText?.text.toString()

            Log.i("signT", email)
            Log.i("signT", password)

            if(!email.contains("@")){
                Toast.makeText(this,"Invalid Email",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(password.length<6){
                Toast.makeText(this,"Password must be at least 6 characters",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressDialog.show()

            viewModel.login(email,password)
        }

    }
}