package com.zhair.stitchwell

import OrderAdapter
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.zhair.stitchwell.MainActivity.Companion.user
import com.zhair.stitchwell.databinding.FragmentHomeBinding
import com.zhair.stitchwell.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    lateinit var binding:FragmentProfileBinding
//  lateinit var viewModel: HomeFragmentViewModel
    lateinit var progressDialog: ProgressDialog
    lateinit var authViewModel: AuthViewModel
    lateinit var CurrentUser:Users


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel = AuthViewModel()
        CurrentUser = Users("", "", "", "", "")
        authViewModel.loadUser()

        progressDialog= ProgressDialog(requireActivity())
        progressDialog.setMessage("Updating Your Profile...")
        progressDialog.setCancelable(false)
        lifecycleScope.launch {
            authViewModel.currentUser.collect {
                it?.let {

                    CurrentUser = it
                    user = it

                    binding.email.editText?.setText(CurrentUser.email)
                    binding.displayname.editText?.setText(CurrentUser.fullName)
                    binding.phoneNo.text = it.phone

                }
            }
        }

        lifecycleScope.launch {
            authViewModel.isSavingUser.collect{
                if (it==true)
                    progressDialog.show()
            }
        }
        lifecycleScope.launch {
            authViewModel.isUpdated.collect{
                if (it==true){
                     Toast.makeText(requireActivity(), "The Information is Updated", Toast.LENGTH_LONG).show()
                       progressDialog.dismiss()
                      authViewModel.loadUser()}

            }
        }
        lifecycleScope.launch {
            authViewModel.isPasswordUpdated.collect{
                if (it==true){
                    Toast.makeText(requireActivity(), "The Password is Changed Successfully", Toast.LENGTH_LONG).show()
                    progressDialog.dismiss()
                    authViewModel.Logout()
                }

            }
        }



        binding.loginbtn3.setOnClickListener(){
            if(CurrentUser.fullName == binding.displayname.editText?.text.toString() && CurrentUser.email == binding.email.editText?.text.toString()){
             Toast.makeText(requireActivity(), "Data Not Changed", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else{
                      CurrentUser.email=binding.email.editText?.text.toString()
                      CurrentUser.fullName=binding.displayname.editText?.text.toString()
                      authViewModel.updateUserProfile(CurrentUser)

            }

        }

        binding.changebtn4.setOnClickListener(){
            if(binding.password.editText?.text.toString() == binding.confirmPassword.editText?.text.toString()){
                progressDialog.setMessage("Updating Your Password...")
                progressDialog.show()
                authViewModel.updatePassword(binding.password.editText?.text.toString())
            }
            else{
                Toast.makeText(requireActivity(), "Password Not Match", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

        }
    }
}