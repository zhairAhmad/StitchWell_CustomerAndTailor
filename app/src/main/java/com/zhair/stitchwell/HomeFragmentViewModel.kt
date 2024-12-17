package com.coderobust.handcraftsshop.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhair.stitchwell.Order
import com.zhair.stitchwell.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeFragmentViewModel : ViewModel() {
    val orderRepository = OrderRepository()
    val failureMessage = MutableStateFlow<String?>(null)
    val data = MutableStateFlow<List<Order>?>(null)

    init {
        readOrders()
    }

    fun readOrders() {
        viewModelScope.launch {
            orderRepository.getOrders().catch {
                failureMessage.value = it.message
            }
                .collect {
                    data.value = it
                }
        }
    }
}