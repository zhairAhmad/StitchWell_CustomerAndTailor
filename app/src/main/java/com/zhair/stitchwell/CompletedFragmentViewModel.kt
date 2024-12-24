package com.coderobust.handcraftsshop.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhair.stitchwell.MainActivity
import com.zhair.stitchwell.Order
import com.zhair.stitchwell.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CompletedFragmentViewModel : ViewModel() {
    private val orderRepository = OrderRepository()

    val failureMessage = MutableStateFlow<String?>(null)
    val data = MutableStateFlow<List<Order>?>(null)




    fun readOrders() {
        viewModelScope.launch {
            orderRepository.getOrdersOfUserCompleted(MainActivity.user?.phone!!).catch {
                failureMessage.value = it.message
            }
                .collect {
                    data.value = it
                }
        }
    }

    fun readAllCompleted() {
        viewModelScope.launch {
            orderRepository.getAllCompleted().catch {
                failureMessage.value = it.message
            }
                .collect {
                    data.value = it
                }
        }
    }
}