package com.zhair.stitchwell

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class OrderRepository {
    val orderCollection = FirebaseFirestore.getInstance().collection("orders")

    suspend fun saveOrder(order: Order): Result<Boolean> {
        try {
            val document = orderCollection.document()
            order.orderid = document.id
            document.set(order).await()
            return Result.success(true)

        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun updateRequest(order: Order): Result<Boolean> {
        try {
            if (order.orderid.isNullOrEmpty()) {
                return Result.failure(IllegalArgumentException("Order ID (rid) is null or empty"))
            }

            // Update the document with the provided rid
            orderCollection.document(order.orderid!!).set(order).await()
            return Result.success(true)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    fun getOrders() =
        orderCollection.snapshots().map { it.toObjects(Order::class.java) }
    fun getOrdersOfUser(phoneNumber: String) =
        orderCollection.whereEqualTo("phoneNumber",phoneNumber).snapshots().map { it.toObjects(Order::class.java)

        }


    fun getOrdersOfUserPending(phoneNumber: String) =
        orderCollection.whereEqualTo("phoneNumber",phoneNumber).whereEqualTo("status", "Pending").snapshots().map { it.toObjects(Order::class.java) }
    fun getOrdersOfUserCompleted(phoneNumber: String) =
        orderCollection.whereEqualTo("phoneNumber",phoneNumber).whereEqualTo("status", "Completed").snapshots().map { it.toObjects(Order::class.java) }

}

