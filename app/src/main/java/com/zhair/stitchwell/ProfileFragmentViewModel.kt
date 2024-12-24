package com.zhair.stitchwell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhair.stitchwell.MainActivity
import com.zhair.stitchwell.Order
import com.zhair.stitchwell.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProfileFragmentViewModel : ViewModel() {
    val orderRepository = AuthRepositories()

    val failureMessage = MutableStateFlow<String?>(null)
    val data = MutableStateFlow<List<Order>?>(null)

//
//    fun getInformatoin() {
//        viewModelScope.launch {
//            orderRepository.getOrdersOfUser(MainActivity.user?.phone!!).catch {
//                failureMessage.value = it.message
//            }
//                .collect {
//                    data.value = it
//                }
//        }
//    }

//    fun readAllOrders() {
//        viewModelScope.launch {
//            orderRepository.getOrders().catch {
//                failureMessage.value = it.message
//            }
//                .collect {
//                    data.value = it
//                }
//        }
//    }
}