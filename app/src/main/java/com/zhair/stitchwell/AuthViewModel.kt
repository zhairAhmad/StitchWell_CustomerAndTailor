package com.zhair.stitchwell

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.MultiFactor
import com.google.firebase.auth.oAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel:ViewModel() {
    val AuthRepository = AuthRepositories()
    val currentUser = MutableStateFlow<Users?>(null)
    var isSavingUser = MutableStateFlow<Boolean>(false)
    var isUpdated = MutableStateFlow<Boolean>(false)
    var isPasswordUpdated = MutableStateFlow<Boolean>(false)
    val token = MutableStateFlow<String?>(null)
    val failureMessage = MutableStateFlow<String?>(null)
    val resetResponse = MutableStateFlow<Boolean?>(null)

    fun  getTokenOf(phone: String){
        viewModelScope.launch {
            val result=AuthRepository.getTokenOf(phone)
            if (result.isSuccess){
                  if(result.getOrNull()!=null){
                      token.value=result.getOrNull()!!.fmcToken
                  }
            }else{
                failureMessage.value=result.exceptionOrNull()?.message
            }
        }
    }

    fun resetPassword(email:String){
        viewModelScope.launch {
            val result=AuthRepository.resetPassword(email)
            if (result.isSuccess){
                resetResponse.value=result.getOrThrow()
            }else{
                failureMessage.value=result.exceptionOrNull()?.message
            }
        }
    }
    suspend fun Logout(){
        val result=AuthRepository.logout()
        if (result.isSuccess){
            currentUser.value=null
        }else{
            failureMessage.value=result.exceptionOrNull()?.message
    }


    }
    fun updateUserProfile(user:Users){
        viewModelScope.launch {
            isSavingUser.value= true
            val isUpdateSuccessful = AuthRepository.updateUserProfile(user)
            if (isUpdateSuccessful.isSuccess) {
                isSavingUser.value=false
                currentUser.value=user
                isUpdated.value=isUpdateSuccessful.getOrThrow()
            } else {
                isSavingUser.value=false
                isUpdated.value=false
                failureMessage.value = isUpdateSuccessful.exceptionOrNull()?.message
            }
        }
    }

    fun signUp(name: String, email: String, phone: String, password: String) {
        viewModelScope.launch {
            val result = AuthRepository.signup(name, email, phone, password,)
            if (result.isSuccess) {
                currentUser.value = result.getOrThrow()
                MainActivity.user=result.getOrThrow()
            } else {
                failureMessage.value = result.exceptionOrNull()?.message
            }
        }
    }
    fun checkUser(){
        currentUser.value=AuthRepository.getCurrentUser()
    }

    fun loadUser(){
        viewModelScope.launch {
            val result=AuthRepository.loadUser()
            if (result.isSuccess){
                currentUser.value=result.getOrThrow()
//                Log.i("test1",currentUser.value.toString())
            }else{
                failureMessage.value=result.exceptionOrNull()?.message
            }
        }
    }

    fun updatePassword(newPassword:String){
        viewModelScope.launch {
            val isUpdateSuccessful = AuthRepository.updatePassword(newPassword)
            if (isUpdateSuccessful.isSuccess) {
               isPasswordUpdated.value=isUpdateSuccessful.getOrThrow()
            } else {
                failureMessage.value=isUpdateSuccessful.exceptionOrNull()?.message
            }
        }
    }
    fun login(email:String,password:String){
        viewModelScope.launch {
            val result=AuthRepository.login(email,password)
            if (result.isSuccess){
                val result2=AuthRepository.loadUser()
                if (result2.isSuccess){
                    MainActivity.user=result2.getOrThrow()
                    currentUser.value=result2.getOrThrow()
                }else{
                    failureMessage.value=result2.exceptionOrNull()?.message
                }
            }else{
                failureMessage.value=result.exceptionOrNull()?.message
            }
        }
    }

}