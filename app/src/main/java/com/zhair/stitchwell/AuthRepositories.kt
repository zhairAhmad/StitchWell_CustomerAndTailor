package com.zhair.stitchwell

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await

class AuthRepositories {

    suspend fun logout():Result<Boolean>{
        FirebaseAuth.getInstance().signOut()
        return Result.success(true)
    }


    suspend fun login(email: String, password: String): Result<Users> {
        try {
            val result = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user!!

            // Create Users object from FirebaseUser
            val user = Users(
                id = firebaseUser.uid,
                role = "customer", // You might need to fetch the role from Firestore
                fullName = firebaseUser.displayName ?: "", // Handle null if needed
                email = firebaseUser.email ?: "", // Handle null if needed
                phone = firebaseUser.phoneNumber ?: "" // Handle null if needed
            )

            return Result.success(user)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }



    suspend fun signup(name: String, email: String, phone: String, password: String): Result<Users> {
        try {
            val result = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user!!

            // Assign user ID
            val userId = firebaseUser.uid

            // Create Users object
            val user = Users(
                id = userId,
                role = "customer",
                fullName = name,
                email = email,
                phone = phone
            )

            val profileUpdates = userProfileChangeRequest {
                displayName = name
            }
            firebaseUser.updateProfile(profileUpdates).await()

            return Result.success(user)
        } catch (e: Exception) {
            return Result.failure(e)
        }


    }

    suspend fun resetPassword(email: String): Result<Boolean> {
        try {
            val result = FirebaseAuth.getInstance().sendPasswordResetEmail(email).await()
            return Result.success(true)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
    fun getCurrentUser(): Users? {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        return if (firebaseUser != null) {
            Users(
                id = firebaseUser.uid,
                role = "customer", // You might need to fetch the role from Firestore
                fullName = firebaseUser.displayName ?: "", // Handle null if needed
                email = firebaseUser.email ?: "", // Handle null if needed
                phone = firebaseUser.phoneNumber ?: "" // Handle null if needed
            )
        } else {
            null
        }
    }
//fun getCurrentUser():FirebaseUser?{
//    return FirebaseAuth.getInstance().currentUser
//}
}
