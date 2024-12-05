package com.zhair.stitchwell

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zhair.stitchwell.databinding.ActivityForgetPasswordBinding
import kotlinx.coroutines.launch

class forgetPassword : AppCompatActivity() {
    lateinit var progressDialog: ProgressDialog
    lateinit var binding: ActivityForgetPasswordBinding
    lateinit var viewModel:AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel=AuthViewModel()

        progressDialog= ProgressDialog(this)
        progressDialog.setMessage("Please wait while...")
        progressDialog.setCancelable(false)

        lifecycleScope.launch {
            viewModel.failureMessage.collect{
                progressDialog.dismiss()
                if (it!=null){
                    Toast.makeText(this@forgetPassword,it, Toast.LENGTH_SHORT).show()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.resetResponse.collect{
                progressDialog.dismiss()
                if (it!=null){
                    val builder= MaterialAlertDialogBuilder(this@forgetPassword)
                    builder.setMessage("We have sent you a password reset email, check your inbox and click the link to reset your password")
                    builder.setCancelable(false)
                    builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, i ->
                        finish()
                    })
                    builder.show()
                }
            }
        }

        binding.loginBack.setOnClickListener {
            startActivity(Intent(this,Login::class.java))
            finish()
        }

        binding.restPasswordbtn.setOnClickListener {
            val email=binding.forgetPassword.editText?.text.toString()

            if(!email.contains("@")){
                Toast.makeText(this,"Invalid Email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressDialog.show()

            viewModel.resetPassword(email)

        }


    }
}