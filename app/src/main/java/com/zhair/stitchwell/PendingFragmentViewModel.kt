package com.coderobust.handcraftsshop.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhair.stitchwell.MainActivity
import com.zhair.stitchwell.Order
import com.zhair.stitchwell.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class PendingFragmentViewModel : ViewModel() {
    private val orderRepository = OrderRepository()

    val failureMessage = MutableStateFlow<String?>(null)
    val data = MutableStateFlow<List<Order>?>(null)

    init {
//        readOrders()
//        readHandcrafts

    }


    fun readOrders() {
        viewModelScope.launch {
            orderRepository.getOrdersOfUserPending(MainActivity.user?.phone!!).catch {
                failureMessage.value = it.message
            }
                .collect {
                    data.value = it
                }
        }
    }
    fun readAllPending() {
        viewModelScope.launch {
            orderRepository.getAllPending().catch {
                failureMessage.value = it.message
            }
                .collect {
                    data.value = it
                }
        }
    }
}