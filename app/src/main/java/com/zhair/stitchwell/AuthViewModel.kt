package com.zhair.stitchwell

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel:ViewModel() {
    val AuthRepository = AuthRepositories()
    val currentUser = MutableStateFlow<Users?>(null)
//    val currentUser = MutableStateFlow<FirebaseUser?>(null)

    val failureMessage = MutableStateFlow<String?>(null)
    val resetResponse = MutableStateFlow<Boolean?>(null)

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

    fun signUp(name: String, email: String, phone: String, password: String) {
        viewModelScope.launch {
            val result = AuthRepository.signup(name, email, phone, password,)
            if (result.isSuccess) {
                currentUser.value = result.getOrThrow()
            } else {
                failureMessage.value = result.exceptionOrNull()?.message
            }
        }
    }
    fun checkUser(){
        currentUser.value=AuthRepository.getCurrentUser()
    }
    fun login(email:String,password:String){
        viewModelScope.launch {
            val result=AuthRepository.login(email,password)
            if (result.isSuccess){
                currentUser.value=result.getOrThrow()
            }else{
                failureMessage.value=result.exceptionOrNull()?.message
            }
        }
    }

}