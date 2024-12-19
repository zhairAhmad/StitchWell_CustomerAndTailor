package com.zhair.stitchwell

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.tasks.await

class AuthRepositories {
    val UserCollection = FirebaseFirestore.getInstance().collection("users")

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
            saveUser(user)
            firebaseUser.updateProfile(profileUpdates).await()

            return Result.success(user)
        } catch (e: Exception) {
            return Result.failure(e)
        }


    }

    suspend fun saveUser(user: Users): Result<Boolean> {
        try {
            val document = UserCollection.document()
//            user.id = document.id
            document.set(user).await()
            return Result.success(true)

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
    suspend fun loadUser(): Result<Users?> {
        return try {
            // Get current user's UID from FirebaseAuth
            val uid = FirebaseAuth.getInstance().currentUser?.uid
                ?: return Result.success(null)
            // If the user is not logged in, return null
            Log.i("test3", uid)
            // Firestore instance and users collection
            val db = FirebaseFirestore.getInstance()
            val usersCollection = db.collection("users")

            // Query Firestore for the user document where id matches the UID
            val querySnapshot = usersCollection.whereEqualTo("id", uid).get().await()
            if (!querySnapshot.isEmpty) {
                // Assuming you expect a single document; retrieve the first one
                val documentSnapshot = querySnapshot.documents.first()
                val user = documentSnapshot.toObject(Users::class.java)
                Log.i("test3", user!!.phone.toString())
                Result.success(user)
            } else {
                Log.i("test3", "No matching user found")
                Result.success(null) // No matching user found

            }
        } catch (e: Exception) {
            // Handle any exceptions during the operation
            Log.e("test3", "Error loading user: ${e.message}")
            Result.failure(e)
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
