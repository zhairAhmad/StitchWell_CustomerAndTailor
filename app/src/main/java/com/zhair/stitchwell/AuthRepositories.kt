package com.zhair.stitchwell

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class AuthRepositories {
    val UserCollection = FirebaseFirestore.getInstance().collection("users")

    suspend fun logout(): Result<Boolean> {
        FirebaseAuth.getInstance().signOut()
        return Result.success(true)
    }


    suspend fun login(email: String, password: String): Result<Users> {
        try {
            val result =
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user!!

            // Create Users object from FirebaseUser
            val user = Users(
                idd = firebaseUser.uid,
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


    suspend fun signup(
        name: String,
        email: String,
        phone: String,
        password: String
    ): Result<Users> {
        try {
            val result =
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user!!

            // Assign user ID
            val userId = firebaseUser.uid

            // Create Users object
            val user = Users(
                idd = userId,
                role = "Customer",
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

  suspend  fun getTokenOf(phoneNumber: String): Result<Users> {
      try {
          val result = FirebaseFirestore.getInstance().collection("users").whereEqualTo("phone",phoneNumber).get().await()
          val users = result.documents.first().toObject(Users::class.java)
          if(users != null){
              return  Result.success(users)

//              Log.i("Token", "Get Success ${users.fmcToken}")
          } else {

//              Log.i("Token", "Get Success failed")
              return Result.failure(Exception("User not found while finding token"))
          }


      } catch (e: Exception) {
//          Log.i("Token", "Get failed ${e}")
          return Result.failure(e)
      }
  }


    suspend fun saveUser(user: Users): Result<Boolean> {
        try {
            val document = UserCollection.document(user.idd!!)
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
            val uid = FirebaseAuth.getInstance().currentUser?.uid
                ?: return Result.success(null)

            val db = FirebaseFirestore.getInstance()
            val usersCollection = db.collection("users")

            val querySnapshot = usersCollection.whereEqualTo("idd", uid).get().await()
            if (!querySnapshot.isEmpty) {
                // Assuming you expect a single document; retrieve the first one
                val documentSnapshot = querySnapshot.documents.first()
                val user = documentSnapshot.toObject(Users::class.java)
                Result.success(user)
            } else {
                Result.success(null) // No matching user found

            }
        } catch (e: Exception) {
            // Handle any exceptions during the operation
            Log.e("test3", "Error loading user: ${e.message}")
            Result.failure(e)
        }
    }


    suspend fun updateUserProfile(user: Users): Result<Boolean> {
        try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid


            val db = FirebaseFirestore.getInstance()
            val usersCollection = db.collection("users")

            usersCollection.document(uid!!).set(user, SetOptions.merge()).await()

            return Result.success(true)
        } catch (e: Exception) {
            Log.e("UpdateUserProfile", "Error updating user profile: ${e.message}")
            return Result.failure(e)
        }
    }


    fun getCurrentUser(): Users? {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        return if (firebaseUser != null) {
            Users(
                idd = firebaseUser.uid,
                role = "customer", // You might need to fetch the role from Firestore
                fullName = firebaseUser.displayName ?: "", // Handle null if needed
                email = firebaseUser.email ?: "", // Handle null if needed
                phone = firebaseUser.phoneNumber ?: "" // Handle null if needed
            )
        } else {
            null
        }
    }

    suspend fun updatePassword(newPassword: String): Result<Boolean> {
        return try {
            val user = FirebaseAuth.getInstance().currentUser
            user!!.updatePassword(newPassword).await() // Use await() for asynchronous operation
          return Result.success(true) // Update successful
        } catch (e: Exception) {
            return Result.failure(e) // Update failed
        }
    }
}
//fun getCurrentUser():FirebaseUser?{
//    return FirebaseAuth.getInstance().currentUser
//}

